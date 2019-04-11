package edu.fhda.uportal.confgraph.impl.hazelcast;

import com.hazelcast.core.IMap;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import edu.fhda.uportal.confgraph.api.EntityProvider;
import edu.fhda.uportal.confgraph.api.ExtensibleConfigEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class HazelcastEntityProvider implements EntityProvider {

    private static final Logger log = LogManager.getLogger();

    private final IMap<String, ExtensibleConfigEntity> entityMap;

    HazelcastEntityProvider(IMap entityMap) {
        this.entityMap = entityMap;
    }

    @Override
    public void save(ExtensibleConfigEntity entity) {
        String key = entity.getType() + ":" + entity.getFname();
        entityMap.put(key, entity);
    }

    @Override
    public ExtensibleConfigEntity get(String type, String fname) {
        String key = type + ":" + fname;
        return entityMap.get(key);
    }

    @Override
    public void delete(String type, String fname) {
        String key = type + ":" + fname;
        entityMap.delete(key);
    }

    @Override
    public List<ExtensibleConfigEntity> listAll() {
        return new ArrayList<>(entityMap.values());
    }

    @Override
    public List<ExtensibleConfigEntity> queryByType(String type) {
        // Build query
        EntryObject entry = new PredicateBuilder().getEntryObject();
        Predicate predicate = entry.get("type").equal(type);

        // Execute query
        return new ArrayList<>(entityMap.values(predicate));
    }

    @Override
    public List<ExtensibleConfigEntity> queryByTypeAndTag(String type, String... tagSpecs) {
        // Build query
        EntryObject entry = new PredicateBuilder().getEntryObject();
        PredicateBuilder predicate = entry.get("type").equal(type);

        // Iterate and apply tags to query predicate
        for(String spec : tagSpecs) {
            predicate = predicate.and(entry.get("tags").equal(spec));
        }

        log.trace("Built Hazelcast query {}", predicate);

        // Execute query
        return new ArrayList<>(entityMap.values(predicate));
    }

    @Override
    public List<ExtensibleConfigEntity> queryByTag(String... tagSpecs) {
        // Build query
        EntryObject entry = new PredicateBuilder().getEntryObject();
        PredicateBuilder predicate = null;

        // Iterate and apply tags to query predicate
        for(String spec : tagSpecs) {
            if(predicate == null) {
                predicate = entry.get("tags").equal(spec);
            }
            else {
                predicate = predicate.and(entry.get("tags").equal(spec));
            }
        }

        log.trace("Built Hazelcast query {}", predicate);

        // Execute query
        return new ArrayList<>(entityMap.values(predicate));
    }

}
