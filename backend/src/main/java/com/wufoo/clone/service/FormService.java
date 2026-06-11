package com.wufoo.clone.service;

import com.wufoo.clone.dto.request.FormCreateRequest;
import com.wufoo.clone.dto.request.FormUpdateRequest;
import com.wufoo.clone.dto.response.FormResponse;
import com.wufoo.clone.entity.User;

import java.util.List;

public interface FormService {

    /**
     * Create a new form for the authenticated user
     * @param formCreateRequest the form creation details
     * @param user the authenticated user
     * @return the created form response
     */
    FormResponse createForm(FormCreateRequest formCreateRequest, User user);

    /**
     * Get all forms for the authenticated user
     * @param user the authenticated user
     * @return list of forms belonging to the user
     */
    List<FormResponse> getAllForms(User user);

    /**
     * Get a form by ID
     * @param id the form ID
     * @param user the authenticated user
     * @return the form response
     * @throws RuntimeException if form not found or doesn't belong to user
     */
    FormResponse getFormById(Long id, User user);

    /**
     * Update a form
     * @param id the form ID
     * @param formUpdateRequest the update details
     * @param user the authenticated user
     * @return the updated form response
     * @throws RuntimeException if form not found or doesn't belong to user
     */
    FormResponse updateForm(Long id, FormUpdateRequest formUpdateRequest, User user);

    /**
     * Delete a form
     * @param id the form ID
     * @param user the authenticated user
     * @throws RuntimeException if form not found or doesn't belong to user
     */
    void deleteForm(Long id, User user);

    /**
     * Publish a form
     * @param id the form ID
     * @param user the authenticated user
     * @return the updated form response
     * @throws RuntimeException if form not found or doesn't belong to user
     */
    FormResponse publishForm(Long id, User user);

    /**
     * Unpublish a form
     * @param id the form ID
     * @param user the authenticated user
     * @return the updated form response
     * @throws RuntimeException if form not found or doesn't belong to user
     */
    FormResponse unpublishForm(Long id, User user);
}
