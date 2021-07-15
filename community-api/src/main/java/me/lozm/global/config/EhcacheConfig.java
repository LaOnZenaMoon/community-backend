package me.lozm.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Slf4j
@EnableCaching
@Configuration
@RequiredArgsConstructor
public class EhcacheConfig implements CommandLineRunner {

    private final CacheManager cacheManager;

    @Override
    public void run(String... args) throws Exception {
        log.info("\n\n" + "=========================================================\n"
                + "Using cache manager: " + this.cacheManager.getClass().getName() + "\n"
                + "=========================================================\n\n");
    }

}
