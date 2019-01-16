package edu.fhda.uportal.confgraph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.io.ByteArrayResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Spring Boot servlet initializer that will be auto-detected by Servlet 3.0 contains to automatically deploy
 * the config graph web application. Incorporates support for finding configuration files in a location
 * defined with the <code>portal.home</code> system property.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class SpringBootWarApplication extends SpringBootServletInitializer {

    private static final Logger log = LogManager.getLogger();

    /**
     * Configure the web application, and build the Spring context.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // Is a portal.home system property available?
        String portalHome = System.getProperty("portal.home");
        if(portalHome != null) {
            log.debug("Detected portal.home property = {}", portalHome);
            
            // Build path to config file
            Path cfgGraphConfig = Paths.get(portalHome, "config-graph.yml");

            // Does the config file exist?
            if(Files.exists(cfgGraphConfig)) {
                log.debug("Found config-graph.yml");

                // Load YAML file into Properties
                YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
                factoryBean.setResources(readFileFromDisk(cfgGraphConfig));
                Properties cfgGraphProperties = factoryBean.getObject();
                log.debug("Successfully loaded config-graph.yml as properties {}", cfgGraphProperties);

                // Build application with external properties file
                return builder
                    .bannerMode(Banner.Mode.OFF)
                    .properties(cfgGraphProperties)
                    .sources(SpringConfig.class);
            }
        }

        // Build application without preloading properties
        return builder
            .bannerMode(Banner.Mode.OFF)
            .sources(SpringConfig.class);
    }

    private ByteArrayResource readFileFromDisk(Path target) {
        try {
            return new ByteArrayResource(Files.readAllBytes(target));
        }
        catch(Exception error) {
            throw new RuntimeException(error);
        }
    }

}
