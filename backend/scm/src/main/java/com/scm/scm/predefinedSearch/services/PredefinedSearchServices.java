package com.scm.scm.predefinedSearch.services;

import com.scm.scm.events.services.EventsServices;
import com.scm.scm.predefinedSearch.dao.PredefinedSearchRepository;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class PredefinedSearchServices {

    private static final Logger log = Logger.getLogger(EventsServices.class.toString());
    private PredefinedSearchRepository predefinedSearchRepository;

    public PredefinedSearch addPredefinedSearch(PredefinedSearch predefinedSearch) {
        if (predefinedSearchRepository.existsById(predefinedSearch.getSearchBy())){
            throw new CustomHttpException("PredefinedSearch with this id already exists", 400, ExceptionCause.USER_ERROR);
        } else {
            if (Objects.equals(predefinedSearch.getTitle(), "")) {
                throw new CustomHttpException("PredefinedSearch title is empty", 400, ExceptionCause.USER_ERROR);
            } else {
                predefinedSearch.setId(predefinedSearch.generateId(predefinedSearch.getTitle()));
                predefinedSearchRepository.save(predefinedSearch);
                log.info("PredefinedSearch created with id: " + predefinedSearch.getId());
                return predefinedSearch;
            }
        }
    }

    public List<PredefinedSearch> getAllPredefinedSearches() {
        log.info("All predefined searches found");
        return predefinedSearchRepository.findAll();
    }

    public PredefinedSearch getPredefinedSearchById(String id) {
        PredefinedSearch predefinedSearch = predefinedSearchRepository.findById(id).orElseThrow(() -> new CustomHttpException("PredefinedSearch not found", 404, ExceptionCause.USER_ERROR));
        log.info("PredefinedSearch found with id: " + id);
        return predefinedSearch;
    }

    public PredefinedSearch updatePredefinedSearch(PredefinedSearch predefinedSearch) {
        PredefinedSearch oldPredefinedSearch = predefinedSearchRepository.findById(predefinedSearch.getId()).orElse(null);
        if (oldPredefinedSearch != null) {
            oldPredefinedSearch.setSearchQuery(predefinedSearch.getSearchQuery());
            oldPredefinedSearch.setSearchBy(predefinedSearch.getSearchBy());
            oldPredefinedSearch.setUser(predefinedSearch.getUser());
            oldPredefinedSearch.setOnTenant(predefinedSearch.getOnTenant());
            oldPredefinedSearch.setTitle(predefinedSearch.getTitle());
            oldPredefinedSearch.setFilter(predefinedSearch.getFilter());
            predefinedSearchRepository.save(oldPredefinedSearch);
        } else {
            throw new CustomHttpException("PredefinedSearch not found", 404, ExceptionCause.USER_ERROR);
        }
        log.info("PredefinedSearch updated with id: " + predefinedSearch.getId());
        return oldPredefinedSearch;
    }

    public String deletePredefinedSearch(String id) {
        if (predefinedSearchRepository.existsById(id) && !id.isEmpty()) {
            predefinedSearchRepository.deleteById(id);
            log.info("PredefinedSearch deleted with id: " + id);
            return "PredefinedSearch successfully deleted";
        } else {
            throw new CustomHttpException("PredefinedSearch not found", 404, ExceptionCause.USER_ERROR);
        }
    }

    public List<PredefinedSearch> getPredefinedSearchByUser(String user) {
        if (!user.isEmpty()){
            log.info("PredefinedSearch found by user: " + user);
            return predefinedSearchRepository.findByUser(user);
        } else {
            throw new CustomHttpException("PredefinedSearch user is empty", 400, ExceptionCause.USER_ERROR);
        }
    }

    public List<PredefinedSearch> getPredefinedSearchByTenant(String tenant) {
        if (!tenant.isEmpty()){
            log.info("PredefinedSearch found by tenant: " + tenant);
            return predefinedSearchRepository.findByOnTenant(tenant);
        } else {
            throw new CustomHttpException("PredefinedSearch tenant is empty", 400, ExceptionCause.USER_ERROR);
        }
    }

}
