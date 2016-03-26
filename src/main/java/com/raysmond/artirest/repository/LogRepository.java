package com.raysmond.artirest.repository;

import com.raysmond.artirest.domain.Log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Log entity.
 */
public interface LogRepository extends MongoRepository<Log, String> {

    Page<Log> findByProcessId(String processId, Pageable pageable);
}
