package edu.fhda.uportal.confgraph.impl.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * Spring data JPA repository. Lots of Spring superpowers going on here.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public interface ExtensibleConfigRepository extends CrudRepository<ExtensibleConfigJpaEntity, ExtensibleConfigEntityId> {

    /**
     * Query for a list of entity objects by type.
     * @param type Type to search by
     * @return List of zero or more objects returned by the query
     */
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<ExtensibleConfigJpaEntity> findByType(String type);

    /**
     * Find a specific entity object by type and fname.
     * @param type Entity type
     * @param fname Unique entity functional name
     * @return The object if found, or null if not found
     */
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    ExtensibleConfigJpaEntity findByTypeAndFname(String type, String fname);

    /**
     * Query for a list of entity objects by type, and a tag key/value.
     * @param type Type to search by
     * @param tagKey Tag key
     * @param tagValue Tag value
     * @return List of zero or more objects returned by the query
     */
    @Query(
        value =
            "select * " +
            "from config_entity cfg " +
            "inner join config_entity_tag tag on cfg.type = tag.type and cfg.fname = tag.fname " +
            "where cfg.type = ?1 and tag_key = ?2 and tag_value = ?3",
        nativeQuery = true)
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<ExtensibleConfigJpaEntity> findByTypeAndTag(String type, String tagKey, String tagValue);

    /**
     * Query for a list of entity objects by tag key and value.
     * @param tagKey Tag key
     * @param tagValue Tag value
     * @return List of zero or more objects returned by the query
     */
    @Query("from ExtensibleConfigJpaEntity cfg join cfg.tags tag where key(tag) = ?1 and ?2 in (value(tag))")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<ExtensibleConfigJpaEntity> findByTag(String tagKey, String tagValue);

}
