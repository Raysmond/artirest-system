package com.raysmond.artirest.repository;

import com.raysmond.artirest.domain.Process;
import com.raysmond.artirest.domain.ProcessModel;
import com.raysmond.artirest.service.ProcessService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Process entity.
 */
public interface ProcessRepository extends MongoRepository<Process, String> {

    Page<Process> findByProcessModel(ProcessModel processModel, Pageable pageable);

    @Cacheable(value = ProcessService.CACHE_NAME)
    Process findOne(String id);
}
