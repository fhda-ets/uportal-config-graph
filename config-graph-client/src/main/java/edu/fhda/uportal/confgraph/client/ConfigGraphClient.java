package edu.fhda.uportal.confgraph.client;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * WIP - 1/14/2018
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class ConfigGraphClient {

    private final static int cacheSize = 32 * 1024 * 1024; // 32 MiB

    // HTTP objects
    private final Cache httpCache;
    private final OkHttpClient httpClient;

    // Portal properties
    private final Properties portalProperties = new Properties();

    public ConfigGraphClient() {
        try {
            // Create local HTTP cache
            httpCache = new Cache(
                Files.createTempDirectory("config-graph-client").toFile(),
                cacheSize);

            // Build HTTP client to use cache
            httpClient = new OkHttpClient.Builder()
                .cache(httpCache)
                .build();

            // Is a portal.home system property available?
            String portalHome = System.getProperty("portal.home");
            if(portalHome != null) {
                // Build path to uPortal properties
                Path pathPortalProperties = Paths.get(portalHome, "uPortal.properties");

                // Load properties from local file
                try (BufferedReader propertiesReader = Files.newBufferedReader(pathPortalProperties)) {
                    portalProperties.load(propertiesReader);
                }
            }
        }
        catch(Exception error) {
            throw new RuntimeException(error);
        }
    }
}
