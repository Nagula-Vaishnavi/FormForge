package com.wufoo.clone.service;

import com.wufoo.clone.dto.request.FieldCreateRequest;
import com.wufoo.clone.dto.request.FieldUpdateRequest;
import com.wufoo.clone.dto.response.FieldResponse;
import com.wufoo.clone.entity.User;

import java.util.List;

public interface FormFieldService {

    /**
     * Add a new field to a form
     * @param formId the form ID
     * @param fieldCreateRequest the field creation details
     * @param user the authenticated user
     * @return the created field response
     */
    FieldResponse addField(Long formId, FieldCreateRequest fieldCreateRequest, User user);

    /**
     * Get all fields of a form
     * @param formId the form ID
     * @param user the authenticated user
     * @return list of fields ordered by orderIndex
     */
    List<FieldResponse> getFieldsByForm(Long formId, User user);

    /**
     * Update a field
     * @param formId the form ID
     * @param fieldId the field ID
     * @param fieldUpdateRequest the update details
     * @param user the authenticated user
     * @return the updated field response
     */
    FieldResponse updateField(Long formId, Long fieldId, FieldUpdateRequest fieldUpdateRequest, User user);

    /**
     * Delete a field
     * @param formId the form ID
     * @param fieldId the field ID
     * @param user the authenticated user
     */
    void deleteField(Long formId, Long fieldId, User user);
}
