package me.lozm.global.config.ehcache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Slf4j
public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(target.getClass().getName() + "_");
        keyBuilder.append(method.getName() + "_");
        keyBuilder.append(StringUtils.arrayToDelimitedString(params, "_"));
        log.debug(String.format("CustomKeyGenerator generates %s", keyBuilder));
        return keyBuilder.toString();
    }

}
