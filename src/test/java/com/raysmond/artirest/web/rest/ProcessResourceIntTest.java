//package com.raysmond.artirest.web.rest;
//
//import com.raysmond.artirest.Application;
//import com.raysmond.artirest.domain.Process;
//import com.raysmond.artirest.repository.ProcessRepository;
//import com.raysmond.artirest.service.ProcessService;
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
// * Test class for the ProcessResource REST controller.
// *
// * @see ProcessResource
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
//@IntegrationTest
//public class ProcessResourceIntTest {
//
//    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));
//
//    private static final String DEFAULT_NAME = "AAAAA";
//    private static final String UPDATED_NAME = "BBBBB";
//
//    private static final Boolean DEFAULT_IS_RUNNING = false;
//    private static final Boolean UPDATED_IS_RUNNING = true;
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
//    private ProcessRepository processRepository;
//
//    @Inject
//    private ProcessService processService;
//
//    @Inject
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
//
//    @Inject
//    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
//
//    private MockMvc restProcessMockMvc;
//
//    private Process process;
//
//    @PostConstruct
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        ProcessResource processResource = new ProcessResource();
//        ReflectionTestUtils.setField(processResource, "processService", processService);
//        this.restProcessMockMvc = MockMvcBuilders.standaloneSetup(processResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setMessageConverters(jacksonMessageConverter).build();
//    }
//
//    @Before
//    public void initTest() {
//        processRepository.deleteAll();
//        process = new Process();
//        process.setName(DEFAULT_NAME);
//        process.setIsRunning(DEFAULT_IS_RUNNING);
//        process.setCreatedAt(DEFAULT_CREATED_AT);
//        process.setUpdatedAt(DEFAULT_UPDATED_AT);
//    }
//
//    @Test
//    public void createProcess() throws Exception {
//        int databaseSizeBeforeCreate = processRepository.findAll().size();
//
//        // Create the Process
//
//        restProcessMockMvc.perform(post("/api/processs")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(process)))
//                .andExpect(status().isCreated());
//
//        // Validate the Process in the database
//        List<Process> processs = processRepository.findAll();
//        assertThat(processs).hasSize(databaseSizeBeforeCreate + 1);
//        Process testProcess = processs.get(processs.size() - 1);
//        assertThat(testProcess.getName()).isEqualTo(DEFAULT_NAME);
//        assertThat(testProcess.getIsRunning()).isEqualTo(DEFAULT_IS_RUNNING);
//        assertThat(testProcess.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
//        assertThat(testProcess.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
//    }
//
//    @Test
//    public void getAllProcesss() throws Exception {
//        // Initialize the database
//        processRepository.save(process);
//
//        // Get all the processs
//        restProcessMockMvc.perform(get("/api/processs?sort=id,desc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.[*].id").value(hasItem(process.getId())))
//                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
//                .andExpect(jsonPath("$.[*].isRunning").value(hasItem(DEFAULT_IS_RUNNING.booleanValue())))
//                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
//                .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT_STR)));
//    }
//
//    @Test
//    public void getProcess() throws Exception {
//        // Initialize the database
//        processRepository.save(process);
//
//        // Get the process
//        restProcessMockMvc.perform(get("/api/processs/{id}", process.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(process.getId()))
//            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
//            .andExpect(jsonPath("$.isRunning").value(DEFAULT_IS_RUNNING.booleanValue()))
//            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
//            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT_STR));
//    }
//
//    @Test
//    public void getNonExistingProcess() throws Exception {
//        // Get the process
//        restProcessMockMvc.perform(get("/api/processs/{id}", Long.MAX_VALUE))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void updateProcess() throws Exception {
//        // Initialize the database
//        processRepository.save(process);
//
//		int databaseSizeBeforeUpdate = processRepository.findAll().size();
//
//        // Update the process
//        process.setName(UPDATED_NAME);
//        process.setIsRunning(UPDATED_IS_RUNNING);
//        process.setCreatedAt(UPDATED_CREATED_AT);
//        process.setUpdatedAt(UPDATED_UPDATED_AT);
//
//        restProcessMockMvc.perform(put("/api/processs")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(process)))
//                .andExpect(status().isOk());
//
//        // Validate the Process in the database
//        List<Process> processs = processRepository.findAll();
//        assertThat(processs).hasSize(databaseSizeBeforeUpdate);
//        Process testProcess = processs.get(processs.size() - 1);
//        assertThat(testProcess.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testProcess.getIsRunning()).isEqualTo(UPDATED_IS_RUNNING);
//        assertThat(testProcess.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
//        assertThat(testProcess.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
//    }
//
//    @Test
//    public void deleteProcess() throws Exception {
//        // Initialize the database
//        processRepository.save(process);
//
//		int databaseSizeBeforeDelete = processRepository.findAll().size();
//
//        // Get the process
//        restProcessMockMvc.perform(delete("/api/processs/{id}", process.getId())
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Process> processs = processRepository.findAll();
//        assertThat(processs).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
