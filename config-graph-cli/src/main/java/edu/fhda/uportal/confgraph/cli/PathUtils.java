package edu.fhda.uportal.confgraph.cli;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class PathUtils {

    private static final Pattern regexJson = Pattern.compile("^.*[.]json", Pattern.CASE_INSENSITIVE);

    private static final Pattern regexYaml = Pattern.compile("^.*[.](yml|yaml)", Pattern.CASE_INSENSITIVE);

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static final MediaType YAML = MediaType.get("application/x-yaml");

    public static RequestBody createHttpBody(Path source) throws IOException {
        if(regexJson.matcher(source.getFileName().toString()).matches()) {
            return RequestBody.create(JSON, Files.readAllBytes(source));
        }
        else if(regexYaml.matcher(source.getFileName().toString()).matches()) {
            return RequestBody.create(YAML, Files.readAllBytes(source));
        }

        throw new RuntimeException("Source file " + source.toString() + " has an unrecognized extension");
    }

    public static boolean isSupportedFile(Path source) {
        if(regexJson.matcher(source.getFileName().toString()).matches()) {
            return Boolean.TRUE;
        }
        else if(regexYaml.matcher(source.getFileName().toString()).matches()) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

}
