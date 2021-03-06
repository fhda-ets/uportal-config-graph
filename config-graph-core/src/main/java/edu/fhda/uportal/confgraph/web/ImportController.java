package edu.fhda.uportal.confgraph.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fhda.uportal.confgraph.api.EntityProvider;
import edu.fhda.uportal.confgraph.api.ExtensibleConfigEntity;
import edu.fhda.uportal.confgraph.impl.SimpleEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring controller with routes for importing new entities.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@Controller
public class ImportController {

    private static final Logger log = LogManager.getLogger();

    @Autowired @Qualifier("jacksonJsonMapper") ObjectMapper jacksonJsonMapper;
    @Autowired @Qualifier("jacksonYamlMapper") ObjectMapper jacksonYamlMapper;
    @Autowired EntityProvider entityProvider;

    /**
     * Import an entity from a JSON document.
     * @param bytes Request body as a byte array
     * @return Outcome of the import operation
     */
    @RequestMapping(
        value="admin/import",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> handleJsonImport(@RequestBody byte[] bytes) {
        try {
            // Create response
            Map<String, Object> response = new HashMap<>();

            // Parse YAML from request body
            Map<String, Object> payload = jacksonJsonMapper.readValue(bytes, HashMap.class);
            log.debug("Received new JSON import type={} fname={}", payload.get("type"), payload.get("fname"));

            // Delegate to internal mapping method
            this.mapAndSaveEntity(payload);

            // Update and return response
            response.put("status", "success");
            response.put("data", payload);
            return response;
        }
        catch(Exception error) {
            log.error("Failed to import JSON via API", error);
            throw new RuntimeException(error);
        }
    }

    /**
     * Import an entity from a YAML document.
     * @param bytes Request body as a byte array
     * @return Outcome of the import operation
     */
    @RequestMapping(
        value="admin/import",
        method = RequestMethod.POST,
        consumes = "application/x-yaml",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> handleYamlImport(@RequestBody byte[] bytes) {
        try {
            // Create response
            Map<String, Object> response = new HashMap<>();

            // Parse YAML from request body
            Map<String, Object> payload = jacksonYamlMapper.readValue(bytes, HashMap.class);
            log.debug("Received new YAML import type={} fname={}", payload.get("type"), payload.get("fname"));

            // Delegate to internal mapping method
            this.mapAndSaveEntity(payload);

            // Update and return response
            response.put("status", "success");
            response.put("data", payload);
            return response;
        }
        catch(Exception error) {
            log.error("Failed to import YAML via API", error);
            throw new RuntimeException(error);
        }
    }

    private void mapAndSaveEntity(Map<String, Object> payload) {
        // Map into new entity
        ExtensibleConfigEntity entity = new SimpleEntity(payload);

        // Persist entity into storage
        entityProvider.save(entity);
        log.debug("Successfully imported new entity type={} fname={}", payload.get("type"), payload.get("fname"));
    }

}
