package me.lozm.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String PATH = "/";

    @Bean
    public Docket api() {
        return new Docket
                (DocumentationType.SWAGGER_2)
                .directModelSubstitute(LocalTime.class, String.class)
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("me.lozm"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("community-api Documentation")
                .description("커뮤니티 API 서버 문서입니다.")
                .license("JUN LEE(@LaOnZenaMoon)")
                .licenseUrl("https://github.com/LaOnZenaMoon")
                .termsOfServiceUrl("https://github.com/LaOnZenaMoon")
                .version("0.0.1")
                .contact(new Contact("JUN LEE ", "https://github.com/LaOnZenaMoon", "laonzenamoon@gmail.com"))
                .build();
    }

}
