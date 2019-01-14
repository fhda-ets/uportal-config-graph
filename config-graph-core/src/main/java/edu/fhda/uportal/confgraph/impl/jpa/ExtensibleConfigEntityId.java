package edu.fhda.uportal.confgraph.impl.jpa;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtensibleConfigEntityId that = (ExtensibleConfigEntityId) o;
        return Objects.equal(fname, that.fname) &&
            Objects.equal(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fname, type);
    }

    @Override
    public String toString() {
        return MoreObjects
            .toStringHelper(this)
                .add("fname", fname)
                .add("type", type)
                .toString();
    }
    
}
