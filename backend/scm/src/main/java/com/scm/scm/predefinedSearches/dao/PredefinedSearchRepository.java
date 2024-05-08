package com.scm.scm.predefinedSearches.dao;

import com.scm.scm.predefinedSearches.vao.PredefinedSearch;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PredefinedSearchRepository extends MongoRepository<PredefinedSearch, String> {
    List<PredefinedSearch> findByUser(String user);
    List<PredefinedSearch> findByOnTenant(String tenant);
}
