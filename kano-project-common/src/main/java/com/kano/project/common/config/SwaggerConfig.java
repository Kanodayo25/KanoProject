package com.kano.project.common.config;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Value("${haotu.swagger.basePackage:com.kano.project.controller.controller}")
    private String basePackage;

    @Bean
    public Docket adminApi() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        ticketPar.name("Authorization").description("key:Authorization=jwtToken")
                .modelRef(new ModelRef("string")).parameterType("header")
                // header中的ticket参数非必填，传空也可以
                .required(false).build();
        List<Parameter> pars = Collections.singletonList(ticketPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .groupName("default")
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build().apiInfo(buildApiInfo())
                .globalOperationParameters(pars)
                .securitySchemes(Arrays.asList(apiKey()));
    }

    @Bean
    public SecurityScheme apiKey() {
        return new ApiKey("Bearer", HttpHeaders.AUTHORIZATION, "header");
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("KanoProject")
                .description("")
                .version("1.0")
                .contact(new Contact("Kano", "", "ylt8410407@163.com"))
                .build();
    }
}