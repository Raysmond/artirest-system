package com.raysmond.artirest.repository;

import com.raysmond.artirest.domain.ProcessModel;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ProcessModel entity.
 */
public interface ProcessModelRepository extends MongoRepository<ProcessModel,String> {

}
