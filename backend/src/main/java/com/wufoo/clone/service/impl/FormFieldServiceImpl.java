package com.wufoo.clone.service.impl;

import com.wufoo.clone.dto.request.FieldCreateRequest;
import com.wufoo.clone.dto.request.FieldUpdateRequest;
import com.wufoo.clone.dto.response.FieldResponse;
import com.wufoo.clone.entity.Form;
import com.wufoo.clone.entity.FormField;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.exception.ResourceNotFoundException;
import com.wufoo.clone.repository.FormFieldRepository;
import com.wufoo.clone.repository.FormRepository;
import com.wufoo.clone.service.FormFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FormFieldServiceImpl implements FormFieldService {

    private final FormFieldRepository formFieldRepository;
    private final FormRepository formRepository;

    @Override
    public FieldResponse addField(Long formId, FieldCreateRequest fieldCreateRequest, User user) {
        Form form = formRepository.findByIdAndUser(formId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + formId));

        String optionsJson = convertOptionsToJson(fieldCreateRequest.getOptions());

        FormField formField = FormField.builder()
                .form(form)
                .fieldType(fieldCreateRequest.getFieldType())
                .label(fieldCreateRequest.getLabel())
                .placeholder(fieldCreateRequest.getPlaceholder())
                .required(fieldCreateRequest.getRequired() != null ? fieldCreateRequest.getRequired() : false)
                .options(optionsJson)
                .orderIndex(fieldCreateRequest.getOrderIndex() != null ? fieldCreateRequest.getOrderIndex() : 0)
                .build();

        @SuppressWarnings("null")
        FormField savedField = formFieldRepository.save(formField);
        return mapToFieldResponse(savedField);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FieldResponse> getFieldsByForm(Long formId, User user) {
        Form form = formRepository.findByIdAndUser(formId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + formId));

        List<FormField> fields = formFieldRepository.findByFormOrderByOrderIndexAsc(form);
        return fields.stream()
                .map(this::mapToFieldResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FieldResponse updateField(Long formId, Long fieldId, FieldUpdateRequest fieldUpdateRequest, User user) {
        Form form = formRepository.findByIdAndUser(formId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + formId));

        FormField formField = formFieldRepository.findByIdAndForm(fieldId, form)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + fieldId));

        if (fieldUpdateRequest.getFieldType() != null) {
            formField.setFieldType(fieldUpdateRequest.getFieldType());
        }
        if (fieldUpdateRequest.getLabel() != null) {
            formField.setLabel(fieldUpdateRequest.getLabel());
        }
        if (fieldUpdateRequest.getPlaceholder() != null) {
            formField.setPlaceholder(fieldUpdateRequest.getPlaceholder());
        }
        if (fieldUpdateRequest.getRequired() != null) {
            formField.setRequired(fieldUpdateRequest.getRequired());
        }
        if (fieldUpdateRequest.getOptions() != null) {
            String optionsJson = convertOptionsToJson(fieldUpdateRequest.getOptions());
            formField.setOptions(optionsJson);
        }
        if (fieldUpdateRequest.getOrderIndex() != null) {
            formField.setOrderIndex(fieldUpdateRequest.getOrderIndex());
        }

        @SuppressWarnings("null")
        FormField updatedField = formFieldRepository.save(formField);
        return mapToFieldResponse(updatedField);
    }

    @Override
    @SuppressWarnings("null")
    public void deleteField(Long formId, Long fieldId, User user) {
        Form form = formRepository.findByIdAndUser(formId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + formId));

        FormField formField = formFieldRepository.findByIdAndForm(fieldId, form)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + fieldId));

        formFieldRepository.delete(formField);
    }

    private FieldResponse mapToFieldResponse(FormField formField) {
        return FieldResponse.builder()
                .id(formField.getId())
                .formId(formField.getForm() != null ? formField.getForm().getId() : null)
                .fieldType(formField.getFieldType())
                .label(formField.getLabel())
                .placeholder(formField.getPlaceholder())
                .required(formField.getRequired())
                .options(formField.getOptions())
                .orderIndex(formField.getOrderIndex())
                .createdAt(formField.getCreatedAt())
                .build();
    }

    private String convertOptionsToJson(String options) {
        if (options == null || options.trim().isEmpty()) {
            return null;
        }
        
        String[] optionArray = options.split(",");
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < optionArray.length; i++) {
            if (i > 0) {
                jsonBuilder.append(",");
            }
            String option = optionArray[i].trim();
            jsonBuilder.append("\"").append(option.replace("\"", "\\\"")).append("\"");
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}
