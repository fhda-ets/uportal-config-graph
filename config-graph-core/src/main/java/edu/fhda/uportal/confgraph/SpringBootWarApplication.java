package edu.fhda.uportal.confgraph;

import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Spring Boot servlet initializer that will be auto-detected by Servlet 3.0 contains to automatically deploy
 * the config graph web application.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class SpringBootWarApplication extends SpringBootServletInitializer {

    /**
     * Configure the web application, and build the Spring context.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // Build application without preloading properties
        return builder
            .bannerMode(Banner.Mode.OFF)
            .sources(SpringConfig.class);
    }

}
