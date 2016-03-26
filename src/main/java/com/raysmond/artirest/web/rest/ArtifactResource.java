package com.raysmond.artirest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.raysmond.artirest.domain.Artifact;
import com.raysmond.artirest.service.ArtifactService;
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
 * REST controller for managing Artifact.
 */
@RestController
@RequestMapping("/api")
public class ArtifactResource {

    private final Logger log = LoggerFactory.getLogger(ArtifactResource.class);
        
    @Inject
    private ArtifactService artifactService;
    
    /**
     * POST  /artifacts -> Create a new artifact.
     */
    @RequestMapping(value = "/artifacts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Artifact> createArtifact(@RequestBody Artifact artifact) throws URISyntaxException {
        log.debug("REST request to save Artifact : {}", artifact);
        if (artifact.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("artifact", "idexists", "A new artifact cannot already have an ID")).body(null);
        }
        Artifact result = artifactService.save(artifact);
        return ResponseEntity.created(new URI("/api/artifacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("artifact", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /artifacts -> Updates an existing artifact.
     */
    @RequestMapping(value = "/artifacts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Artifact> updateArtifact(@RequestBody Artifact artifact) throws URISyntaxException {
        log.debug("REST request to update Artifact : {}", artifact);
        if (artifact.getId() == null) {
            return createArtifact(artifact);
        }
        Artifact result = artifactService.save(artifact);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("artifact", artifact.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artifacts -> get all the artifacts.
     */
    @RequestMapping(value = "/artifacts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Artifact>> getAllArtifacts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Artifacts");
        Page<Artifact> page = artifactService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/artifacts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /artifacts/:id -> get the "id" artifact.
     */
    @RequestMapping(value = "/artifacts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Artifact> getArtifact(@PathVariable String id) {
        log.debug("REST request to get Artifact : {}", id);
        Artifact artifact = artifactService.findOne(id);
        return Optional.ofNullable(artifact)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /artifacts/:id -> delete the "id" artifact.
     */
    @RequestMapping(value = "/artifacts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteArtifact(@PathVariable String id) {
        log.debug("REST request to delete Artifact : {}", id);
        artifactService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("artifact", id.toString())).build();
    }
}
