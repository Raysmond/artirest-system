package com.raysmond.artirest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.raysmond.artirest.domain.Artifact;
import com.raysmond.artirest.domain.Process;
import com.raysmond.artirest.domain.ProcessModel;
import com.raysmond.artirest.domain.ServiceModel;
import com.raysmond.artirest.service.ArtifactService;
import com.raysmond.artirest.service.ProcessModelService;
import com.raysmond.artirest.service.ProcessService;
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
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Process.
 */
@RestController
@RequestMapping("/api")
public class ProcessResource {

    private final Logger log = LoggerFactory.getLogger(ProcessResource.class);

    @Inject
    private ProcessService processService;

    @Inject
    private ProcessModelService processModelService;

    @Inject
    private ArtifactService artifactService;

    @RequestMapping(value = "/processModels/{id}/processes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Process>> getAllProcesssOfModel(@PathVariable(value = "id") String processModelId, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Processes");

        Page<Process> page = processService.findInstances(processModelId, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/processes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/processModels/{id}/processes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Process> createProcessFromModel(
        @PathVariable(value = "id") String processModelId,
        @RequestBody Process process) throws URISyntaxException {

        log.debug("REST request to save Process : {}", process);
        if (process.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("process", "idexists", "A new process cannot already have an ID")).body(null);
        }

        ProcessModel processModel = processModelService.findOne(processModelId);
        Process result = processService.createProcessInstance(processModel);

        return ResponseEntity.created(new URI("/api/processes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("process", result.getId().toString()))
            .body(result);
    }

    @RequestMapping(
        value = "/processes/{processId}/services/{service}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public ResponseEntity<Process> invokeService(
        @PathVariable String processId,
        @PathVariable String service,
        @RequestBody Artifact artifact) throws Exception {
        log.debug("REST request to invoke a service {} of process {}", service, processId);

        Process process = processService.findOne(processId);

        if (process == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Process result = processService.invokeService(service, process, artifact);
        processService.afterInvokingService(result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * POST  /processes -> Create a new process.
     */
    @RequestMapping(value = "/processes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Process> createProcess(@RequestBody Process process) throws URISyntaxException {
        log.info("REST request to save Process : {}", process);
        if (process.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("process", "idexists", "A new process cannot already have an ID")).body(null);
        }
        Process result = processService.save(process);
        return ResponseEntity.created(new URI("/api/processes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("process", result.getId().toString()))
            .body(result);
    }


    /**
     * PUT  /processes -> Updates an existing process.
     */
    @RequestMapping(value = "/processes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Process> updateProcess(@RequestBody Process process) throws URISyntaxException {
        log.debug("REST request to update Process : {}", process);
        if (process.getId() == null) {
            return createProcess(process);
        }
        Process result = processService.save(process);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("process", process.getId().toString()))
            .body(result);
    }

    /**
     * GET  /processes -> get all the processs.
     */
    @RequestMapping(value = "/processes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Process>> getAllProcesss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Processs");
        Page<Process> page = processService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/processes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /processes/:id -> get the "id" process.
     */
    @RequestMapping(value = "/processes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Process> getProcess(@PathVariable String id) {
        log.debug("REST request to get Process : {}", id);

        Process process = processService.findOne(id);
        return Optional.ofNullable(process)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/processes/{id}/available_services",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ServiceModel>> getAvailableServices(@PathVariable String id) {
        log.debug("REST request to get available services of process : {}", id);

        Process process = processService.findOne(id);

        return Optional.ofNullable(process)
            .map(result -> new ResponseEntity<>(
                processService.availableServices(process),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /processes/:id -> delete the "id" process.
     */
    @RequestMapping(value = "/processes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProcess(@PathVariable String id) {
        log.debug("REST request to delete Process : {}", id);
        processService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("process", id.toString())).build();
    }
}
