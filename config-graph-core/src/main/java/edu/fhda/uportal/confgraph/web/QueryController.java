package edu.fhda.uportal.confgraph.web;

import edu.fhda.uportal.confgraph.api.EntityProvider;
import edu.fhda.uportal.confgraph.api.ExtensibleConfigEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Spring controller with routes for performing administrative queries of stored configuration entities.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@Controller
public class QueryController {

    private static final Logger log = LogManager.getLogger();

    @Autowired EntityProvider entityProvider;

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
    public List<ExtensibleConfigEntity> queryByType(
        @PathVariable String type,
        @RequestParam(value = "tagkey", required = false) String tagKey,
        @RequestParam(value = "tagval", required = false) String tagValue) {

        log.debug("Handling entity type query type={} tag_key={} tag_value={}", type, tagKey, tagValue);

        if(tagKey != null && tagValue != null) {
            return entityProvider.queryByTypeAndTag(type, tagKey + "=" + tagValue);
        }
        return entityProvider.queryByType(type);
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
    public ExtensibleConfigEntity queryByTypeAndFname(@PathVariable String type, @PathVariable String fname) {
        log.debug("Handling entity type and fname query type={} fname={}", type, fname);

        return entityProvider.get(type, fname);
    }

    /**
     * Query stored entities by a tag key and value.
     * @param tagKey Tag key query variable
     * @param tagValue Tag value query variable
     * @return List of zero or more entity objects
     */
    @RequestMapping(
        value="admin/query/tag/{tagKey}/{tagValue}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ExtensibleConfigEntity> queryByTags(@PathVariable String tagKey, @PathVariable String tagValue) {
        log.debug("Handling entity tag query key={} value={}", tagKey, tagValue);

        return entityProvider.queryByTag(tagKey + "=" + tagValue);
    }

}
