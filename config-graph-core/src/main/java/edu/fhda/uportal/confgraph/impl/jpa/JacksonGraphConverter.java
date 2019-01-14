package edu.fhda.uportal.confgraph.impl.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class JacksonGraphConverter implements AttributeConverter<Map<String, Object>, String> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        }
        catch(Exception error) {
            throw new RuntimeException(error);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String data) {
        try {
            return objectMapper.readValue(data, HashMap.class);
        }
        catch(Exception error) {
            throw new RuntimeException(error);
        }
    }
    
}
