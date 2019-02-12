package edu.fhda.uportal.confgraph;

import edu.fhda.uportal.confgraph.impl.jpa.ExtensibleConfigJpaEntity;
import edu.fhda.uportal.confgraph.impl.jpa.ExtensibleConfigRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BasicIntegrationTests {

    @Autowired ExtensibleConfigRepository repository;

    @Test
    @DisplayName("Create a new config entity")
    @Order(1)
    void createNewEntity() {
        // Create a new entity
        ExtensibleConfigJpaEntity entity = new ExtensibleConfigJpaEntity("content", "student");

        // Set some stuff on the graph
        entity.getGraph().put("attribute1", "helloworld");
        entity.getGraph().put("attribute2", "yaynoschema");

        // Add some tags
        entity.getTags().put("campus", "foothill");

        // Save it
        repository.save(entity);
    }

    @Test
    @DisplayName("Query entity by tags")
    @Order(2)
    void queryEntityByTags() {
        List<ExtensibleConfigJpaEntity> entities = repository
            .findByTag("campus", "foothill");

        System.out.println(entities);
    }

    @Test
    @DisplayName("Delete entity by type and fname")
    @Order(3)
    @Disabled
    void removeEntity() {
        ExtensibleConfigJpaEntity entity = repository.findByTypeAndFname("content", "student");
        repository.delete(entity);
    }

}
