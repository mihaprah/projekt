package com.scm.scm.predefinedSearch.rest;

import com.scm.scm.predefinedSearch.dto.PredefinedSearchDTO;
import com.scm.scm.predefinedSearch.services.PredefinedSearchServices;
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
    public ResponseEntity<List<PredefinedSearchDTO>> getPredefinedSearches() {
        List<PredefinedSearchDTO> predefinedSearches = predefinedSearchServices.getAllPredefinedSearches();
        return ResponseEntity.ok(predefinedSearches);
    }

    @GetMapping("/{predefined_search_id}")
    public ResponseEntity<PredefinedSearchDTO> getPredefinedSearch(@PathVariable("predefined_search_id") String predefinedSearchId) {
        PredefinedSearchDTO predefinedSearch = predefinedSearchServices.getPredefinedSearchById(predefinedSearchId);
        return ResponseEntity.ok(predefinedSearch);
    }

    @PostMapping
    public ResponseEntity<PredefinedSearchDTO> createPredefinedSearch(@RequestBody PredefinedSearchDTO predefinedSearchDTO) {
        PredefinedSearchDTO createdPredefinedSearch = predefinedSearchServices.addPredefinedSearch(predefinedSearchDTO);
        return ResponseEntity.ok(createdPredefinedSearch);
    }

    @PutMapping
    public ResponseEntity<PredefinedSearchDTO> updatePredefinedSearch(@RequestBody PredefinedSearchDTO predefinedSearchDTO) {
        PredefinedSearchDTO updatedPredefinedSearch = predefinedSearchServices.updatePredefinedSearch(predefinedSearchDTO);
        return ResponseEntity.ok(updatedPredefinedSearch);
    }

    @DeleteMapping("/{predefined_search_id}")
    public ResponseEntity<String> deletePredefinedSearch(@PathVariable("predefined_search_id") String predefinedSearchId) {
        return ResponseEntity.ok(predefinedSearchServices.deletePredefinedSearch(predefinedSearchId));
    }

    @GetMapping("/user/{user}")
    public ResponseEntity<List<PredefinedSearchDTO>> getPredefinedSearchByUser(@PathVariable("user") String user) {
        List<PredefinedSearchDTO> predefinedSearches = predefinedSearchServices.getPredefinedSearchByUser(user);
        return ResponseEntity.ok(predefinedSearches);
    }

    @GetMapping("/tenant/{tenant}")
    public ResponseEntity<List<PredefinedSearchDTO>> getPredefinedSearchByTenant(@PathVariable("tenant") String tenant) {
        List<PredefinedSearchDTO> predefinedSearches = predefinedSearchServices.getPredefinedSearchByTenant(tenant);
        return ResponseEntity.ok(predefinedSearches);
    }

}