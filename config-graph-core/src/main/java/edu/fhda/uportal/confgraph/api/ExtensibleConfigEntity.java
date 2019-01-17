package edu.fhda.uportal.confgraph.api;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * An data entity object identified by a type and unique name (or "fname"). Entities are designed to implement a common
 * structure regardless of the actual named type that an entity represents. Developer flexibility is provided via the
 * <code>graph</code> property where any hierarchy and arrangement of properties, values, and types can be set that gives the
 * entity purpose and meaning for portal development. The entity storage backend can be configured to do the heavy
 * lifting of serializing and deserializing the graph.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public interface ExtensibleConfigEntity extends Serializable {

    /**
     * Get the named type of this entity.
     * @return The type
     */
    String getType();

    /**
     * Set the named type of this entity.
     * @param type Type to be set
     */
    void setType(String type);

    /**
     * Get the functional name of this entity. Should be a unique identifier among many entity objects of the same type.
     * @return Functional name or "fname"
     */
    String getFname();

    /**
     * Set the functional name of this entity.
     * @param fname The functional name
     */
    void setFname(String fname);

    /**
     * Get the tags assigned to this entity. Tags can be used to add arbitrary pieces of data can be indexed in a backend
     * allow for more diverse queries.
     * @return Map of tags
     */
    Map<String, String> getTags();

    /**
     * Set the tags assigned to this entity.
     * @param tags Map of tags
     */
    void setTags(Map<String, String> tags);

    /**
     * Get the graph.
     * @return Map of entity properties and values.
     */
    Map<String, Object> getGraph();

    /**
     * Set the graph.
     * @param graph Map of entity properties and values.
     */
    void setGraph(Map<String, Object> graph);

    /**
     * Get the ACL expressions used to protect this entity. Keys are actions, and the value is a Spring expression
     * that can be evaluated.
     * @return Map of ACLs
     */
    Map<String, List<String>> getAcls();

    /**
     * Set the map of ACL expressions used to protect this entity.
     * @param acls Map of ACLs
     */
    void setAcls(Map<String, List<String>> acls);

    /**
     * Get the timestamp for when the entity was created. Used for auditing in the storage backend.
     * @return Created timestamp
     */
    LocalDateTime getDateCreated();

    /**
     * Set the timestamp for when the entity was created.
     * @param dateCreated Created timestamp
     */
    void setDateCreated(LocalDateTime dateCreated);

    /**
     * Get the timestamp for when the entity was last updated. Used for auditing in the storage backend.
     * @return Updated timestamp
     */
    LocalDateTime getDateUpdated();

    /**
     * Set the timestamp for when the entity was last updated. Used for auditing in the storage backend.
     * @param dateUpdated Updated timestamp
     */
    void setDateUpdated(LocalDateTime dateUpdated);

}
