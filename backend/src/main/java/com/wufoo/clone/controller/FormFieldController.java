package com.wufoo.clone.controller;

import com.wufoo.clone.config.CustomUserDetails;
import com.wufoo.clone.dto.request.FieldCreateRequest;
import com.wufoo.clone.dto.request.FieldUpdateRequest;
import com.wufoo.clone.dto.response.FieldResponse;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.service.FormFieldService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forms/{formId}/fields")
@RequiredArgsConstructor
public class FormFieldController {

    private final FormFieldService formFieldService;

    @PostMapping
    public ResponseEntity<FieldResponse> addField(@PathVariable Long formId,
                                                   @Valid @RequestBody FieldCreateRequest fieldCreateRequest,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        FieldResponse fieldResponse = formFieldService.addField(formId, fieldCreateRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(fieldResponse);
    }

    @GetMapping
    public ResponseEntity<List<FieldResponse>> getFieldsByForm(@PathVariable Long formId,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        List<FieldResponse> fields = formFieldService.getFieldsByForm(formId, user);
        return ResponseEntity.ok(fields);
    }

    @PutMapping("/{fieldId}")
    public ResponseEntity<FieldResponse> updateField(@PathVariable Long formId,
                                                      @PathVariable Long fieldId,
                                                      @Valid @RequestBody FieldUpdateRequest fieldUpdateRequest,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        FieldResponse fieldResponse = formFieldService.updateField(formId, fieldId, fieldUpdateRequest, user);
        return ResponseEntity.ok(fieldResponse);
    }

    @DeleteMapping("/{fieldId}")
    public ResponseEntity<Void> deleteField(@PathVariable Long formId,
                                             @PathVariable Long fieldId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        formFieldService.deleteField(formId, fieldId, user);
        return ResponseEntity.noContent().build();
    }
}
