//package com.raysmond.artirest.web.rest;
//
//import com.raysmond.artirest.Application;
//import com.raysmond.artirest.domain.ProcessModel;
//import com.raysmond.artirest.repository.ProcessModelRepository;
//import com.raysmond.artirest.service.ProcessModelService;
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
//import com.raysmond.artirest.domain.enumeration.Status;
//
///**
// * Test class for the ProcessModelResource REST controller.
// *
// * @see ProcessModelResource
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
//@IntegrationTest
//public class ProcessModelResourceIntTest {
//
//    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));
//
//    private static final String DEFAULT_NAME = "AAAAA";
//    private static final String UPDATED_NAME = "BBBBB";
//    private static final String DEFAULT_COMMENT = "AAAAA";
//    private static final String UPDATED_COMMENT = "BBBBB";
//
//
//    private static final Status DEFAULT_STATUS = Status.DESIGNING;
//    private static final Status UPDATED_STATUS = Status.ENACTED;
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
//    private ProcessModelRepository processModelRepository;
//
//    @Inject
//    private ProcessModelService processModelService;
//
//    @Inject
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
//
//    @Inject
//    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
//
//    private MockMvc restProcessModelMockMvc;
//
//    private ProcessModel processModel;
//
//    @PostConstruct
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        ProcessModelResource processModelResource = new ProcessModelResource();
//        ReflectionTestUtils.setField(processModelResource, "processModelService", processModelService);
//        this.restProcessModelMockMvc = MockMvcBuilders.standaloneSetup(processModelResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setMessageConverters(jacksonMessageConverter).build();
//    }
//
//    @Before
//    public void initTest() {
//        processModelRepository.deleteAll();
//        processModel = new ProcessModel();
//        processModel.setName(DEFAULT_NAME);
//        processModel.setComment(DEFAULT_COMMENT);
//        processModel.setStatus(DEFAULT_STATUS);
//        processModel.setCreatedAt(DEFAULT_CREATED_AT);
//        processModel.setUpdatedAt(DEFAULT_UPDATED_AT);
//    }
//
//    @Test
//    public void createProcessModel() throws Exception {
//        int databaseSizeBeforeCreate = processModelRepository.findAll().size();
//
//        // Create the ProcessModel
//
//        restProcessModelMockMvc.perform(post("/api/processModels")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(processModel)))
//                .andExpect(status().isCreated());
//
//        // Validate the ProcessModel in the database
//        List<ProcessModel> processModels = processModelRepository.findAll();
//        assertThat(processModels).hasSize(databaseSizeBeforeCreate + 1);
//        ProcessModel testProcessModel = processModels.get(processModels.size() - 1);
//        assertThat(testProcessModel.getName()).isEqualTo(DEFAULT_NAME);
//        assertThat(testProcessModel.getComment()).isEqualTo(DEFAULT_COMMENT);
//        assertThat(testProcessModel.getStatus()).isEqualTo(DEFAULT_STATUS);
//        assertThat(testProcessModel.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
//        assertThat(testProcessModel.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
//    }
//
//    @Test
//    public void getAllProcessModels() throws Exception {
//        // Initialize the database
//        processModelRepository.save(processModel);
//
//        // Get all the processModels
//        restProcessModelMockMvc.perform(get("/api/processModels?sort=id,desc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.[*].id").value(hasItem(processModel.getId())))
//                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
//                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
//                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
//                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
//                .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT_STR)));
//    }
//
//    @Test
//    public void getProcessModel() throws Exception {
//        // Initialize the database
//        processModelRepository.save(processModel);
//
//        // Get the processModel
//        restProcessModelMockMvc.perform(get("/api/processModels/{id}", processModel.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(processModel.getId()))
//            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
//            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
//            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
//            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
//            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT_STR));
//    }
//
//    @Test
//    public void getNonExistingProcessModel() throws Exception {
//        // Get the processModel
//        restProcessModelMockMvc.perform(get("/api/processModels/{id}", Long.MAX_VALUE))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void updateProcessModel() throws Exception {
//        // Initialize the database
//        processModelRepository.save(processModel);
//
//		int databaseSizeBeforeUpdate = processModelRepository.findAll().size();
//
//        // Update the processModel
//        processModel.setName(UPDATED_NAME);
//        processModel.setComment(UPDATED_COMMENT);
//        processModel.setStatus(UPDATED_STATUS);
//        processModel.setCreatedAt(UPDATED_CREATED_AT);
//        processModel.setUpdatedAt(UPDATED_UPDATED_AT);
//
//        restProcessModelMockMvc.perform(put("/api/processModels")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(processModel)))
//                .andExpect(status().isOk());
//
//        // Validate the ProcessModel in the database
//        List<ProcessModel> processModels = processModelRepository.findAll();
//        assertThat(processModels).hasSize(databaseSizeBeforeUpdate);
//        ProcessModel testProcessModel = processModels.get(processModels.size() - 1);
//        assertThat(testProcessModel.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testProcessModel.getComment()).isEqualTo(UPDATED_COMMENT);
//        assertThat(testProcessModel.getStatus()).isEqualTo(UPDATED_STATUS);
//        assertThat(testProcessModel.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
//        assertThat(testProcessModel.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
//    }
//
//    @Test
//    public void deleteProcessModel() throws Exception {
//        // Initialize the database
//        processModelRepository.save(processModel);
//
//		int databaseSizeBeforeDelete = processModelRepository.findAll().size();
//
//        // Get the processModel
//        restProcessModelMockMvc.perform(delete("/api/processModels/{id}", processModel.getId())
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<ProcessModel> processModels = processModelRepository.findAll();
//        assertThat(processModels).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
