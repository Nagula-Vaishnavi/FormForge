package com.wufoo.clone.service;

import com.wufoo.clone.dto.response.AnalyticsResponse;
import com.wufoo.clone.entity.User;

public interface AnalyticsService {

    /**
     * Get overall analytics for the user
     * @param user the authenticated user
     * @return analytics data
     */
    AnalyticsResponse getAnalytics(User user);

    /**
     * Get analytics for a specific form
     * @param formId the form ID
     * @param user the authenticated user
     * @return analytics data for the form
     */
    AnalyticsResponse getFormAnalytics(Long formId, User user);
}
