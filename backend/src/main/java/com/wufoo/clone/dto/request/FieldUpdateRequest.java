package com.wufoo.clone.dto.request;

import com.wufoo.clone.entity.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldUpdateRequest {

    private FieldType fieldType;

    private String label;

    private String placeholder;

    private Boolean required;

    private String options;

    private Integer orderIndex;
}
