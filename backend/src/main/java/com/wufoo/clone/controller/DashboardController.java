package com.wufoo.clone.controller;

import com.wufoo.clone.config.CustomUserDetails;
import com.wufoo.clone.dto.response.DashboardStatsResponse;
import com.wufoo.clone.dto.response.FormResponse;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        DashboardStatsResponse stats = dashboardService.getDashboardStats(user);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/recent-forms")
    public ResponseEntity<List<FormResponse>> getRecentForms(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "5") int limit) {
        User user = userDetails.getUser();
        List<FormResponse> recentForms = dashboardService.getRecentForms(user, limit);
        return ResponseEntity.ok(recentForms);
    }

    @GetMapping("/recent-responses")
    public ResponseEntity<List<Long>> getRecentResponses(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "5") int limit) {
        User user = userDetails.getUser();
        List<Long> recentResponses = dashboardService.getRecentResponses(user, limit);
        return ResponseEntity.ok(recentResponses);
    }
}
