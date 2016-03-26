package com.raysmond.artirest.repository;

import com.raysmond.artirest.domain.Artifact;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Artifact entity.
 */
public interface ArtifactRepository extends MongoRepository<Artifact,String> {

}
