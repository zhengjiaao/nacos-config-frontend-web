package com.dist.nacos.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.dist.nacos.filter.IPInterceptor;
import com.dist.nacos.filter.WhiteList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-25 10:53
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Web Mvc配置
 */
@Component
@Configuration
public class WebMvcFastJsonConfig extends WebMvcConfigurationSupport {

    @Bean
    public WhiteList whiteList() {
        return new WhiteList();
    }

    /**
     * 使用阿里 fastjson 作为JSON MessageConverter
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                //全局修改日期格式,如果时间是data、时间戳类型，按照这种格式初始化时间 "yyyy-MM-dd HH:mm:ss"
                SerializerFeature.WriteDateUseDateFormat,
                // 保留map空的字段
                SerializerFeature.WriteMapNullValue,
                // 将String类型的null转成""
                SerializerFeature.WriteNullStringAsEmpty,
                // 将Number类型的null转成0
                SerializerFeature.WriteNullNumberAsZero,
                // 将List类型的null转成[]
                SerializerFeature.WriteNullListAsEmpty,
                // 将Boolean类型的null转成false
                SerializerFeature.WriteNullBooleanAsFalse,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect);

        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        List<MediaType> mediaTypeList = new ArrayList<>();
        // 解决中文乱码问题，相当于在Controller上的@RequestMapping中加了个属性produces = "application/json"
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypeList);
        converters.add(converter);
    }

    /**
     * 启动时页面重定向到swagger页面
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui.html");
    }

    /**
     * 添加资源: 静态不被springmvc拦截
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //开放所有资源路径
        registry.addResourceHandler("/**").addResourceLocations("/");
        // 添加 swagger和index页面
        registry.addResourceHandler("swagger-ui.html", "index-stop.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/META-INF/resources/static/");

        // 自定义静态资源文件目录 映射到本机服务器目录
//        registry.addResourceHandler(ConfigConstants.file.proxyPath + "/**").addResourceLocations("file:" + ConfigConstants.file.localStoragePath + "\\");
    }

    /**
     * 跨域问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }


    //自定义拦截器
    @Bean
    public IPInterceptor iPInterceptor() {
        return new IPInterceptor(whiteList());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截器注册
        InterceptorRegistration registration = registry.addInterceptor(iPInterceptor())
                .addPathPatterns("/**");

        //默认 不拦截swagger
        List<String> excludePaths = new ArrayList<>();
        excludePaths.add("/swagger-ui/**");
        excludePaths.add("/swagger-ui.html");
        excludePaths.add("/swagger-resources/**");
        excludePaths.add("/v3/api-docs");
        excludePaths.add("/error");
        excludePaths.add("/webjars/**");
        registration.excludePathPatterns(excludePaths);
    }

}
