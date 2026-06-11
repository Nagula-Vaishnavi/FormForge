package com.wufoo.clone.service.impl;

import com.wufoo.clone.dto.response.AnalyticsResponse;
import com.wufoo.clone.entity.Form;
import com.wufoo.clone.entity.Response;
import com.wufoo.clone.entity.User;
import com.wufoo.clone.repository.FormRepository;
import com.wufoo.clone.repository.ResponseRepository;
import com.wufoo.clone.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final FormRepository formRepository;
    private final ResponseRepository responseRepository;

    @Override
    public AnalyticsResponse getAnalytics(User user) {
        List<Form> forms = formRepository.findByUser(user);
        long totalForms = forms.size();
        long publishedForms = forms.stream().filter(Form::getIsPublished).count();

        long totalResponses = forms.stream()
                .mapToLong(form -> responseRepository.findByForm(form).size())
                .sum();

        double averageResponsesPerForm = totalForms > 0 ? (double) totalResponses / totalForms : 0;

        // Get responses by field type (simplified)
        Map<String, Long> responsesByFieldType = new HashMap<>();
        responsesByFieldType.put("TEXT", totalResponses);
        responsesByFieldType.put("EMAIL", totalResponses);
        responsesByFieldType.put("NUMBER", totalResponses);

        // Get responses by day (last 7 days)
        Map<String, Long> responsesByDay = getResponsesByDay(forms);

        return AnalyticsResponse.builder()
                .totalForms(totalForms)
                .totalResponses(totalResponses)
                .publishedForms(publishedForms)
                .averageResponsesPerForm(averageResponsesPerForm)
                .totalViews(totalResponses * 2) // Simplified: assume 2 views per response
                .responsesByFieldType(responsesByFieldType)
                .responsesByDay(responsesByDay)
                .build();
    }

    @Override
    public AnalyticsResponse getFormAnalytics(Long formId, User user) {
        if (formId == null) {
            throw new IllegalArgumentException("Form ID cannot be null");
        }
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));

        if (!Objects.equals(form.getUser().getId(), user.getId())) {
            throw new RuntimeException("Unauthorized access to form analytics");
        }

        List<Response> responses = responseRepository.findByForm(form);
        long totalResponses = responses.size();

        Map<String, Long> responsesByFieldType = new HashMap<>();
        responsesByFieldType.put("TOTAL", totalResponses);

        Map<String, Long> responsesByDay = getResponsesByDay(List.of(form));

        return AnalyticsResponse.builder()
                .totalForms(1L)
                .totalResponses(totalResponses)
                .publishedForms(form.getIsPublished() ? 1L : 0L)
                .averageResponsesPerForm((double) totalResponses)
                .totalViews(totalResponses * 2)
                .responsesByFieldType(responsesByFieldType)
                .responsesByDay(responsesByDay)
                .build();
    }

    private Map<String, Long> getResponsesByDay(List<Form> forms) {
        Map<String, Long> responsesByDay = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Initialize last 7 days with 0
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            responsesByDay.put(date.format(formatter), 0L);
        }

        // Count responses by day
        for (Form form : forms) {
            List<Response> responses = responseRepository.findByForm(form);
            for (Response response : responses) {
                String dateKey = response.getSubmittedAt().format(formatter);
                responsesByDay.put(dateKey, responsesByDay.getOrDefault(dateKey, 0L) + 1);
            }
        }

        return responsesByDay;
    }
}
