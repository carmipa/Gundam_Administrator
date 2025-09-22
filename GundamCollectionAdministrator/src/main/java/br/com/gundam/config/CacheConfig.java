package br.com.gundam.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        // simples e sem dependência extra; se preferir Caffeine, eu troco
        return new ConcurrentMapCacheManager(
                "grades_all", "escalas_all", "alturas_all", "kit_by_id", "universos_all"
        );
    }
}
