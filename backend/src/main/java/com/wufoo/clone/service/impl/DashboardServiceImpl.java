package com.wufoo.clone.service.impl;

import com.wufoo.clone.dto.response.DashboardStatsResponse;
import com.wufoo.clone.dto.response.FormResponse;
import com.wufoo.clone.entity.Form;
import com.wufoo.clone.entity.Response;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.repository.FormRepository;
import com.wufoo.clone.repository.ResponseRepository;
import com.wufoo.clone.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final FormRepository formRepository;
    private final ResponseRepository responseRepository;

    @Override
    public DashboardStatsResponse getDashboardStats(User user) {
        List<Form> forms = formRepository.findByUser(user);
        long totalForms = forms.size();
        long publishedForms = forms.stream().filter(Form::getIsPublished).count();

        long totalResponses = forms.stream()
                .mapToLong(form -> responseRepository.findByForm(form).size())
                .sum();

        return DashboardStatsResponse.builder()
                .totalForms(totalForms)
                .totalResponses(totalResponses)
                .publishedForms(publishedForms)
                .build();
    }

    @Override
    public List<FormResponse> getRecentForms(User user, int limit) {
        List<Form> forms = formRepository.findByUser(user);
        return forms.stream()
                .sorted((f1, f2) -> f2.getCreatedAt().compareTo(f1.getCreatedAt()))
                .limit(limit)
                .map(this::mapToFormResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getRecentResponses(User user, int limit) {
        List<Form> forms = formRepository.findByUser(user);
        return forms.stream()
                .flatMap(form -> responseRepository.findByFormOrderBySubmittedAtDesc(form).stream())
                .sorted((r1, r2) -> r2.getSubmittedAt().compareTo(r1.getSubmittedAt()))
                .limit(limit)
                .map(Response::getId)
                .collect(Collectors.toList());
    }

    private FormResponse mapToFormResponse(Form form) {
        return FormResponse.builder()
                .id(form.getId())
                .userId(form.getUser() != null ? form.getUser().getId() : null)
                .title(form.getTitle())
                .description(form.getDescription())
                .publicUrl(form.getPublicUrl())
                .isPublished(form.getIsPublished())
                .createdAt(form.getCreatedAt())
                .build();
    }
}
