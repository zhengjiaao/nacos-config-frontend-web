package com.dist.nacos.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-07 14:17
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：swagger 配置
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket adminApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Admin API")
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(paths())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private Predicate<String> paths(){
        return PathSelectors.regex("^/(?!error).*$");
    }

    private ApiInfo apiInfo(){
        Contact contact = new Contact("BaiDu", "controller://baidu.com", " zhengja@dist.com.cn");
        return new ApiInfoBuilder()
                .title("提供给前端的Nacos配置中转系统")
                .description("开发API文档")
                .contact(contact)
                .version("2.0")
                .build();
    }

    /**
     * 关闭swagger页面 Models默认打开
     * @return
     */
    @Bean
    public UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder().defaultModelsExpandDepth(0)
                .build();
    }
}
