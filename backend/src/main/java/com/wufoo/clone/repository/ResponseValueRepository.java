package com.wufoo.clone.repository;

import com.wufoo.clone.entity.FormField;
import com.wufoo.clone.entity.Response;
import com.wufoo.clone.entity.ResponseValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseValueRepository extends JpaRepository<ResponseValue, Long> {

    /**
     * Find all values by response
     * @param response the response
     * @return list of response values
     */
    List<ResponseValue> findByResponse(Response response);

    /**
     * Find all values by field
     * @param field the form field
     * @return list of response values
     */
    List<ResponseValue> findByField(FormField field);

    /**
     * Delete all values by response
     * @param response the response
     */
    void deleteByResponse(Response response);
}
