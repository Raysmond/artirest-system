package com.raysmond.artirest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.raysmond.artirest.domain.ArtifactModel;
import com.raysmond.artirest.domain.BusinessRuleModel;
import com.raysmond.artirest.domain.ProcessModel;
import com.raysmond.artirest.domain.ServiceModel;
import com.raysmond.artirest.service.ArtifactModelService;
import com.raysmond.artirest.service.ProcessCreateService;
import com.raysmond.artirest.service.ProcessModelService;
import com.raysmond.artirest.web.rest.util.HeaderUtil;
import com.raysmond.artirest.web.rest.util.PaginationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing ProcessModel.
 */
@RestController
@RequestMapping("/api")
public class ProcessModelResource {

    private final Logger log = LoggerFactory.getLogger(ProcessModelResource.class);

    @Autowired
    private ProcessModelService processModelService;

    @Autowired
    private ProcessCreateService processCreateService;

    @Autowired
    private ArtifactModelService artifactModelService;

    /**
     * POST  /processModels -> Create a new processModel.
     */
    @RequestMapping(value = "/processModels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessModel> createProcessModel(@RequestBody ProcessModel processModel) throws URISyntaxException {
        log.debug("REST request to save ProcessModel : {}", processModel);
        if (processModel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("processModel", "idexists", "A new processModel cannot already have an ID")).body(null);
        }
        ArtifactModel artifact = new ArtifactModel();
        artifact.setName("Artifact");
        artifact = artifactModelService.save(artifact);
        processModel.artifacts.add(artifact);
        ProcessModel result = processModelService.save(processModel);
        return ResponseEntity.created(new URI("/api/processModels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("processModel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /processModels -> Updates an existing processModel.
     */
    @RequestMapping(value = "/processModels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessModel> updateProcessModel(@RequestBody ProcessModel processModel) throws URISyntaxException {
        log.debug("REST request to update ProcessModel : {}", processModel);
        if (processModel.getId() == null) {
            return createProcessModel(processModel);
        }
        ProcessModel result = processModelService.save(processModel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("processModel", processModel.getId().toString()))
            .body(result);
    }

    @RequestMapping(value = "/processModels/{id}/services",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ProcessModel> saveProcessServices(@RequestBody Set<ServiceModel> serviceModels, @PathVariable String id) throws URISyntaxException {
        ProcessModel processModel = processModelService.findOne(id);
        processModel.services = serviceModels;
        ProcessModel result = processModelService.save(processModel);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("processModel", processModel.getId().toString()))
            .body(result);
    }

    @RequestMapping(value = "/processModels/{id}/businessRules",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<ProcessModel> saveBusinessRules(@RequestBody Set<BusinessRuleModel> businessRuleModels, @PathVariable String id) throws URISyntaxException {
        ProcessModel processModel = processModelService.findOne(id);
        processModel.businessRules = businessRuleModels;
        ProcessModel result = processModelService.save(processModel);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("processModel", processModel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /processModels -> get all the processModels.
     */
    @RequestMapping(value = "/processModels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcessModel>> getAllProcessModels(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ProcessModels");
        Page<ProcessModel> page = processModelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/processModels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /processModels/:id -> get the "id" processModel.
     */
    @RequestMapping(value = "/processModels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessModel> getProcessModel(@PathVariable String id) {
        log.debug("REST request to get ProcessModel : {}", id);
        ProcessModel processModel = processModelService.findOne(id);
        return Optional.ofNullable(processModel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/processModels/create_test_models",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProcessModel> createTestModel(@RequestParam(required = false) String model) {
        log.debug("REST request to create test process model : {}", model);

        ProcessModel processModel = null;

        if (model != null && model.equals("loan")) {
            processModel = processCreateService.createLoanProcessModel();
        } else {
            processModel = processCreateService.createOrderProcessModel();
        }

        return Optional.ofNullable(processModel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /processModels/:id -> delete the "id" processModel.
     */
    @RequestMapping(value = "/processModels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteProcessModel(@PathVariable String id) {
        log.debug("REST request to delete ProcessModel : {}", id);
        processModelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("processModel", id.toString())).build();
    }
}
