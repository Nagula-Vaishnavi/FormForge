package com.wufoo.clone.service.impl;

import com.wufoo.clone.dto.request.FormCreateRequest;
import com.wufoo.clone.dto.request.FormUpdateRequest;
import com.wufoo.clone.dto.response.FormResponse;
import com.wufoo.clone.entity.Form;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.exception.ResourceNotFoundException;
import com.wufoo.clone.repository.FormRepository;
import com.wufoo.clone.service.FormService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;

    @Override
    @SuppressWarnings("null")
    public FormResponse createForm(FormCreateRequest formCreateRequest, User user) {
        Form form = Form.builder()
                .user(user)
                .title(formCreateRequest.getTitle())
                .description(formCreateRequest.getDescription())
                .isPublished(false)
                .build();

        return mapToFormResponse(formRepository.save(form));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormResponse> getAllForms(User user) {
        List<Form> forms = formRepository.findByUser(user);
        return forms.stream()
                .map(this::mapToFormResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FormResponse getFormById(Long id, User user) {
        Form form = formRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + id));
        return mapToFormResponse(form);
    }

    @Override
    public FormResponse updateForm(Long id, FormUpdateRequest formUpdateRequest, User user) {
        Form form = formRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + id));

        form.setTitle(formUpdateRequest.getTitle());
        form.setDescription(formUpdateRequest.getDescription());

        Form updatedForm = formRepository.save(form);
        return mapToFormResponse(updatedForm);
    }

    @Override
    @SuppressWarnings("null")
    public void deleteForm(Long id, User user) {
        Form form = formRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + id));
        formRepository.delete(form);
    }

    @Override
    public FormResponse publishForm(Long id, User user) {
        Form form = formRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + id));

        if (form.getPublicUrl() == null || form.getPublicUrl().isEmpty()) {
            form.setPublicUrl(generatePublicUrl(form.getId()));
        }

        form.setIsPublished(true);
        Form updatedForm = formRepository.save(form);
        return mapToFormResponse(updatedForm);
    }

    @Override
    public FormResponse unpublishForm(Long id, User user) {
        Form form = formRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + id));

        form.setIsPublished(false);
        Form updatedForm = formRepository.save(form);
        return mapToFormResponse(updatedForm);
    }

    private String generatePublicUrl(Long formId) {
        return "form-" + formId + "-" + System.currentTimeMillis();
    }

    private FormResponse mapToFormResponse(@NonNull Form form) {
        return FormResponse.builder()
                .id(form.getId())
                .userId(form.getUser() != null ? form.getUser().getId() : null)
                .title(form.getTitle())
                .description(form.getDescription())
                .publicUrl(form.getPublicUrl())
                .isPublished(form.getIsPublished())
                .createdAt(form.getCreatedAt())
                .build();
    }
}
