package com.wufoo.clone.service;

import com.wufoo.clone.dto.response.DashboardStatsResponse;
import com.wufoo.clone.dto.response.FormResponse;
import com.wufoo.clone.entity.User;

import java.util.List;

public interface DashboardService {

    /**
     * Get dashboard statistics for the authenticated user
     * @param user the authenticated user
     * @return dashboard statistics
     */
    DashboardStatsResponse getDashboardStats(User user);

    /**
     * Get recent forms for the authenticated user
     * @param user the authenticated user
     * @param limit the number of recent forms to retrieve
     * @return list of recent forms
     */
    List<FormResponse> getRecentForms(User user, int limit);

    /**
     * Get recent responses for the authenticated user
     * @param user the authenticated user
     * @param limit the number of recent responses to retrieve
     * @return list of recent response IDs
     */
    List<Long> getRecentResponses(User user, int limit);
}
