package edu.fhda.uportal.confgraph.impl;

import edu.fhda.uportal.confgraph.api.AbstractExtensibleConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class SimpleEntity extends AbstractExtensibleConfigEntity {

    public SimpleEntity(String type, String fname) {
        super(type, fname);
    }

    public SimpleEntity(Map<String, Object> payload) {
        // Create entity from external map
        super((String) payload.get("type"), (String) payload.get("fname"));

        if(payload.containsKey("acls")) {
            this.setAcls((Map<String, List<String>>) payload.get("acls"));
        }

        if(payload.containsKey("graph")) {
            this.setGraph((Map<String, Object>) payload.get("graph"));
        }

        if(payload.containsKey("tags")) {
            this.setTags((Map<String, String>) payload.get("tags"));
        }
    }
    
}
