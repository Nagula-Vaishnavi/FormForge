package com.wufoo.clone.dto.request;

import com.wufoo.clone.entity.FieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldCreateRequest {

    @NotNull(message = "Field type is required")
    private FieldType fieldType;

    @NotBlank(message = "Label is required")
    private String label;

    private String placeholder;

    private Boolean required;

    private String options;

    private Integer orderIndex;
}
