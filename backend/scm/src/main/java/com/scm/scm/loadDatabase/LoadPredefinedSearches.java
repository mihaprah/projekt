package com.scm.scm.loadDatabase;

import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import com.scm.scm.predefinedSearch.vao.SortOrientation;
import com.scm.scm.support.mongoTemplate.MongoTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class LoadPredefinedSearches {

    private static final Logger log = Logger.getLogger(LoadContacts.class.toString());


    @Autowired
    private MongoTemplate mongoTemplate;

    public void createPredefinedSearches() {
        PredefinedSearch predefinedSearch = new PredefinedSearch("global", "user1@example.com", "ACM-May-24-694", "Global search", "", SortOrientation.ASC);
        predefinedSearch.setId(predefinedSearch.generateId(predefinedSearch.getTitle()));
        mongoTemplate.save(predefinedSearch);
        log.info("Loaded test PredefinedSearches into the database.");
    }
}
