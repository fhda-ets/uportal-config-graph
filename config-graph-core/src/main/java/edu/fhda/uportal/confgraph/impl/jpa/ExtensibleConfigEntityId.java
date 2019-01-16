package edu.fhda.uportal.confgraph.impl.jpa;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Composite primary key for entity objects.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class ExtensibleConfigEntityId implements Serializable {

    private String fname;
    private String type;

    public ExtensibleConfigEntityId() {
    }

    public ExtensibleConfigEntityId(String fname, String type) {
        this.fname = fname;
        this.type = type;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        ExtensibleConfigEntityId that = (ExtensibleConfigEntityId) other;
        return Objects.equal(fname, that.fname) &&
            Objects.equal(type, that.type);
    }

    /**
     * Implemented using Guava.
     * @return Hash code based on type and fname property values.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(fname, type);
    }

    /**
     * Implemented using Guava.
     * @return Inspection string composed of type and fname property values.
     */
    @Override
    public String toString() {
        return MoreObjects
            .toStringHelper(this)
                .add("fname", fname)
                .add("type", type)
                .toString();
    }
    
}
