package com.wufoo.clone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponse {

    private Long totalForms;
    private Long totalResponses;
    private Long publishedForms;
    private Double averageResponsesPerForm;
    private Long totalViews;
    private Map<String, Long> responsesByFieldType;
    private Map<String, Long> responsesByDay;
}
