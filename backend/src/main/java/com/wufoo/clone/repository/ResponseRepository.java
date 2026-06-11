package com.wufoo.clone.repository;

import com.wufoo.clone.entity.Form;
import com.wufoo.clone.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

    /**
     * Find all responses by form
     * @param form the form
     * @return list of responses
     */
    List<Response> findByForm(Form form);

    /**
     * Find responses by form ordered by submission date
     * @param form the form
     * @return list of responses ordered by submittedAt
     */
    List<Response> findByFormOrderBySubmittedAtDesc(Form form);
}
