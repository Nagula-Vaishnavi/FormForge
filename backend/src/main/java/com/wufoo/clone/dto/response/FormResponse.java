package com.wufoo.clone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormResponse {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String publicUrl;
    private Boolean isPublished;
    private LocalDateTime createdAt;
}
