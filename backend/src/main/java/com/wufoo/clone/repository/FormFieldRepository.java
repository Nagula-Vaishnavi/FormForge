package com.wufoo.clone.repository;

import com.wufoo.clone.entity.Form;
import com.wufoo.clone.entity.FormField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormFieldRepository extends JpaRepository<FormField, Long> {

    /**
     * Find all fields by form
     * @param form the form
     * @return list of fields ordered by orderIndex
     */
    List<FormField> findByFormOrderByOrderIndexAsc(Form form);

    /**
     * Find a field by ID and form
     * @param id the field ID
     * @param form the form
     * @return Optional containing the field if found
     */
    Optional<FormField> findByIdAndForm(Long id, Form form);

    /**
     * Check if a field belongs to a form
     * @param id the field ID
     * @param form the form
     * @return true if the field belongs to the form
     */
    boolean existsByIdAndForm(Long id, Form form);

    /**
     * Delete all fields by form
     * @param form the form
     */
    void deleteByForm(Form form);
}
