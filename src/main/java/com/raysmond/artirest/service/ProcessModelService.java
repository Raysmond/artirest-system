package com.raysmond.artirest.service;

import com.raysmond.artirest.domain.ProcessModel;
import com.raysmond.artirest.repository.ProcessModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing ProcessModel.
 */
@Service
public class ProcessModelService {

    private final Logger log = LoggerFactory.getLogger(ProcessModelService.class);

    @Autowired
    private ProcessModelRepository processModelRepository;

    public static final String CACHE_NAME = "artirest.process_model";

    /**
     * Save a processModel.
     * @return the persisted entity
     */
    public ProcessModel save(ProcessModel processModel) {
        log.debug("Request to save ProcessModel : {}", processModel);
        ProcessModel result = processModelRepository.save(processModel);
        return result;
    }

    /**
     *  get all the processModels.
     *  @return the list of entities
     */
    public Page<ProcessModel> findAll(Pageable pageable) {
        log.debug("Request to get all ProcessModels");
        Page<ProcessModel> result = processModelRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one processModel by id.
     *  @return the entity
     */
    @Cacheable(value = CACHE_NAME)
    public ProcessModel findOne(String id) {
        log.debug("----- Request to get ProcessModel : {}", id);
        ProcessModel processModel = processModelRepository.findOne(id);
        return processModel;
    }

    /**
     *  delete the  processModel by id.
     */
    public void delete(String id) {
        log.debug("Request to delete ProcessModel : {}", id);
        processModelRepository.delete(id);
    }
}
