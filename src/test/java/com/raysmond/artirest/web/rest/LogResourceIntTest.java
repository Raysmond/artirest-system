//package com.raysmond.artirest.web.rest;
//
//import com.raysmond.artirest.Application;
//import com.raysmond.artirest.domain.Log;
//import com.raysmond.artirest.repository.LogRepository;
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
//import com.raysmond.artirest.domain.enumeration.LogType;
//
///**
// * Test class for the LogResource REST controller.
// *
// * @see LogResource
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
//@IntegrationTest
//public class LogResourceIntTest {
//
//    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));
//
//    private static final String DEFAULT_TITLE = "AAAAA";
//    private static final String UPDATED_TITLE = "BBBBB";
//    private static final String DEFAULT_PROCESS_ID = "AAAAA";
//    private static final String UPDATED_PROCESS_ID = "BBBBB";
//    private static final String DEFAULT_ARTIFACT_ID = "AAAAA";
//    private static final String UPDATED_ARTIFACT_ID = "BBBBB";
//    private static final String DEFAULT_FROM_STATE = "AAAAA";
//    private static final String UPDATED_FROM_STATE = "BBBBB";
//    private static final String DEFAULT_TO_STATE = "AAAAA";
//    private static final String UPDATED_TO_STATE = "BBBBB";
//    private static final String DEFAULT_SERVICE = "AAAAA";
//    private static final String UPDATED_SERVICE = "BBBBB";
//    private static final String DEFAULT_MEMO = "AAAAA";
//    private static final String UPDATED_MEMO = "BBBBB";
//
//    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
//    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//    private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.format(DEFAULT_CREATED_AT);
//
//
//    private static final LogType DEFAULT_TYPE = LogType.CREATE_PROCESS_INSTANCE;
//    private static final LogType UPDATED_TYPE = LogType.CREATE_ARTIFACT;
//
//    @Inject
//    private LogRepository logRepository;
//
//    @Inject
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
//
//    @Inject
//    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
//
//    private MockMvc restLogMockMvc;
//
//    private Log log;
//
//    @PostConstruct
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        LogResource logResource = new LogResource();
//        ReflectionTestUtils.setField(logResource, "logRepository", logRepository);
//        this.restLogMockMvc = MockMvcBuilders.standaloneSetup(logResource)
//            .setCustomArgumentResolvers(pageableArgumentResolver)
//            .setMessageConverters(jacksonMessageConverter).build();
//    }
//
//    @Before
//    public void initTest() {
//        logRepository.deleteAll();
//        log = new Log();
//        log.setTitle(DEFAULT_TITLE);
//        log.setProcessId(DEFAULT_PROCESS_ID);
//        log.setArtifactId(DEFAULT_ARTIFACT_ID);
//        log.setFromState(DEFAULT_FROM_STATE);
//        log.setToState(DEFAULT_TO_STATE);
//        log.setService(DEFAULT_SERVICE);
//        log.setMemo(DEFAULT_MEMO);
//        log.setCreatedAt(DEFAULT_CREATED_AT);
//        log.setType(DEFAULT_TYPE);
//    }
//
//    @Test
//    public void createLog() throws Exception {
//        int databaseSizeBeforeCreate = logRepository.findAll().size();
//
//        // Create the Log
//
//        restLogMockMvc.perform(post("/api/logs")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(log)))
//                .andExpect(status().isCreated());
//
//        // Validate the Log in the database
//        List<Log> logs = logRepository.findAll();
//        assertThat(logs).hasSize(databaseSizeBeforeCreate + 1);
//        Log testLog = logs.get(logs.size() - 1);
//        assertThat(testLog.getTitle()).isEqualTo(DEFAULT_TITLE);
//        assertThat(testLog.getProcessId()).isEqualTo(DEFAULT_PROCESS_ID);
//        assertThat(testLog.getArtifactId()).isEqualTo(DEFAULT_ARTIFACT_ID);
//        assertThat(testLog.getFromState()).isEqualTo(DEFAULT_FROM_STATE);
//        assertThat(testLog.getToState()).isEqualTo(DEFAULT_TO_STATE);
//        assertThat(testLog.getService()).isEqualTo(DEFAULT_SERVICE);
//        assertThat(testLog.getMemo()).isEqualTo(DEFAULT_MEMO);
//        assertThat(testLog.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
//        assertThat(testLog.getType()).isEqualTo(DEFAULT_TYPE);
//    }
//
//    @Test
//    public void getAllLogs() throws Exception {
//        // Initialize the database
//        logRepository.save(log);
//
//        // Get all the logs
//        restLogMockMvc.perform(get("/api/logs?sort=id,desc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.[*].id").value(hasItem(log.getId())))
//                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
//                .andExpect(jsonPath("$.[*].processId").value(hasItem(DEFAULT_PROCESS_ID.toString())))
//                .andExpect(jsonPath("$.[*].artifactId").value(hasItem(DEFAULT_ARTIFACT_ID.toString())))
//                .andExpect(jsonPath("$.[*].fromState").value(hasItem(DEFAULT_FROM_STATE.toString())))
//                .andExpect(jsonPath("$.[*].toState").value(hasItem(DEFAULT_TO_STATE.toString())))
//                .andExpect(jsonPath("$.[*].service").value(hasItem(DEFAULT_SERVICE.toString())))
//                .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())))
//                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
//                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
//    }
//
//    @Test
//    public void getLog() throws Exception {
//        // Initialize the database
//        logRepository.save(log);
//
//        // Get the log
//        restLogMockMvc.perform(get("/api/logs/{id}", log.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(log.getId()))
//            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
//            .andExpect(jsonPath("$.processId").value(DEFAULT_PROCESS_ID.toString()))
//            .andExpect(jsonPath("$.artifactId").value(DEFAULT_ARTIFACT_ID.toString()))
//            .andExpect(jsonPath("$.fromState").value(DEFAULT_FROM_STATE.toString()))
//            .andExpect(jsonPath("$.toState").value(DEFAULT_TO_STATE.toString()))
//            .andExpect(jsonPath("$.service").value(DEFAULT_SERVICE.toString()))
//            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()))
//            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
//            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
//    }
//
//    @Test
//    public void getNonExistingLog() throws Exception {
//        // Get the log
//        restLogMockMvc.perform(get("/api/logs/{id}", Long.MAX_VALUE))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void updateLog() throws Exception {
//        // Initialize the database
//        logRepository.save(log);
//
//		int databaseSizeBeforeUpdate = logRepository.findAll().size();
//
//        // Update the log
//        log.setTitle(UPDATED_TITLE);
//        log.setProcessId(UPDATED_PROCESS_ID);
//        log.setArtifactId(UPDATED_ARTIFACT_ID);
//        log.setFromState(UPDATED_FROM_STATE);
//        log.setToState(UPDATED_TO_STATE);
//        log.setService(UPDATED_SERVICE);
//        log.setMemo(UPDATED_MEMO);
//        log.setCreatedAt(UPDATED_CREATED_AT);
//        log.setType(UPDATED_TYPE);
//
//        restLogMockMvc.perform(put("/api/logs")
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(log)))
//                .andExpect(status().isOk());
//
//        // Validate the Log in the database
//        List<Log> logs = logRepository.findAll();
//        assertThat(logs).hasSize(databaseSizeBeforeUpdate);
//        Log testLog = logs.get(logs.size() - 1);
//        assertThat(testLog.getTitle()).isEqualTo(UPDATED_TITLE);
//        assertThat(testLog.getProcessId()).isEqualTo(UPDATED_PROCESS_ID);
//        assertThat(testLog.getArtifactId()).isEqualTo(UPDATED_ARTIFACT_ID);
//        assertThat(testLog.getFromState()).isEqualTo(UPDATED_FROM_STATE);
//        assertThat(testLog.getToState()).isEqualTo(UPDATED_TO_STATE);
//        assertThat(testLog.getService()).isEqualTo(UPDATED_SERVICE);
//        assertThat(testLog.getMemo()).isEqualTo(UPDATED_MEMO);
//        assertThat(testLog.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
//        assertThat(testLog.getType()).isEqualTo(UPDATED_TYPE);
//    }
//
//    @Test
//    public void deleteLog() throws Exception {
//        // Initialize the database
//        logRepository.save(log);
//
//		int databaseSizeBeforeDelete = logRepository.findAll().size();
//
//        // Get the log
//        restLogMockMvc.perform(delete("/api/logs/{id}", log.getId())
//                .accept(TestUtil.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk());
//
//        // Validate the database is empty
//        List<Log> logs = logRepository.findAll();
//        assertThat(logs).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
