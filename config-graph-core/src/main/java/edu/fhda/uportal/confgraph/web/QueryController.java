package edu.fhda.uportal.confgraph.web;

import com.hazelcast.core.IMap;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import edu.fhda.uportal.confgraph.impl.hazelcast.ExtensibleHazelcastEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Spring controller with routes for performing administrative queries of stored configuration entities.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@Controller
public class QueryController {

    private static final Logger log = LogManager.getLogger();

    @Autowired IMap<String, ExtensibleHazelcastEntity> entityStorageMap;

    /**
     * Query stored entities by type.
     * @param type Type path variable
     * @param tagKey Tag key query variable (optional)
     * @param tagValue Tag valye query variable (optional)
     * @return List of zero or more entity objects
     */
    @RequestMapping(
        value="admin/query/{type}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<ExtensibleHazelcastEntity> queryByType(
        @PathVariable String type,
        @RequestParam(value = "tagkey", required = false) String tagKey,
        @RequestParam(value = "tagval", required = false) String tagValue) {

        log.debug("Handling entity type query type={} tag_key={} tag_value={}", type, tagKey, tagValue);

        // Prepare query
        EntryObject entry = new PredicateBuilder().getEntryObject();

        if(tagKey != null && tagValue != null) {
            // If tags have been provided, then use them as part of query predicates
            Predicate predicate = entry
                .get("type").equal(type)
                .and(entry.get("tags").equal(tagKey + "=" + tagValue));

            return entityStorageMap.values(predicate);
        }
        else {
            // Query exclusively by entity type
            Predicate predicate = entry.get("type").equal(type);
            return entityStorageMap.values(predicate);
        }
    }

    /**
     * Query for a specific entity by type and fname.
     * @param type Type path variable
     * @param fname Fname path variable
     * @return The entity
     */
    @RequestMapping(
        value="admin/query/{type}/{fname}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ExtensibleHazelcastEntity queryByTypeAndTags(@PathVariable String type, @PathVariable String fname) {
        log.debug("Handling entity type and fname query type={} fname={}", type, fname);
        // Prepare query
        EntryObject entry = new PredicateBuilder().getEntryObject();
        Predicate predicate = entry
            .get("type").equal(type)
            .and(entry.get("fname").equal(fname));

        // Execute query
        return new ArrayList<>(entityStorageMap.values(predicate)).get(0);
    }

    /**
     * Query stored entities by a tag key and value.
     * @param tagKey Tag key query variable
     * @param tagValue Tag value query variable
     * @return List of zero or more entity objects
     */
    @RequestMapping(
        value="admin/query/tag/{tagkey}/{tagval}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<ExtensibleHazelcastEntity> queryByTags(@PathVariable String tagKey, @PathVariable String tagValue) {
        log.debug("Handling entity tag query key={} value={}", tagKey, tagValue);
        // Prepare query
        EntryObject entry = new PredicateBuilder().getEntryObject();
        Predicate predicate = entry.get("tags").equal(tagKey + "=" + tagValue);

        // Execute query
        return entityStorageMap.values(predicate);
    }

}
