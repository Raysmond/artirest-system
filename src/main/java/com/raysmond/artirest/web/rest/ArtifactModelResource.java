package com.raysmond.artirest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.raysmond.artirest.domain.ArtifactModel;
import com.raysmond.artirest.service.ArtifactModelService;
import com.raysmond.artirest.web.rest.util.HeaderUtil;
import com.raysmond.artirest.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ArtifactModel.
 */
@RestController
@RequestMapping("/api")
public class ArtifactModelResource {

    private final Logger log = LoggerFactory.getLogger(ArtifactModelResource.class);
        
    @Inject
    private ArtifactModelService artifactModelService;
    
    /**
     * POST  /artifactModels -> Create a new artifactModel.
     */
    @RequestMapping(value = "/artifactModels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArtifactModel> createArtifactModel(@RequestBody ArtifactModel artifactModel) throws URISyntaxException {
        log.debug("REST request to save ArtifactModel : {}", artifactModel);
        if (artifactModel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("artifactModel", "idexists", "A new artifactModel cannot already have an ID")).body(null);
        }
        ArtifactModel result = artifactModelService.save(artifactModel);
        return ResponseEntity.created(new URI("/api/artifactModels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("artifactModel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /artifactModels -> Updates an existing artifactModel.
     */
    @RequestMapping(value = "/artifactModels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArtifactModel> updateArtifactModel(@RequestBody ArtifactModel artifactModel) throws URISyntaxException {
        log.debug("REST request to update ArtifactModel : {}", artifactModel);
        if (artifactModel.getId() == null) {
            return createArtifactModel(artifactModel);
        }
        ArtifactModel result = artifactModelService.save(artifactModel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("artifactModel", artifactModel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artifactModels -> get all the artifactModels.
     */
    @RequestMapping(value = "/artifactModels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ArtifactModel>> getAllArtifactModels(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ArtifactModels");
        Page<ArtifactModel> page = artifactModelService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/artifactModels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /artifactModels/:id -> get the "id" artifactModel.
     */
    @RequestMapping(value = "/artifactModels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArtifactModel> getArtifactModel(@PathVariable String id) {
        log.debug("REST request to get ArtifactModel : {}", id);
        ArtifactModel artifactModel = artifactModelService.findOne(id);
        return Optional.ofNullable(artifactModel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /artifactModels/:id -> delete the "id" artifactModel.
     */
    @RequestMapping(value = "/artifactModels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteArtifactModel(@PathVariable String id) {
        log.debug("REST request to delete ArtifactModel : {}", id);
        artifactModelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("artifactModel", id.toString())).build();
    }
}
