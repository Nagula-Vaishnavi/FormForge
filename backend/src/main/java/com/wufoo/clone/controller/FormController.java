package com.wufoo.clone.controller;

import com.wufoo.clone.config.CustomUserDetails;
import com.wufoo.clone.dto.request.FormCreateRequest;
import com.wufoo.clone.dto.request.FormUpdateRequest;
import com.wufoo.clone.dto.response.FormResponse;
import com.wufoo.clone.dto.response.ResponseDetailResponse;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.service.FormService;
import com.wufoo.clone.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forms")
@RequiredArgsConstructor
public class FormController {

    private final FormService formService;
    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<FormResponse> createForm(@Valid @RequestBody FormCreateRequest formCreateRequest,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        FormResponse formResponse = formService.createForm(formCreateRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(formResponse);
    }

    @GetMapping
    public ResponseEntity<List<FormResponse>> getAllForms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        List<FormResponse> forms = formService.getAllForms(user);
        return ResponseEntity.ok(forms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormResponse> getFormById(@PathVariable Long id,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        FormResponse formResponse = formService.getFormById(id, user);
        return ResponseEntity.ok(formResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormResponse> updateForm(@PathVariable Long id,
                                                  @Valid @RequestBody FormUpdateRequest formUpdateRequest,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        FormResponse formResponse = formService.updateForm(id, formUpdateRequest, user);
        return ResponseEntity.ok(formResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        formService.deleteForm(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<FormResponse> publishForm(@PathVariable Long id,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        FormResponse formResponse = formService.publishForm(id, user);
        return ResponseEntity.ok(formResponse);
    }

    @PostMapping("/{id}/unpublish")
    public ResponseEntity<FormResponse> unpublishForm(@PathVariable Long id,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        FormResponse formResponse = formService.unpublishForm(id, user);
        return ResponseEntity.ok(formResponse);
    }

    @GetMapping("/{id}/responses")
    public ResponseEntity<List<ResponseDetailResponse>> getResponses(@PathVariable Long id,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        List<ResponseDetailResponse> responses = responseService.getResponsesByForm(id, user);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}/export/csv")
    public ResponseEntity<String> exportResponsesToCSV(@PathVariable Long id,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        String csvContent = responseService.exportResponsesToCSV(id, user);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "form_responses_" + id + ".csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }
}
