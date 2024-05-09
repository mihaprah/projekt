package com.scm.scm.predefinedSearch.services;

import com.scm.scm.predefinedSearch.dao.PredefinedSearchRepository;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PredefinedSearchServices {

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
                return predefinedSearch;
            }
        }
    }

    public List<PredefinedSearch> getAllPredefinedSearches() {
        return predefinedSearchRepository.findAll();
    }

    public PredefinedSearch getPredefinedSearchById(String id) {
        return predefinedSearchRepository.findById(id).orElseThrow(() -> new CustomHttpException("PredefinedSearch not found", 404, ExceptionCause.USER_ERROR));
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
        return oldPredefinedSearch;
    }

    public String deletePredefinedSearch(String id) {
        if (predefinedSearchRepository.existsById(id) && !id.isEmpty()) {
            predefinedSearchRepository.deleteById(id);
            return "PredefinedSearch successfully deleted";
        } else {
            throw new CustomHttpException("PredefinedSearch not found", 404, ExceptionCause.USER_ERROR);
        }
    }

    public List<PredefinedSearch> getPredefinedSearchByUser(String user) {
        if (!user.isEmpty()){
            return predefinedSearchRepository.findByUser(user);
        } else {
            throw new CustomHttpException("PredefinedSearch user is empty", 400, ExceptionCause.USER_ERROR);
        }
    }

    public List<PredefinedSearch> getPredefinedSearchByTenant(String tenant) {
        if (!tenant.isEmpty()){
            return predefinedSearchRepository.findByOnTenant(tenant);
        } else {
            throw new CustomHttpException("PredefinedSearch tenant is empty", 400, ExceptionCause.USER_ERROR);
        }
    }

}
