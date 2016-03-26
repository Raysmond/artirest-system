package com.raysmond.artirest.repository;

import com.raysmond.artirest.domain.ArtifactModel;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ArtifactModel entity.
 */
public interface ArtifactModelRepository extends MongoRepository<ArtifactModel,String> {

}
