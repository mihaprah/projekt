package com.scm.scm.predefinedSearch.services;

import com.scm.scm.events.services.EventsServices;
import com.scm.scm.predefinedSearch.dao.PredefinedSearchRepository;
import com.scm.scm.predefinedSearch.dto.PredefinedSearchDTO;
import com.scm.scm.predefinedSearch.vao.PredefinedSearch;
import com.scm.scm.support.exceptions.CustomHttpException;
import com.scm.scm.support.exceptions.ExceptionCause;
import com.scm.scm.support.exceptions.ExceptionMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PredefinedSearchServices {

    private static final Logger log = Logger.getLogger(EventsServices.class.toString());
    private PredefinedSearchRepository predefinedSearchRepository;

    private PredefinedSearchDTO convertToDTO(PredefinedSearch predefinedSearch) {
        return PredefinedSearchDTO.builder()
                .id(predefinedSearch.getId())
                .searchQuery(predefinedSearch.getSearchQuery())
                .user(predefinedSearch.getUser())
                .onTenant(predefinedSearch.getOnTenant())
                .title(predefinedSearch.getTitle())
                .filter(predefinedSearch.getFilter())
                .sortOrientation(predefinedSearch.getSortOrientation())
                .build();
    }

    private PredefinedSearch convertToEntity(PredefinedSearchDTO predefinedSearchDTO) {
        return PredefinedSearch.builder()
                .id(predefinedSearchDTO.getId())
                .searchQuery(predefinedSearchDTO.getSearchQuery())
                .user(predefinedSearchDTO.getUser())
                .onTenant(predefinedSearchDTO.getOnTenant())
                .title(predefinedSearchDTO.getTitle())
                .filter(predefinedSearchDTO.getFilter())
                .sortOrientation(predefinedSearchDTO.getSortOrientation())
                .build();
    }

    public PredefinedSearchDTO addPredefinedSearch(PredefinedSearchDTO predefinedSearchDTO) {
        PredefinedSearch predefinedSearch = convertToEntity(predefinedSearchDTO);
        if (Objects.equals(predefinedSearch.getTitle(), "")) {
            throw new CustomHttpException("PredefinedSearch title is empty", 400, ExceptionCause.USER_ERROR);
        } else {
            predefinedSearch.setId(predefinedSearch.generateId(predefinedSearch.getTitle()));
            predefinedSearchRepository.save(predefinedSearch);
            log.info("PredefinedSearch created with id: " + predefinedSearch.getId());
            return convertToDTO(predefinedSearch);
        }
    }

    public List<PredefinedSearchDTO> getAllPredefinedSearches() {
        List<PredefinedSearch> predefinedSearches = predefinedSearchRepository.findAll();
        log.info("All predefined searches found");
        return predefinedSearches.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public PredefinedSearchDTO getPredefinedSearchById(String id) {
        PredefinedSearch predefinedSearch = predefinedSearchRepository.findById(id).orElseThrow(() -> new CustomHttpException(ExceptionMessage.SEARCH_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR));
        log.info("PredefinedSearch found with id: " + id);
        return convertToDTO(predefinedSearch);
    }

    public PredefinedSearchDTO updatePredefinedSearch(PredefinedSearchDTO predefinedSearchDTO) {
        PredefinedSearch predefinedSearch = convertToEntity(predefinedSearchDTO);
        PredefinedSearch oldPredefinedSearch = predefinedSearchRepository.findById(predefinedSearch.getId()).orElseThrow(() -> new CustomHttpException(ExceptionMessage.SEARCH_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR));
        if (oldPredefinedSearch != null) {
            oldPredefinedSearch.setSearchQuery(predefinedSearch.getSearchQuery());
            oldPredefinedSearch.setUser(predefinedSearch.getUser());
            oldPredefinedSearch.setOnTenant(predefinedSearch.getOnTenant());
            oldPredefinedSearch.setTitle(predefinedSearch.getTitle());
            oldPredefinedSearch.setFilter(predefinedSearch.getFilter());
            predefinedSearchRepository.save(oldPredefinedSearch);
        } else {
            throw new CustomHttpException(ExceptionMessage.SEARCH_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR);
        }
        log.info("PredefinedSearch updated with id: " + predefinedSearch.getId());
        return convertToDTO(oldPredefinedSearch);
    }

    public String deletePredefinedSearch(String id) {
        if (!id.isEmpty() && predefinedSearchRepository.existsById(id)) {
            predefinedSearchRepository.deleteById(id);
            log.info("PredefinedSearch deleted with id: " + id);
            return "PredefinedSearch successfully deleted";
        } else {
            throw new CustomHttpException(ExceptionMessage.SEARCH_NOT_FOUND.getExceptionMessage(), 404, ExceptionCause.USER_ERROR);
        }
    }

    public List<PredefinedSearchDTO> getPredefinedSearchByUser(String user) {
        if (user == null || user.isEmpty()) {
            throw new CustomHttpException("User is empty", 400, ExceptionCause.USER_ERROR);
        } else {
            log.info("PredefinedSearch found by user: " + user);
            List<PredefinedSearch> predefinedSearches = predefinedSearchRepository.findByUser(user);
            return predefinedSearches.stream().map(this::convertToDTO).collect(Collectors.toList());
        }
    }

    public List<PredefinedSearchDTO> getPredefinedSearchByTenant(String tenant) {
        if (tenant == null || tenant.isEmpty()) {
            throw new CustomHttpException("Tenant is empty", 400, ExceptionCause.USER_ERROR);
        } else {
            log.info("PredefinedSearch found by tenant: " + tenant);
            List<PredefinedSearch> predefinedSearches = predefinedSearchRepository.findByOnTenant(tenant);
            return predefinedSearches.stream().map(this::convertToDTO).collect(Collectors.toList());
        }
    }

}