package edu.fhda.uportal.confgraph.api;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public interface IExtensibleConfigEntity {
    
    /* Primary key */
    public String getType();
    public void setType(String type);

    public String getFname();
    public void setFname(String fname);

    /* Tags for filtering and querying objects */
    public Map<String, String> getTags();
    public void setTags(Map<String, String> tags);

    /* The schemaless graph */
    public Map<String, Object> getGraph();
    public void setGraph(Map<String, Object> graph);

    /* ACLs for applying user permissions to an object */
    public Map<String, String> getAcls();
    public void setAcls(Map<String, String> acls);

    /* Miscellaneous attributes for auditing */
    public LocalDateTime getDateCreated();
    public void setDateCreated(LocalDateTime dateCreated);

    public LocalDateTime getDateUpdated();
    public void setDateUpdated(LocalDateTime dateUpdated);

}
