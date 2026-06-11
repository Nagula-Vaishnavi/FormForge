package com.wufoo.clone.controller;

import com.wufoo.clone.config.CustomUserDetails;
import com.wufoo.clone.dto.response.AnalyticsResponse;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<AnalyticsResponse> getAnalytics(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        AnalyticsResponse analytics = analyticsService.getAnalytics(user);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/form/{formId}")
    public ResponseEntity<AnalyticsResponse> getFormAnalytics(
            @PathVariable Long formId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        AnalyticsResponse analytics = analyticsService.getFormAnalytics(formId, user);
        return ResponseEntity.ok(analytics);
    }
}
