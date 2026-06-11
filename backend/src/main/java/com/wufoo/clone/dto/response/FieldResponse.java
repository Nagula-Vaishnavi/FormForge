package com.wufoo.clone.dto.response;

import com.wufoo.clone.entity.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldResponse {

    private Long id;
    private Long formId;
    private FieldType fieldType;
    private String label;
    private String placeholder;
    private Boolean required;
    private String options;
    private Integer orderIndex;
    private LocalDateTime createdAt;
}
