package com.dist.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/nacos-config-frontend-web/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2023/08/11 10:20
 */
@SpringBootApplication
public class NacosConfigApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(NacosConfigApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NacosConfigApplication.class);
    }
}