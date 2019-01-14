package edu.fhda.uportal.confgraph.impl.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public interface ExtensibleConfigRepository extends CrudRepository<ExtensibleConfigJpaEntity, ExtensibleConfigEntityId> {



    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<ExtensibleConfigJpaEntity> findByType(String type);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    ExtensibleConfigJpaEntity findByTypeAndFname(String type, String fname);
    
    @Query(
        value =
            "select * " +
            "from config_entity cfg " +
            "inner join config_entity_tag tag on cfg.type = tag.type and cfg.fname = tag.fname " +
            "where cfg.type = ?1 and tag_key = ?2 and tag_value = ?3",
        nativeQuery = true)
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<ExtensibleConfigJpaEntity> findByTypeAndTag(String type, String tagKey, String tagValue);

    @Query("from ExtensibleConfigJpaEntity cfg join cfg.tags tag where key(tag) = ?1 and ?2 in (value(tag))")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    List<ExtensibleConfigJpaEntity> findByTag(String tagKey, String tagValue);

}
