package edu.fhda.uportal.confgraph;

import com.hazelcast.core.IMap;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import edu.fhda.uportal.confgraph.impl.hazelcast.ExtensibleHazelcastEntity;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BasicIntegrationTests {

    @Autowired IMap<String, ExtensibleHazelcastEntity> entityStorageMap;

    @Test
    @DisplayName("Create a new config entity")
    @Order(1)
    void createNewEntity() {
        // Create a new entity
        ExtensibleHazelcastEntity entity = new ExtensibleHazelcastEntity("content", "student");

        // Set some stuff on the graph
        entity.getGraph().put("attribute1", "helloworld");
        entity.getGraph().put("attribute2", "yaynoschema");

        // Add some tags
        entity.getTags().put("campus", "foothill");

        // Save it
        entityStorageMap.put(entity.getDistributedMapKey(), entity);
    }

    @Test
    @DisplayName("Query entity by tags")
    @Order(2)
    @RepeatedTest(5)
    void queryEntityByTags() {
        // Build query
        EntryObject entry = new PredicateBuilder().getEntryObject();
        Predicate predicate = entry.get("tags").equal("campus=foothill");

        // Execute query
        Collection<ExtensibleHazelcastEntity> results = entityStorageMap.values(predicate);
        assert results.size() > 1;
    }

    @Test
    @DisplayName("Query entity by type and fname")
    @Order(3)
    @RepeatedTest(5)
    void queryEntityByTypeAndFname() {
        // Build query
        EntryObject entry = new PredicateBuilder().getEntryObject();
        Predicate predicate = entry
            .get("type").equal("content")
            .and(entry.get("fname").equal("student"));

        // Execute query
        Collection<ExtensibleHazelcastEntity> results = entityStorageMap.values(predicate);
        assert results.size() == 1;
    }

    @Test
    @DisplayName("Delete entity by type and fname")
    @Order(4)
    void removeEntity() {
        entityStorageMap.delete("content:student");
    }

    @Test
    @DisplayName("Verify entity was deleted")
    @Order(5)
    void verifyDeletedEntity() {
        // Build query
        EntryObject entry = new PredicateBuilder().getEntryObject();
        Predicate predicate = entry
            .get("type").equal("content")
            .and(entry.get("fname").equal("student"));

        // Execute query
        Collection<ExtensibleHazelcastEntity> results = entityStorageMap.values(predicate);
        assert results.size() == 0;
    }

}
