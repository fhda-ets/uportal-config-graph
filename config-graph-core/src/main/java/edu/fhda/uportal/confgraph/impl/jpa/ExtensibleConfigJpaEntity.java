package edu.fhda.uportal.confgraph.impl.jpa;

import com.google.common.base.Objects;
import edu.fhda.uportal.confgraph.api.IExtensibleConfigEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@Entity
@Table(name = "config_entity")
@IdClass(ExtensibleConfigEntityId.class)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExtensibleConfigJpaEntity implements IExtensibleConfigEntity {

    private String type;
    private String fname;
    private Map<String, String> tags = new HashMap<>();
    private Map<String, Object> graph = new HashMap<>();
    private Map<String, String> acls = new HashMap<>();
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public ExtensibleConfigJpaEntity() {
    }

    public ExtensibleConfigJpaEntity(String type, String fname) {
        this.type = type;
        this.fname = fname;
    }

    @Id
    @Override
    public String getType() {
        return this.type;
    }

    @Id
    @Override
    public String getFname() {
        return this.fname;
    }

    @ElementCollection
    @CollectionTable(name="config_entity_tag", joinColumns={
        @JoinColumn(name="fname"),
        @JoinColumn(name="type"),

    })
    @MapKeyColumn (name="tag_key")
    @Column(name="tag_value")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Override
    public Map<String, String> getTags() {
        return this.tags;
    }

    @Column(name="graph")
    @Lob
    @Convert(converter = JacksonGraphConverter.class)
    @Override
    public Map<String, Object> getGraph() {
        return this.graph;
    }

    @ElementCollection
    @CollectionTable(name = "config_entity_acl", joinColumns={
        @JoinColumn(name="fname"),
        @JoinColumn(name="type"),
    })
    @MapKeyColumn (name="action")
    @Column(name="expression")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Override
    public Map<String, String> getAcls() {
        return this.acls;
    }

    @Column(name="date_created")
    @Override
    public LocalDateTime getDateCreated() {
        return this.dateCreated;
    }

    @Column(name="date_updated")
    @Override
    public LocalDateTime getDateUpdated() {
        return this.dateUpdated;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setFname(String fname) {
        this.fname = fname;
    }

    @Override
    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    @Override
    public void setAcls(Map<String, String> acls) {
        this.acls = acls;
    }

    @Override
    public void setGraph(Map<String, Object> graph) {
        this.graph = graph;
    }

    @Override
    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @PrePersist
    protected void onCreate() {
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateUpdated = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtensibleConfigJpaEntity entity = (ExtensibleConfigJpaEntity) o;
        return Objects.equal(type, entity.type) &&
            Objects.equal(fname, entity.fname);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, fname);
    }
    
}
