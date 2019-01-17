package edu.fhda.uportal.confgraph.web;

import edu.fhda.uportal.confgraph.SpelServices;
import edu.fhda.uportal.confgraph.impl.jpa.ExtensibleConfigJpaEntity;
import edu.fhda.uportal.confgraph.impl.jpa.ExtensibleConfigRepository;
import edu.fhda.uportal.confgraph.util.DeepAppendableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Spring controller with routes for portal end-users to build personalized graphs from config entities. Entities
 * returned by these routes are first authorized by evaluating ACL expressions against the uPortal open ID token for
 * a user.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@SuppressWarnings({"ConstantConditions", "unchecked"})
@Controller
public class UserGraphController {

    private static final Logger log = LogManager.getLogger();

    @Autowired private ExtensibleConfigRepository repository;
    @Autowired private SpelServices spelServices;

    private StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

    /**
     * Build a graph for a user.
     * @param request HTTP request
     * @param tagKey Tag key query variable (optional)
     * @param tagValue Tag valye query variable (optional)
     * @return One or more matching graphs merged into a final object
     */
    @RequestMapping(
        value="graph/me",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map queryForMe(
        HttpServletRequest request,
        @RequestParam(value = "tagkey", required = false) String tagKey,
        @RequestParam(value = "tagval", required = false) String tagValue) {

        log.debug("Handling end user graph query tag_key={} tag_value={}", tagKey, tagValue);

        // Placeholder
        Iterable<ExtensibleConfigJpaEntity> entities;

        if(tagKey != null && tagValue != null) {
            // If tags have been provided, then use them for query predicates
            entities = repository.findByTag(tagKey, tagValue);
        }
        else {
            // Query every defined entity
            entities = repository.findAll();
        }

        // Create an appendable map to capture the final graph output
        DeepAppendableMap result = new DeepAppendableMap();

        // Create a root object for security expression evaluation
        Map rootObject = new HashMap();
        rootObject.put("claims", request.getAttribute("jwt-claims"));
        log.debug("SpEL root object {}", rootObject);

        // Process entity results
        StreamSupport
            .stream(entities.spliterator(), false)
            .filter(new QueryEntityAclPredicate(rootObject))
            .forEach(entity -> {
                log.trace("Merging entity approved by ACL {}", entity);
                result.append(entity.getGraph());
            });

        log.trace("Final graph {}", result);

        // Return final output
        return result;
    }

    /**
     * Generate an inventory of stored entities.
     * @param request HTTP request
     * @param type Type path variable
     * @param tagKey Tag key query variable (optional)
     * @param tagValue Tag valye query variable (optional)
     * @return List of zero or more basic entity records with type, fname, and tags.
     */
    @RequestMapping(
        value="graph/inventory/{type}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List inventory(
        HttpServletRequest request,
        @PathVariable String type,
        @RequestParam(value = "tagkey", required = false) String tagKey,
        @RequestParam(value = "tagval", required = false) String tagValue) {

        log.debug("Handling end user entity inventory tag_key={} tag_value={}", tagKey, tagValue);

        // Placeholder
        Iterable<ExtensibleConfigJpaEntity> entities;

        if(tagKey != null && tagValue != null) {
            // If tags have been provided, then use them for query predicates
            entities = repository.findByTypeAndTag(type, tagKey, tagValue);
        }
        else {
            // Query all entities by type
            entities = repository.findByType(type);
        }

        // Create a root object for security expression evaluation
        Map rootObject = new HashMap();
        rootObject.put("claims", request.getAttribute("jwt-claims"));
        log.debug("SpEL root object {}", rootObject);

        // Process entity results
        return StreamSupport
            .stream(entities.spliterator(), false)
            .filter(new QueryEntityAclPredicate(rootObject))
            .map(entity -> {
                Map inventoryObject = new HashMap();
                inventoryObject.put("type", entity.getType());
                inventoryObject.put("fname", entity.getFname());
                inventoryObject.put("tags", entity.getTags());
                return inventoryObject;
            })
            .collect(Collectors.toList());

    }

    /**
     * Implementation of a <code>Predicate</code> for stream filtering that verifies if an entity has a query
     * ACL, and the expression for that ACL matches an input root object with the claims from an open ID token issued
     * by the portal.
     */
    class QueryEntityAclPredicate implements Predicate<ExtensibleConfigJpaEntity> {

        private Map rootObject;

        QueryEntityAclPredicate(Map rootObject) {
            this.rootObject = rootObject;
        }

        @Override
        public boolean test(ExtensibleConfigJpaEntity entity) {
            // Check if entity is tagged disabled
            if(entity.getTags().get("graph.disabled").equalsIgnoreCase("true")) {
                return false;
            }

            // Check if a query ACL is present
            if(entity.getAcls().containsKey("query")) {
                log.trace("Evaluating ACL expression={} root={}", entity.getAcls().get("query"), rootObject);

                // Get entity ACLs for query action
                List<String> acls = entity.getAcls().get("query");

                // Iterate each expression
                for(String acl : acls) {
                    // Run the expression
                    boolean result = (Boolean) UserGraphController
                        .this
                        .spelServices
                        .parseExpression(acl)
                        .getValue(evaluationContext, rootObject);

                    log.debug("Evaluated ACL {} -> result {}", acl, result);

                    // If true, exit iteration early
                    if(result) {
                        return true;
                    }
                }
            }

            // If user cannot pass any query ACLs, then exclude entity
            log.debug("No valid user ACLs found for entity {} - excluding from final result", entity);
            return false;
        }

    }

}
