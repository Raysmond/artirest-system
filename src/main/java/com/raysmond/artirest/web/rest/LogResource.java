package com.raysmond.artirest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.raysmond.artirest.domain.Log;
import com.raysmond.artirest.repository.LogRepository;
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
 * REST controller for managing Log.
 */
@RestController
@RequestMapping("/api")
public class LogResource {

    private final Logger logger = LoggerFactory.getLogger(LogResource.class);

    @Inject
    private LogRepository logRepository;

    /**
     * POST  /processLogs -> Create a new log.
     */
    @RequestMapping(value = "/processLogs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Log> createLog(@RequestBody Log log) throws URISyntaxException {
        logger.debug("REST request to save Log : {}", log);
        if (log.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("log", "idexists", "A new log cannot already have an ID")).body(null);
        }
        Log result = logRepository.save(log);
        return ResponseEntity.created(new URI("/api/processLogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("log", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /processLogs -> Updates an existing log.
     */
    @RequestMapping(value = "/processLogs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Log> updateLog(@RequestBody Log log) throws URISyntaxException {
        logger.debug("REST request to update Log : {}", log);
        if (log.getId() == null) {
            return createLog(log);
        }
        Log result = logRepository.save(log);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("log", log.getId().toString()))
            .body(result);
    }

    /**
     * GET  /processLogs -> get all the logs.
     */
    @RequestMapping(value = "/processLogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Log>> getAllLogs(Pageable pageable)
        throws URISyntaxException {
        logger.debug("REST request to get a page of Logs");
        Page<Log> page = logRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/processLogs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/processes/{id}/processLogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Log>> getAllLogsOfProcess(@PathVariable(value = "id") String processId, Pageable pageable)
        throws URISyntaxException {
        logger.debug("REST request to get a page of process logs {}", processId);

        Page<Log> page = logRepository.findByProcessId(processId, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/processLogs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /processLogs/:id -> get the "id" log.
     */
    @RequestMapping(value = "/processLogs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Log> getLog(@PathVariable String id) {
        logger.debug("REST request to get Log : {}", id);
        Log log = logRepository.findOne(id);
        return Optional.ofNullable(log)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /processLogs/:id -> delete the "id" log.
     */
    @RequestMapping(value = "/processLogs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLog(@PathVariable String id) {
        logger.debug("REST request to delete Log : {}", id);
        logRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("log", id.toString())).build();
    }
}
