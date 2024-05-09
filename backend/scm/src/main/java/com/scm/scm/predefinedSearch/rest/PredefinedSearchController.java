package com.scm.scm.predefinedSearch.rest;

import com.scm.scm.predefinedSearch.services.PredefinedSearchServices;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/predefined_searches")
public class PredefinedSearchController {

    @Autowired
    private PredefinedSearchServices predefinedSearchServices;

    @GetMapping
    public ResponseEntity<List<PredefinedSearch>> getPredefinedSearches() {
        List<PredefinedSearch> predefinedSearches = predefinedSearchServices.getAllPredefinedSearches();
        return ResponseEntity.ok(predefinedSearches);
    }

    @GetMapping("/{predefined_search_id}")
    public ResponseEntity<PredefinedSearch> getPredefinedSearch(@PathVariable("predefined_search_id") String predefinedSearchId) {
        PredefinedSearch predefinedSearch = predefinedSearchServices.getPredefinedSearchById(predefinedSearchId);
        return ResponseEntity.ok(predefinedSearch);
    }

    @PostMapping
    public ResponseEntity<PredefinedSearch> createPredefinedSearch(@RequestBody PredefinedSearch predefinedSearch) {
        PredefinedSearch createdPredefinedSearch = predefinedSearchServices.addPredefinedSearch(predefinedSearch);
        return ResponseEntity.ok(createdPredefinedSearch);
    }

    @PutMapping
    public ResponseEntity<PredefinedSearch> updatePredefinedSearch(@RequestBody PredefinedSearch predefinedSearch) {
        PredefinedSearch updatedPredefinedSearch = predefinedSearchServices.updatePredefinedSearch(predefinedSearch);
        return ResponseEntity.ok(updatedPredefinedSearch);
    }

    @DeleteMapping("/{predefined_search_id}")
    public ResponseEntity<String> deletePredefinedSearch(@PathVariable("predefined_search_id") String predefinedSearchId) {
        return ResponseEntity.ok(predefinedSearchServices.deletePredefinedSearch(predefinedSearchId));
    }

    @GetMapping("/user/{user}")
    public ResponseEntity<List<PredefinedSearch>> getPredefinedSearchByUser(@PathVariable("user") String user) {
        List<PredefinedSearch> predefinedSearches = predefinedSearchServices.getPredefinedSearchByUser(user);
        return ResponseEntity.ok(predefinedSearches);
    }

    @GetMapping("/tenant/{tenant}")
    public ResponseEntity<List<PredefinedSearch>> getPredefinedSearchByTenant(@PathVariable("tenant") String tenant) {
        List<PredefinedSearch> predefinedSearches = predefinedSearchServices.getPredefinedSearchByTenant(tenant);
        return ResponseEntity.ok(predefinedSearches);
    }

}
