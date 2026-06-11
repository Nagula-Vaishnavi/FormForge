package com.wufoo.clone.controller;

import com.wufoo.clone.dto.request.ResponseSubmitRequest;
import com.wufoo.clone.dto.response.FieldResponse;
import com.wufoo.clone.dto.response.FormResponse;
import com.wufoo.clone.entity.Form;
import com.wufoo.clone.entity.FormField;
import com.wufoo.clone.exception.ResourceNotFoundException;
import com.wufoo.clone.repository.FormFieldRepository;
import com.wufoo.clone.repository.FormRepository;
import com.wufoo.clone.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public/forms")
@RequiredArgsConstructor
public class PublicFormController {

    private final FormRepository formRepository;
    private final FormFieldRepository formFieldRepository;
    private final ResponseService responseService;

    @GetMapping("/{publicUrl}")
    public ResponseEntity<Map<String, Object>> getPublicForm(@PathVariable String publicUrl) {
        Form form = formRepository.findByPublicUrl(publicUrl)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with public URL: " + publicUrl));

        if (!form.getIsPublished()) {
            throw new ResourceNotFoundException("Form is not published");
        }

        List<FormField> fields = formFieldRepository.findByFormOrderByOrderIndexAsc(form);
        List<FieldResponse> fieldResponses = fields.stream()
                .map(this::mapToFieldResponse)
                .collect(Collectors.toList());

        FormResponse formResponse = FormResponse.builder()
                .id(form.getId())
                .userId(form.getUser() != null ? form.getUser().getId() : null)
                .title(form.getTitle())
                .description(form.getDescription())
                .publicUrl(form.getPublicUrl())
                .isPublished(form.getIsPublished())
                .createdAt(form.getCreatedAt())
                .build();

        Map<String, Object> response = Map.of(
                "form", formResponse,
                "fields", fieldResponses
        );

        return ResponseEntity.ok(response);
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

    @PostMapping("/{publicUrl}/submit")
    public ResponseEntity<Map<String, Long>> submitResponse(
            @PathVariable String publicUrl,
            @Valid @RequestBody ResponseSubmitRequest responseSubmitRequest,
            HttpServletRequest request) {

        String ipAddress = getClientIpAddress(request);
        Long responseId = responseService.submitResponse(publicUrl, responseSubmitRequest, ipAddress);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("responseId", responseId));
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
