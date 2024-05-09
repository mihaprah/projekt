package com.scm.scm.predefinedSearch.services;

import com.scm.scm.predefinedSearch.dao.PredefinedSearchRepository;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
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
            throw new RuntimeException("Predefined search already exists");
        } else {
            if (Objects.equals(predefinedSearch.getTitle(), "")) {
                throw new RuntimeException("Predefined search title is empty");
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
        return predefinedSearchRepository.findById(id).orElseThrow(() -> new RuntimeException("PredefinedSearch not found"));
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
            throw new RuntimeException("PredefinedSearch not found");
        }
        return oldPredefinedSearch;
    }

    public String deletePredefinedSearch(String id) {
        predefinedSearchRepository.deleteById(id);
        return "PredefinedSearch successfully deleted";
    }

    public List<PredefinedSearch> getPredefinedSearchByUser(String user) {
        return predefinedSearchRepository.findByUser(user);
    }

    public List<PredefinedSearch> getPredefinedSearchByTenant(String tenant) {
        return predefinedSearchRepository.findByOnTenant(tenant);
    }

}
