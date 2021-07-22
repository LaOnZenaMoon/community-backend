package me.lozm.global.config.ehcache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class EhCacheConfig {

    @Bean
    public KeyGenerator customKeyGenerator() {
        return new CustomKeyGenerator();
    }

}
