package edu.fhda.uportal.confgraph

import okhttp3.MediaType

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
class ContentType {

    public static final MediaType JSON =
        MediaType.get("application/json; charset=utf-8")

    public static final MediaType YAML =
        MediaType.get("application/x-yaml")

}
