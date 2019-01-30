package edu.fhda.uportal.confgraph.util;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.List;

/**
 * https://stackoverflow.com/questions/28303758/how-to-use-yamlpropertiesfactorybean-to-load-yaml-files-using-spring-framework-4
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class YamlPropertySourceFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null) {
            return super.createPropertySource(name, resource);
        }

        List<PropertySource<?>> propertySourceList = new YamlPropertySourceLoader()
            .load(resource.getResource().getFilename(), resource.getResource());

        if (!propertySourceList.isEmpty()) {
            return propertySourceList.iterator().next();
        }

        return super.createPropertySource(name, resource);
    }

}
