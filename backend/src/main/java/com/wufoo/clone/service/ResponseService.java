package com.wufoo.clone.service;

import com.wufoo.clone.dto.request.ResponseSubmitRequest;
import com.wufoo.clone.dto.response.ResponseDetailResponse;
import com.wufoo.clone.entity.User;

import java.util.List;

public interface ResponseService {

    /**
     * Submit a response to a public form
     * @param publicUrl the public URL of the form
     * @param responseSubmitRequest the response data
     * @param ipAddress the IP address of the submitter
     * @return the response ID
     */
    Long submitResponse(String publicUrl, ResponseSubmitRequest responseSubmitRequest, String ipAddress);

    /**
     * Export form responses to CSV
     * @param formId the form ID
     * @param user the authenticated user
     * @return CSV content as string
     */
    String exportResponsesToCSV(Long formId, User user);

    /**
     * Get all responses for a form
     * @param formId the form ID
     * @param user the authenticated user
     * @return list of response details
     */
    List<ResponseDetailResponse> getResponsesByForm(Long formId, User user);
}
