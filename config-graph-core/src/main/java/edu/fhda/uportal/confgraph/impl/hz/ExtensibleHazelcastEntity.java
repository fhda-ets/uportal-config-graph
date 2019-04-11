package edu.fhda.uportal.confgraph.impl.hz;

import edu.fhda.uportal.confgraph.api.AbstractExtensibleConfigEntity;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class ExtensibleHazelcastEntity extends AbstractExtensibleConfigEntity {

    private String distributedMapKey;

    public ExtensibleHazelcastEntity(String type, String fname) {
        super(type, fname);
        this.distributedMapKey = type + ":" + fname;
    }

    public String getDistributedMapKey() {
        return this.distributedMapKey;
    }

}
