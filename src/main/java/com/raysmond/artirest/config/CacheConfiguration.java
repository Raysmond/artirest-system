package com.raysmond.artirest.config;

import com.raysmond.artirest.service.ProcessModelService;
import com.raysmond.artirest.service.ProcessService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.cache.support.NoOpCacheManager;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = {MetricsConfiguration.class, DatabaseConfiguration.class})
@Profile("!" + Constants.SPRING_PROFILE_FAST)
public class CacheConfiguration {

    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

//    private CacheManager cacheManager;

    @PreDestroy
    public void destroy() {
        log.info("Closing Cache Manager");
    }

//    @Bean
//    public CacheManager cacheManager() {
//        log.debug("Simple cache.");
//
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(
//            Arrays.asList(ProcessModelService.CACHE_NAME, ProcessService.CACHE_NAME).stream()
//                .map(name -> new ConcurrentMapCache(name))
//                .collect(Collectors.toList()));
//        return cacheManager;
//    }
}
