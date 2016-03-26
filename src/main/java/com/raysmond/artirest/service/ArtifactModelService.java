package com.raysmond.artirest.service;

import com.raysmond.artirest.domain.ArtifactModel;
import com.raysmond.artirest.repository.ArtifactModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing ArtifactModel.
 */
@Service
public class ArtifactModelService {

    private final Logger log = LoggerFactory.getLogger(ArtifactModelService.class);
    
    @Inject
    private ArtifactModelRepository artifactModelRepository;
    
    /**
     * Save a artifactModel.
     * @return the persisted entity
     */
    public ArtifactModel save(ArtifactModel artifactModel) {
        log.debug("Request to save ArtifactModel : {}", artifactModel);
        ArtifactModel result = artifactModelRepository.save(artifactModel);
        return result;
    }

    /**
     *  get all the artifactModels.
     *  @return the list of entities
     */
    public Page<ArtifactModel> findAll(Pageable pageable) {
        log.debug("Request to get all ArtifactModels");
        Page<ArtifactModel> result = artifactModelRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one artifactModel by id.
     *  @return the entity
     */
    public ArtifactModel findOne(String id) {
        log.debug("Request to get ArtifactModel : {}", id);
        ArtifactModel artifactModel = artifactModelRepository.findOne(id);
        return artifactModel;
    }

    /**
     *  delete the  artifactModel by id.
     */
    public void delete(String id) {
        log.debug("Request to delete ArtifactModel : {}", id);
        artifactModelRepository.delete(id);
    }
}
