package com.tsp.new_tsp_front.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.List.of;

@Configuration
@EnableCaching
public class CacheConfiguration {

    /**
     * <pre>
     * 1. MethodName : cacheManager
     * 2. ClassName  : CacheConfiguration.java
     * 3. Comment    : api cache 설정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 02. 09.
     * </pre>
     */
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(of(
                new ConcurrentMapCache("model"),
                new ConcurrentMapCache("production"),
                new ConcurrentMapCache("portfolio")));
        return cacheManager;
    }
}
