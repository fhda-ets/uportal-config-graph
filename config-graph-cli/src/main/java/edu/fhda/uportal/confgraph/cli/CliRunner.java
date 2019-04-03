package edu.fhda.uportal.confgraph.cli;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import okhttp3.OkHttpClient;
import org.fusesource.jansi.AnsiConsole;
import picocli.CommandLine;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Properties;


/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class CliRunner {

    public static void main(String[] args) {
        // Enable ANSI coloring
        AnsiConsole.systemInstall();

        // Build command line
        CommandLine cmdLine = new CommandLine(new RootCommand());

        // Execute top-level and sub-commands
        cmdLine.parseWithHandler(new CommandLine.RunAll().andExit(0), args);
    }

    @CommandLine.Command(
        name = "configgraph",
        subcommands = {
            ImportFile.class,
            ImportDirectory.class
        })
    static class RootCommand implements Runnable {

        @CommandLine.Option(
            names = "--portal-home",
            defaultValue = "config")
        Path portalHome;

        // Internal properties
        String configGraphUrl;
        OkHttpClient httpClient = new OkHttpClient();
        Properties portalProperties = new Properties();
        String jwtSignedKey;

        @Override
        public void run() {
            try {
                // Resolve uPortal.properties
                Path uportalProperties = portalHome.resolve("uPortal.properties");

                // Load properties
                try(BufferedReader reader = Files.newBufferedReader(uportalProperties)) {
                    portalProperties.load(reader);
                }

                // Get configured config graph URL for API calls
                configGraphUrl = portalProperties.getProperty("edu.fhda.confgraph.url");
                System.out.format("Using config graph URL %s/admin/import%n", configGraphUrl);

                // Create JWT token signing key (JJWT validates keys with the assumption that they are Base64 encoded)
                String jwtSignatureKey = portalProperties.getProperty("org.apereo.portal.soffit.jwt.signatureKey");
                byte[] decodedKey = Base64.getDecoder().decode(jwtSignatureKey.getBytes());
                SecretKey jwtSecretKey = Keys.hmacShaKeyFor(decodedKey);
                jwtSignedKey = Jwts
                    .builder()
                    .setSubject("admin")
                    .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                    .compact();
            }
            catch(Exception error) {
                throw new RuntimeException(error);
            }
        }

    }

}
