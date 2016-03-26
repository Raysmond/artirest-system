package com.raysmond.artirest.web.rest;

import com.raysmond.artirest.Application;
import com.raysmond.artirest.domain.Artifact;
import com.raysmond.artirest.repository.ArtifactRepository;
import com.raysmond.artirest.service.ArtifactService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ArtifactResource REST controller.
 *
 * @see ArtifactResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
//@IntegrationTest
//public class ArtifactResourceIntTest {
//
//    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));
//
//    private static final String DEFAULT_NAME = "AAAAA";
//    private static final String UPDATED_NAME = "BBBBB";
//    private static final String DEFAULT_CURRENT_STATE = "AAAAA";
//    private static final String UPDATED_CURRENT_STATE = "BBBBB";
//
//    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
//    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//    private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.format(DEFAULT_CREATED_AT);
//
//    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
//    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//    private static final String DEFAULT_UPDATED_AT_STR = dateTimeFormatter.format(DEFAULT_UPDATED_AT);
//
//    @Inject
//    private ArtifactRepository artifactRepository;
//
//    @Inject
//    private ArtifactService artifactService;
//
//    @Inject
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
//
//    @Inject
//    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
//
//    private MockMvc restArtifactMockMvc;
//
//    private Artifact artifact;
//
//    @PostConstruct
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        ArtifactResource artifactResource = new ArtifactResource();
//        ReflectionTestUtils.setField(artifactResource, "artifactService", artifactService);
//        this.restArtifactMockMvc = MockMvcBuilders.standaloneSetup(artifactResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setMessageConverters(jacksonMessageConverter).build();
//    }
//
//    @Before
//    public void initTest() {
//        artifactRepository.deleteAll();
//        artifact = new Artifact();
//        artifact.setName(DEFAULT_NAME);
//        artifact.setCurrentState(DEFAULT_CURRENT_STATE);
//        artifact.setCreatedAt(DEFAULT_CREATED_AT);
//        artifact.setUpdatedAt(DEFAULT_UPDATED_AT);
//    }
//
//    @Test
//    public void createArtifact() throws Exception {
//        int databaseSizeBeforeCreate = artifactRepository.findAll().size();
//
//        // Create the Artifact
//
//        restArtifactMockMvc.perform(post("/api/artifacts")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(artifact)))
//                .andExpect(status().isCreated());
//
//        // Validate the Artifact in the database
//        List<Artifact> artifacts = artifactRepository.findAll();
//        assertThat(artifacts).hasSize(databaseSizeBeforeCreate + 1);
//        Artifact testArtifact = artifacts.get(artifacts.size() - 1);
//        assertThat(testArtifact.getName()).isEqualTo(DEFAULT_NAME);
//        assertThat(testArtifact.getCurrentState()).isEqualTo(DEFAULT_CURRENT_STATE);
//        assertThat(testArtifact.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
//        assertThat(testArtifact.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
//    }
//
//    @Test
//    public void getAllArtifacts() throws Exception {
//        // Initialize the database
//        artifactRepository.save(artifact);
//
//        // Get all the artifacts
//        restArtifactMockMvc.perform(get("/api/artifacts?sort=id,desc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.[*].id").value(hasItem(artifact.getId())))
//                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
//                .andExpect(jsonPath("$.[*].currentState").value(hasItem(DEFAULT_CURRENT_STATE.toString())))
//                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
//                .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT_STR)));
//    }
//
//    @Test
//    public void getArtifact() throws Exception {
//        // Initialize the database
//        artifactRepository.save(artifact);
//
//        // Get the artifact
//        restArtifactMockMvc.perform(get("/api/artifacts/{id}", artifact.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(artifact.getId()))
//            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
//            .andExpect(jsonPath("$.currentState").value(DEFAULT_CURRENT_STATE.toString()))
//            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
//            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT_STR));
//    }
//
//    @Test
//    public void getNonExistingArtifact() throws Exception {
//        // Get the artifact
//        restArtifactMockMvc.perform(get("/api/artifacts/{id}", Long.MAX_VALUE))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void updateArtifact() throws Exception {
//        // Initialize the database
//        artifactRepository.save(artifact);
//
//		int databaseSizeBeforeUpdate = artifactRepository.findAll().size();
//
//        // Update the artifact
//        artifact.setName(UPDATED_NAME);
//        artifact.setCurrentState(UPDATED_CURRENT_STATE);
//        artifact.setCreatedAt(UPDATED_CREATED_AT);
//        artifact.setUpdatedAt(UPDATED_UPDATED_AT);
//
//        restArtifactMockMvc.perform(put("/api/artifacts")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(artifact)))
//                .andExpect(status().isOk());
//
//        // Validate the Artifact in the database
//        List<Artifact> artifacts = artifactRepository.findAll();
//        assertThat(artifacts).hasSize(databaseSizeBeforeUpdate);
//        Artifact testArtifact = artifacts.get(artifacts.size() - 1);
//        assertThat(testArtifact.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testArtifact.getCurrentState()).isEqualTo(UPDATED_CURRENT_STATE);
//        assertThat(testArtifact.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
//        assertThat(testArtifact.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
//    }
//
//    @Test
//    public void deleteArtifact() throws Exception {
//        // Initialize the database
//        artifactRepository.save(artifact);
//
//		int databaseSizeBeforeDelete = artifactRepository.findAll().size();
//
//        // Get the artifact
//        restArtifactMockMvc.perform(delete("/api/artifacts/{id}", artifact.getId())
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Artifact> artifacts = artifactRepository.findAll();
//        assertThat(artifacts).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
