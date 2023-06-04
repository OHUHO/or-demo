package com.record.security.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/***
 * Swagger配置
 * 1、切记 SpringBoot3 需要 JDK17 以上
 * 2、该项目使用了 Knife4j 的增强模式，详见配置文件
 * 3、Swagger文档查看权限账户: admin 1234
 * 4、Knife4j 官网：https://doc.xiaominfo.com
 * 5、关于更多的信息，请参考 微信公众号【京茶吉鹿】
 *
 * http://localhost:8417/swagger-ui/index.html
 * http://localhost:8417/doc.html
 */

// TODO 完善 Authorization 认证

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(info())
                .externalDocs(externalDocs())
                .components(components())
                .addSecurityItem(securityRequirement())
                ;
    }


    private Info info(){
        return new Info()
                .title("京茶吉鹿的 Demo")
                .version("v0.0.1")
                .description("Spring Boot 3 + Spring Security + JWT + OpenAPI3")
                .license(new License()
                        .name("Apache 2.0") // The Apache License, Version 2.0
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                .contact(new Contact()
                        .name("京茶吉鹿")
                        .url("http://localost:8417")
                        .email("jc.top@qq.com"))
                .termsOfService("http://localhost:8417")
                ;
    }

    private ExternalDocumentation externalDocs() {
        return new ExternalDocumentation()
                .description("京茶吉鹿的开放文档")
                .url("http://localhost:8417/docs");
    }

    private Components components(){
        return new Components()
                .addSecuritySchemes("Bearer Authorization",
                        new SecurityScheme()
                                .name("Bearer 认证")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                )
                .addSecuritySchemes("Basic Authorization",
                        new SecurityScheme()
                                .name("Basic 认证")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                )
                ;

    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("Bearer Authorization");
    }


    /**
     * 全局请求头
     * @return
     */
    /* @Bean
    public OperationCustomizer operationCustomizer(){
        return (operation, handlerMethod) -> operation.addParametersItem(
                new Parameter()
                        .in("header")
                        .required(true)
                        .description("token 验证")
                        .name("token"));
    } */


    private List<SecurityRequirement> security(Components components) {
        return components.getSecuritySchemes()
                .keySet()
                .stream()
                .map(k -> new SecurityRequirement().addList(k))
                .collect(Collectors.toList());
    }


    /**
     * 通用接口
     * @return
     */
    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("身份认证")
                .pathsToMatch("/api/v1/auth/**")
                // 为指定组设置请求头
                // .addOperationCustomizer(operationCustomizer())
                .build();
    }



    /**
     * 一线人员
     * @return
     */
    @Bean
    public GroupedOpenApi chaserApi(){
        return GroupedOpenApi.builder()
                .group("一线人员")
                .pathsToMatch("/api/v1/chaser/**",
                        "/api/v1/experience/search/**",
                        "/api/v1/log/**",
                        "/api/v1/contact/**",
                        "/api/v1/admin/user/update")
                .pathsToExclude("/api/v1/experience/search/id")
                .build();
    }

    /**
     * 部门主管
     * @return
     */
    @Bean
    public GroupedOpenApi supervisorApi(){
        return GroupedOpenApi.builder()
                .group("部门主管")
                .pathsToMatch("/api/v1/supervisor/**",
                        "/api/v1/experience/**",
                        "/api/v1/schedule/**",
                        "/api/v1/contact/**",
                        "/api/v1/admin/user/update")
                .build();
    }

    /**
     * 系统管理员
     * @return
     */
    @Bean
    public GroupedOpenApi adminApi(){
        return GroupedOpenApi.builder()
                .group("系统管理员")
                .pathsToMatch("/api/v1/admin/**")
                // .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("京茶吉鹿接口—Admin")))
                .build();
    }
}