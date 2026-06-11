package com.wufoo.clone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseSubmitRequest {

    private String submitterName;
    private String submitterEmail;
    private Map<Long, String> fieldValues;
}
