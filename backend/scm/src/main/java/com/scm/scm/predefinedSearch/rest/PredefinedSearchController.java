package com.scm.scm.predefinedSearch.rest;

import com.google.firebase.auth.FirebaseToken;
import com.scm.scm.predefinedSearch.dto.PredefinedSearchDTO;
import com.scm.scm.predefinedSearch.services.PredefinedSearchServices;
import com.scm.scm.support.security.UserVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/predefined_searches")
public class PredefinedSearchController {

    private final PredefinedSearchServices predefinedSearchServices;
    private final UserVerifyService userVerifyService;

    @Autowired
    public PredefinedSearchController(PredefinedSearchServices predefinedSearchServices, UserVerifyService userVerifyService) {
        this.predefinedSearchServices = predefinedSearchServices;
        this.userVerifyService = userVerifyService;
    }

    @GetMapping(value = "/{predefined_search_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PredefinedSearchDTO> getPredefinedSearch(@PathVariable("predefined_search_id") String predefinedSearchId, @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        PredefinedSearchDTO predefinedSearch = predefinedSearchServices.getPredefinedSearchById(predefinedSearchId);
        return ResponseEntity.ok(predefinedSearch);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PredefinedSearchDTO> createPredefinedSearch(@RequestBody PredefinedSearchDTO predefinedSearchDTO,  @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));
        predefinedSearchDTO.setUser(decodedToken.getEmail());

        PredefinedSearchDTO createdPredefinedSearch = predefinedSearchServices.addPredefinedSearch(predefinedSearchDTO);
        return ResponseEntity.ok(createdPredefinedSearch);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PredefinedSearchDTO> updatePredefinedSearch(@RequestBody PredefinedSearchDTO predefinedSearchDTO,  @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        PredefinedSearchDTO updatedPredefinedSearch = predefinedSearchServices.updatePredefinedSearch(predefinedSearchDTO);
        return ResponseEntity.ok(updatedPredefinedSearch);
    }

    @DeleteMapping("/{predefined_search_id}")
    public ResponseEntity<String> deletePredefinedSearch(@PathVariable("predefined_search_id") String predefinedSearchId,  @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        return ResponseEntity.ok(predefinedSearchServices.deletePredefinedSearch(predefinedSearchId));
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PredefinedSearchDTO>> getPredefinedSearchByUser(@RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        List<PredefinedSearchDTO> predefinedSearches = predefinedSearchServices.getPredefinedSearchByUser(decodedToken.getEmail());
        return ResponseEntity.ok(predefinedSearches);
    }

    @GetMapping(value = "/tenant/{tenant}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PredefinedSearchDTO>> getPredefinedSearchByTenant(@PathVariable("tenant") String tenant,  @RequestHeader("userToken") String userToken) {
        FirebaseToken decodedToken = userVerifyService.verifyUserToken(userToken.replace("Bearer ", ""));

        List<PredefinedSearchDTO> predefinedSearches = predefinedSearchServices.getPredefinedSearchByTenant(tenant);
        return ResponseEntity.ok(predefinedSearches);
    }

}