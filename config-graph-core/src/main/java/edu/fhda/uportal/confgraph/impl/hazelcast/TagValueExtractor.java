package edu.fhda.uportal.confgraph.impl.hazelcast;

import com.hazelcast.query.extractor.ValueCollector;
import com.hazelcast.query.extractor.ValueExtractor;
import edu.fhda.uportal.confgraph.api.ExtensibleConfigEntity;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class TagValueExtractor extends ValueExtractor<ExtensibleConfigEntity, String> {

    @Override
    public void extract(ExtensibleConfigEntity target, String argument, ValueCollector collector) {
        // Iterate tags for indexing
        target
            .getTags()
            .forEach((key, value) -> collector.addObject(key + "=" + value));
    }

}
