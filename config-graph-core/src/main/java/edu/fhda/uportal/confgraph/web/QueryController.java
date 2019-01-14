package edu.fhda.uportal.confgraph.web;

import edu.fhda.uportal.confgraph.impl.jpa.ExtensibleConfigJpaEntity;
import edu.fhda.uportal.confgraph.impl.jpa.ExtensibleConfigRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@Controller
public class QueryController {

    private static final Logger log = LogManager.getLogger();

    @Autowired ExtensibleConfigRepository repository;

    @RequestMapping(
        value="admin/query/{type}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ExtensibleConfigJpaEntity> queryByType(
        @PathVariable String type,
        @RequestParam(value = "tagkey", required = false) String tagKey,
        @RequestParam(value = "tagval", required = false) String tagValue) {

        log.debug("Handling entity type query type={} tag_key={} tag_value={}", type, tagKey, tagValue);

        if(tagKey != null && tagValue != null) {
            // If tags have been provided, then use them as part of query predicates
            return repository.findByTypeAndTag(type, tagKey, tagValue);
        }
        else {
            // Query exclusively by entity type
            return repository.findByType(type);
        }
    }

    @RequestMapping(
        value="admin/query/{type}/{fname}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ExtensibleConfigJpaEntity queryByTypeAndTags(@PathVariable String type, @PathVariable String fname) {
        log.debug("Handling entity type and fname query type={} fname={}", type, fname);
        return repository.findByTypeAndFname(type, fname);
    }

    @RequestMapping(
        value="admin/query/tag/{tagkey}/{tagval}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ExtensibleConfigJpaEntity> queryByTags(@PathVariable String tagkey, @PathVariable String tagval) {
        log.debug("Handling entity tag query key={} value={}", tagkey, tagval);
        return repository.findByTag(tagkey, tagval);
    }

}
