package edu.fhda.uportal.confgraph.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@Configuration
@EnableWebMvc
public class SpringMvcConfiguration implements WebMvcConfigurer {

    private static final Logger log = LogManager.getLogger();

    @Value("${config-graph.cors-config.allowed-origins:}")
    private String configuredOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Create the CORS registration
        CorsRegistration allPaths = registry.addMapping("/**");

        // Add allowed origins
        allPaths.allowedOrigins(configuredOrigins.split("[,]"));

        log.info("Completed CORS configuration");
    }

}
