package edu.fhda.uportal.confgraph.impl.hazelcast;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import edu.fhda.uportal.confgraph.api.EntityProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@Configuration
@Profile("provider-hazelcast")
public class HazelcastSpringConfig {

    @Bean
    public HazelcastInstance hazelcast() throws IOException {
        // Load default Hazelcast configuration from classpath
        ClasspathXmlConfig baseConfig = new ClasspathXmlConfig("hz-config-default.xml");

        // Load external configuration from portal home
        FileSystemXmlConfig externalConfig = new FileSystemXmlConfig(
            Paths.get(System.getProperty("portal.home"), "config-graph-hz.xml").toFile());

        // Apply specific sections from external config to base
        baseConfig.setNetworkConfig(externalConfig.getNetworkConfig());

        // Create Hazelcast instance
        return Hazelcast.newHazelcastInstance(baseConfig);
    }

    @Bean
    public EntityProvider entityProvider(@Autowired HazelcastInstance hazelcast) {
        return new HazelcastEntityProvider(hazelcast.getMap("entities"));
    }

}
