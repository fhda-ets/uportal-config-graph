package edu.fhda.uportal.confgraph.api;

import java.util.List;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public interface EntityProvider {

    void save(ExtensibleConfigEntity entity);

    void delete (String type, String fname);

    ExtensibleConfigEntity get(String type, String fname);

    List<ExtensibleConfigEntity> listAll();

    List<ExtensibleConfigEntity> queryByType(String type);

    List<ExtensibleConfigEntity> queryByTypeAndTag(String type, String... tagSpecs);

    List<ExtensibleConfigEntity> queryByTag(String... tagSpecs);

}
