package me.lozm.global.config.ehcache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;

public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(method.getName());
        keyBuilder.append(SimpleKeyGenerator.generateKey(params));
        return keyBuilder.toString();
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }

}
