package edu.fhda.uportal.confgraph.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public abstract class AbstractExtensibleConfigEntity implements ExtensibleConfigEntity {

    private String type;
    private String fname;
    private Map<String, String> tags = new HashMap<>();
    private Map<String, Object> graph = new HashMap<>();
    private Map<String, List<String>> acls = new HashMap<>();
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    /**
     * Create a new entity with the minimum type and fname values.
     * @param type Type of entity
     * @param fname Unique functional name
     */
    public AbstractExtensibleConfigEntity(String type, String fname) {
        this.type = type;
        this.fname = fname;
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = LocalDateTime.now();
    }

    /**
     * Empty constructor.
     */
    protected AbstractExtensibleConfigEntity() {
    }

    public String getType() {
        return this.type;
    }

    public String getFname() {
        return this.fname;
    }

    public Map<String, String> getTags() {
        return this.tags;
    }

    public Map<String, Object> getGraph() {
        return this.graph;
    }

    public Map<String, List<String>> getAcls() {
        return this.acls;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss a")
    public LocalDateTime getDateCreated() {
        return this.dateCreated;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss a")
    public LocalDateTime getDateUpdated() {
        return this.dateUpdated;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
        this.refreshUpdatedTimestamp();
    }

    public void setAcls(Map<String, List<String>> acls) {
        this.acls = acls;
        this.refreshUpdatedTimestamp();
    }

    public void setGraph(Map<String, Object> graph) {
        this.graph = graph;
        this.refreshUpdatedTimestamp();
    }

    protected void refreshUpdatedTimestamp() {
        this.dateUpdated = LocalDateTime.now();
    }

    /**
     * Compare objects by type and fname (implemented using Guava).
     * @param other Another entity to compare.
     * @return True if equal by type and fname, false if not.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ExtensibleConfigEntity entity = (ExtensibleConfigEntity) other;
        return Objects.equal(type, entity.getType()) &&
            Objects.equal(fname, entity.getFname());
    }

    /**
     * Implemented using Guava.
     * @return Hash code based on type and fname property values.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(type, fname);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("type", type)
            .add("fname", fname)
            .add("tags", tags)
            .add("graph", graph)
            .add("acls", acls)
            .add("dateCreated", dateCreated)
            .add("dateUpdated", dateUpdated)
            .toString();
    }
}
