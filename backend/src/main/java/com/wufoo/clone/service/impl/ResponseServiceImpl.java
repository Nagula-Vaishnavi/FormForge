package com.wufoo.clone.service.impl;

import com.wufoo.clone.dto.request.ResponseSubmitRequest;
import com.wufoo.clone.dto.response.ResponseDetailResponse;
import com.wufoo.clone.entity.Form;
import com.wufoo.clone.entity.FormField;
import com.wufoo.clone.entity.Response;
import com.wufoo.clone.entity.ResponseValue;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.exception.ResourceNotFoundException;
import com.wufoo.clone.repository.FormFieldRepository;
import com.wufoo.clone.repository.FormRepository;
import com.wufoo.clone.repository.ResponseRepository;
import com.wufoo.clone.repository.ResponseValueRepository;
import com.wufoo.clone.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ResponseServiceImpl implements ResponseService {

    private final ResponseRepository responseRepository;
    private final ResponseValueRepository responseValueRepository;
    private final FormRepository formRepository;
    private final FormFieldRepository formFieldRepository;

    @Override
    @SuppressWarnings("nullness")
    public Long submitResponse(String publicUrl, ResponseSubmitRequest responseSubmitRequest, String ipAddress) {
        Form form = formRepository.findByPublicUrl(publicUrl)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with public URL: " + publicUrl));

        if (!form.getIsPublished()) {
            throw new ResourceNotFoundException("Form is not published");
        }

        Response response = Response.builder()
                .form(form)
                .submitterName(responseSubmitRequest.getSubmitterName())
                .submitterEmail(responseSubmitRequest.getSubmitterEmail())
                .ipAddress(ipAddress)
                .build();

        @SuppressWarnings("null")
        Response savedResponse = responseRepository.save(response);

        if (responseSubmitRequest.getFieldValues() != null) {
            for (Long fieldId : responseSubmitRequest.getFieldValues().keySet()) {
                if (fieldId == null) {
                    continue;
                }
                FormField field = formFieldRepository.findById(fieldId)
                        .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + fieldId));

                if (!field.getForm().getId().equals(form.getId())) {
                    throw new ResourceNotFoundException("Field does not belong to this form");
                }

                ResponseValue responseValue = ResponseValue.builder()
                        .response(savedResponse)
                        .field(field)
                        .value(responseSubmitRequest.getFieldValues().get(fieldId))
                        .build();

                @SuppressWarnings({"null", "unused"})
                ResponseValue savedValue = responseValueRepository.save(responseValue);
            }
        }

        return savedResponse.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public String exportResponsesToCSV(Long formId, User user) {
        Form form = formRepository.findByIdAndUser(formId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + formId));

        List<FormField> fields = formFieldRepository.findByFormOrderByOrderIndexAsc(form);
        List<Response> responses = responseRepository.findByFormOrderBySubmittedAtDesc(form);

        System.out.println("=== CSV Export Debug ===");
        System.out.println("Form ID: " + formId);
        System.out.println("Number of fields: " + fields.size());
        System.out.println("Number of responses: " + responses.size());

        StringBuilder csv = new StringBuilder();

        // Header row
        csv.append("Response Number,Submitted At");
        for (FormField field : fields) {
            csv.append(",").append(escapeCSV(field.getLabel()));
            System.out.println("Field ID: " + field.getId() + ", Label: " + field.getLabel());
        }
        csv.append("\n");

        // Data rows
        for (int i = 0; i < responses.size(); i++) {
            Response response = responses.get(i);
            // Calculate response number based on position (1-indexed)
            int responseNumber = responses.size() - i; // Reverse since responses are ordered DESC
            csv.append(responseNumber).append(",");
            csv.append(escapeCSV(response.getSubmittedAt().toString()));

            List<ResponseValue> responseValueList = responseValueRepository.findByResponse(response);
            System.out.println("Response ID: " + response.getId() + ", Number of values: " + responseValueList.size());

            Map<Long, ResponseValue> responseValues = responseValueList
                    .stream()
                    .collect(Collectors.toMap(rv -> rv.getField().getId(), rv -> rv));

            for (FormField field : fields) {
                ResponseValue responseValue = responseValues.get(field.getId());
                String value = responseValue != null ? responseValue.getValue() : "";
                System.out.println("  Field ID: " + field.getId() + ", Value: " + value);
                csv.append(",").append(escapeCSV(value));
            }
            csv.append("\n");
        }

        return csv.toString();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDetailResponse> getResponsesByForm(Long formId, User user) {
        Form form = formRepository.findByIdAndUser(formId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + formId));

        List<Response> responses = responseRepository.findByFormOrderBySubmittedAtDesc(form);

        // Calculate response numbers based on position (1-indexed, newest first)
        List<ResponseDetailResponse> responseDetails = new ArrayList<>();
        for (int i = 0; i < responses.size(); i++) {
            Response response = responses.get(i);
            int responseNumber = responses.size() - i; // Reverse since responses are ordered DESC
            responseDetails.add(mapToResponseDetailResponse(response, responseNumber));
        }

        return responseDetails;
    }

    private ResponseDetailResponse mapToResponseDetailResponse(Response response, int responseNumber) {
        List<ResponseValue> responseValues = responseValueRepository.findByResponse(response);
        Map<Long, String> fieldValues = responseValues.stream()
                .collect(Collectors.toMap(rv -> rv.getField().getId(), ResponseValue::getValue));

        return ResponseDetailResponse.builder()
                .id(response.getId())
                .responseNumber(responseNumber)
                .formId(response.getForm().getId())
                .submitterName(response.getSubmitterName())
                .submitterEmail(response.getSubmitterEmail())
                .ipAddress(response.getIpAddress())
                .submittedAt(response.getSubmittedAt())
                .fieldValues(fieldValues)
                .build();
    }

    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
