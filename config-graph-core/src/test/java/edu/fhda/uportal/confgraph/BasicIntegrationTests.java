package edu.fhda.uportal.confgraph;

import edu.fhda.uportal.confgraph.api.EntityProvider;
import edu.fhda.uportal.confgraph.api.ExtensibleConfigEntity;
import edu.fhda.uportal.confgraph.impl.SimpleEntity;
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
    
    @Autowired EntityProvider entityProvider;

    @Test
    @DisplayName("Create a new config entity")
    @Order(1)
    void createNewEntity() {
        // Create a new entity
        ExtensibleConfigEntity entity = new SimpleEntity("content", "student2");

        // Set some stuff on the graph
        entity.getGraph().put("attribute1", "helloworld");
        entity.getGraph().put("attribute2", "yaynoschema");

        // Add some tags
        entity.getTags().put("campus", "foothill");

        // Save it
        entityProvider.save(entity);
    }

    @RepeatedTest(5)
    @DisplayName("Query entity by tags")
    @Order(2)
    void queryEntityByTags() {
        List<ExtensibleConfigEntity> entities = entityProvider.queryByTag("campus=foothill");
        assert entities.size() > 1;
    }

    @RepeatedTest(5)
    @DisplayName("Query entity by multiple tags")
    @Order(3)
    void queryEntityByMultipleTags() {
        List<ExtensibleConfigEntity> entities = entityProvider.queryByTag(
            "audience=Faculty", "application=Starfish");
        assert entities.size() == 1;
    }

    @RepeatedTest(5)
    @DisplayName("Query entity by type")
    @Order(4)
    void queryEntityByType() {
        List<ExtensibleConfigEntity> entities = entityProvider.queryByType("content");
        assert entities.size() > 1;
    }

    @RepeatedTest(5)
    @DisplayName("Query entity by type and tags")
    @Order(5)
    void queryEntityByTypeAndTags() {
        List<ExtensibleConfigEntity> entities = entityProvider.queryByTypeAndTag(
            "content",
            "campus=foothill");
        assert entities.size() > 1;
    }

    @RepeatedTest(5)
    @DisplayName("Get entity by type and fname")
    @Order(6)
    void getEntity() {
        ExtensibleConfigEntity entity = entityProvider.get("content", "student2");
        assert entity != null;
    }

    @Test
    @DisplayName("Delete entity by type and fname")
    @Order(7)
    void removeEntity() {
        entityProvider.delete("content", "student2");
    }

    @Test
    @DisplayName("Verify entity was deleted")
    @Order(8)
    void verifyDeletedEntity() {
        ExtensibleConfigEntity entity = entityProvider.get("content", "student2");
        assert entity == null;
    }

}
