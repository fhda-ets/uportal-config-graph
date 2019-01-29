package edu.fhda.uportal.confgraph;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fhda.uportal.confgraph.impl.jpa.ExtensibleConfigJpaEntity;
import edu.fhda.uportal.confgraph.impl.jpa.ExtensibleConfigRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@Component
public class StartupLoadFromDiskListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LogManager.getLogger();

    private static final Pattern regexJson = Pattern.compile("^.*[.]json", Pattern.CASE_INSENSITIVE);
    private static final Pattern regexYaml = Pattern.compile("^.*[.](yml|yaml)", Pattern.CASE_INSENSITIVE);

    @Autowired @Qualifier("jacksonJsonMapper") ObjectMapper jacksonJsonMapper;
    @Autowired @Qualifier("jacksonYamlMapper") ObjectMapper jacksonYamlMapper;
    @Autowired ExtensibleConfigRepository repository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Unpack application context
        ApplicationContext context = event.getApplicationContext();

        // Get paths variable from config
        String rawPaths = context
            .getEnvironment()
            .getProperty("config-graph.startup.disk-loader.paths");

        // Validate if anything is configured for the startup loader
        if(rawPaths == null) {
            // Stop here
            return;
        }

        // Parse and iterate paths
        Stream.of(rawPaths.split(";"))
            .map(Paths::get)
            .forEach(inputDirectory -> {
                // Walk directory
                try {
                    Files.walk(inputDirectory)
                        .filter(Files::isRegularFile)
                        .filter(StartupLoadFromDiskListener::isSupportedFile)
                        .forEach(this::performFileImport);
                }
                catch(Exception walkError) {
                    log.error("Failed to walk input directory", walkError);
                }
            });
    }

    private void performFileImport(Path inputFile) {
        try {
            // Evaluate and parse by file type
            Map<String, Object> payload = null;
            if(regexJson.matcher(inputFile.getFileName().toString()).matches()) {
                log.debug("Parsing JSON document {}", inputFile);
                payload = jacksonJsonMapper
                    .readValue(Files.readAllBytes(inputFile), HashMap.class);
            }
            else if(regexYaml.matcher(inputFile.getFileName().toString()).matches()) {
                log.debug("Parsing YAML document {}", inputFile);
                payload = jacksonYamlMapper
                    .readValue(Files.readAllBytes(inputFile), HashMap.class);
            }

            // Map into new entity
            ExtensibleConfigJpaEntity entity = new ExtensibleConfigJpaEntity(
                (String) payload.get("type"),
                (String) payload.get("fname"));

            if(payload.containsKey("acls")) {
                entity.setAcls((Map<String, List<String>>) payload.get("acls"));
            }

            if(payload.containsKey("graph")) {
                entity.setGraph((Map<String, Object>) payload.get("graph"));
            }

            if(payload.containsKey("tags")) {
                entity.setTags((Map<String, String>) payload.get("tags"));
            }

            // Persist entity into storage
            repository.save(entity);
            log.debug("Successfully imported new entity type={} fname={}", payload.get("type"), payload.get("fname"));
        }
        catch(Exception error) {
            log.error("Failed to import {} {}", inputFile, error);
        }
    }

    static boolean isSupportedFile(Path source) {
        if(regexJson.matcher(source.getFileName().toString()).matches()) {
            return Boolean.TRUE;
        }
        else if(regexYaml.matcher(source.getFileName().toString()).matches()) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

}
