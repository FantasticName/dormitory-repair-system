package io.github.fantasticname.dormitory.repair.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 资源配置类
 * 
 * @author FantasticName
 */
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源访问
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        // 配置上传文件访问
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        
        // 配置前端页面访问
        registry.addResourceHandler("/**")
                .addResourceLocations("file:../frontend/")
                .addResourceLocations("classpath:/static/");
    }

}