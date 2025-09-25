package org.example.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded files
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        
        registry.addResourceHandler("/static/profiles/**")
                .addResourceLocations("file:uploads/profiles/");
        
        registry.addResourceHandler("/static/covers/**")
                .addResourceLocations("file:uploads/covers/");
        
        // Serve static resources
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // Serve SPA built assets under /app/** from classpath:/static/app/
        registry.addResourceHandler("/app/**")
                .addResourceLocations("classpath:/static/app/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward SPA routes (any path after /app) to SPA index.html
        registry.addViewController("/app").setViewName("forward:/static/app/index.html");
        registry.addViewController("/app/{*path}").setViewName("forward:/static/app/index.html");
    }
}
