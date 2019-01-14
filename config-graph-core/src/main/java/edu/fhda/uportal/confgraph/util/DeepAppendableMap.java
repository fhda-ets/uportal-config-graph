package edu.fhda.uportal.confgraph.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@SuppressWarnings("Duplicates")
public class DeepAppendableMap extends HashMap {

    public DeepAppendableMap() {
        super();
    }

    public void append(Map source) {
        deepMerge(this, source);
    }

    /**
     * Originally inspired by https://stackoverflow.com/a/27476077
     * @param target Destination map for appending new entries
     * @param source Source map to get existing entries from
     * @return The target
     */
    private static Map deepMerge(Map target, Map source) {
        source.forEach((key, value) -> {
            Object existingKey =  target.get(key);

            if(existingKey instanceof List && value instanceof List) {
                ((List) target.get(key)).addAll((Collection) value);
            }
            else if(existingKey instanceof Map && value instanceof Map) {
                target.put(key, deepMerge((Map) existingKey, (Map) value));
            }
            else {
                target.put(key, value);
            }
        });

        return target;
    }

}
