//package com.raysmond.artirest.web.rest;
//
//import com.raysmond.artirest.Application;
//import com.raysmond.artirest.domain.ArtifactModel;
//import com.raysmond.artirest.repository.ArtifactModelRepository;
//import com.raysmond.artirest.service.ArtifactModelService;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import static org.hamcrest.Matchers.hasItem;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.IntegrationTest;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import javax.annotation.PostConstruct;
//import javax.inject.Inject;
//import java.time.Instant;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.ZoneId;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
///**
// * Test class for the ArtifactModelResource REST controller.
// *
// * @see ArtifactModelResource
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
//@IntegrationTest
//public class ArtifactModelResourceIntTest {
//
//    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));
//
//    private static final String DEFAULT_NAME = "AAAAA";
//    private static final String UPDATED_NAME = "BBBBB";
//    private static final String DEFAULT_COMMENT = "AAAAA";
//    private static final String UPDATED_COMMENT = "BBBBB";
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
//    private ArtifactModelRepository artifactModelRepository;
//
//    @Inject
//    private ArtifactModelService artifactModelService;
//
//    @Inject
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
//
//    @Inject
//    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
//
//    private MockMvc restArtifactModelMockMvc;
//
//    private ArtifactModel artifactModel;
//
//    @PostConstruct
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        ArtifactModelResource artifactModelResource = new ArtifactModelResource();
//        ReflectionTestUtils.setField(artifactModelResource, "artifactModelService", artifactModelService);
//        this.restArtifactModelMockMvc = MockMvcBuilders.standaloneSetup(artifactModelResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setMessageConverters(jacksonMessageConverter).build();
//    }
//
//    @Before
//    public void initTest() {
//        artifactModelRepository.deleteAll();
//        artifactModel = new ArtifactModel();
//        artifactModel.setName(DEFAULT_NAME);
//        artifactModel.setComment(DEFAULT_COMMENT);
//        artifactModel.setCreatedAt(DEFAULT_CREATED_AT);
//        artifactModel.setUpdatedAt(DEFAULT_UPDATED_AT);
//    }
//
//    @Test
//    public void createArtifactModel() throws Exception {
//        int databaseSizeBeforeCreate = artifactModelRepository.findAll().size();
//
//        // Create the ArtifactModel
//
//        restArtifactModelMockMvc.perform(post("/api/artifactModels")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(artifactModel)))
//                .andExpect(status().isCreated());
//
//        // Validate the ArtifactModel in the database
//        List<ArtifactModel> artifactModels = artifactModelRepository.findAll();
//        assertThat(artifactModels).hasSize(databaseSizeBeforeCreate + 1);
//        ArtifactModel testArtifactModel = artifactModels.get(artifactModels.size() - 1);
//        assertThat(testArtifactModel.getName()).isEqualTo(DEFAULT_NAME);
//        assertThat(testArtifactModel.getComment()).isEqualTo(DEFAULT_COMMENT);
//        assertThat(testArtifactModel.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
//        assertThat(testArtifactModel.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
//    }
//
//    @Test
//    public void getAllArtifactModels() throws Exception {
//        // Initialize the database
//        artifactModelRepository.save(artifactModel);
//
//        // Get all the artifactModels
//        restArtifactModelMockMvc.perform(get("/api/artifactModels?sort=id,desc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.[*].id").value(hasItem(artifactModel.getId())))
//                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
//                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
//                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
//                .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT_STR)));
//    }
//
//    @Test
//    public void getArtifactModel() throws Exception {
//        // Initialize the database
//        artifactModelRepository.save(artifactModel);
//
//        // Get the artifactModel
//        restArtifactModelMockMvc.perform(get("/api/artifactModels/{id}", artifactModel.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(artifactModel.getId()))
//            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
//            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
//            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
//            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT_STR));
//    }
//
//    @Test
//    public void getNonExistingArtifactModel() throws Exception {
//        // Get the artifactModel
//        restArtifactModelMockMvc.perform(get("/api/artifactModels/{id}", Long.MAX_VALUE))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void updateArtifactModel() throws Exception {
//        // Initialize the database
//        artifactModelRepository.save(artifactModel);
//
//		int databaseSizeBeforeUpdate = artifactModelRepository.findAll().size();
//
//        // Update the artifactModel
//        artifactModel.setName(UPDATED_NAME);
//        artifactModel.setComment(UPDATED_COMMENT);
//        artifactModel.setCreatedAt(UPDATED_CREATED_AT);
//        artifactModel.setUpdatedAt(UPDATED_UPDATED_AT);
//
//        restArtifactModelMockMvc.perform(put("/api/artifactModels")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(artifactModel)))
//                .andExpect(status().isOk());
//
//        // Validate the ArtifactModel in the database
//        List<ArtifactModel> artifactModels = artifactModelRepository.findAll();
//        assertThat(artifactModels).hasSize(databaseSizeBeforeUpdate);
//        ArtifactModel testArtifactModel = artifactModels.get(artifactModels.size() - 1);
//        assertThat(testArtifactModel.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testArtifactModel.getComment()).isEqualTo(UPDATED_COMMENT);
//        assertThat(testArtifactModel.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
//        assertThat(testArtifactModel.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
//    }
//
//    @Test
//    public void deleteArtifactModel() throws Exception {
//        // Initialize the database
//        artifactModelRepository.save(artifactModel);
//
//		int databaseSizeBeforeDelete = artifactModelRepository.findAll().size();
//
//        // Get the artifactModel
//        restArtifactModelMockMvc.perform(delete("/api/artifactModels/{id}", artifactModel.getId())
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<ArtifactModel> artifactModels = artifactModelRepository.findAll();
//        assertThat(artifactModels).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
