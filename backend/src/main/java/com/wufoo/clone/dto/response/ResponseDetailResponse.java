package com.wufoo.clone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDetailResponse {

    private Long id;
    private Integer responseNumber;
    private Long formId;
    private String submitterName;
    private String submitterEmail;
    private String ipAddress;
    private LocalDateTime submittedAt;
    private Map<Long, String> fieldValues;
}
