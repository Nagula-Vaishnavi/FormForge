package com.wufoo.clone.repository;

import com.wufoo.clone.entity.Form;
import com.wufoo.clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {

    /**
     * Find all forms by user
     * @param user the user who owns the forms
     * @return list of forms belonging to the user
     */
    List<Form> findByUser(User user);

    /**
     * Find a form by ID and user
     * @param id the form ID
     * @param user the user who owns the form
     * @return Optional containing the form if found
     */
    Optional<Form> findByIdAndUser(Long id, User user);

    /**
     * Find a form by public URL
     * @param publicUrl the public URL of the form
     * @return Optional containing the form if found
     */
    Optional<Form> findByPublicUrl(String publicUrl);

    /**
     * Check if a form belongs to a user
     * @param id the form ID
     * @param user the user
     * @return true if the form belongs to the user
     */
    boolean existsByIdAndUser(Long id, User user);
}
