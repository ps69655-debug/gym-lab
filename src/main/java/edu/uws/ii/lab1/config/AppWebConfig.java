package edu.uws.ii.lab1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class AppWebConfig implements WebMvcConfigurer {

    @Value("${files.location:uploads}")
    private String filesLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path baseDir = Path.of(filesLocation).toAbsolutePath().normalize();
        String location = baseDir.toUri().toString(); // file:/.../

        registry.addResourceHandler("/images/**")
                .addResourceLocations(location);
    }
}