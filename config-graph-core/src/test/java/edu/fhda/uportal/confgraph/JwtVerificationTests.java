package edu.fhda.uportal.confgraph;

import io.jsonwebtoken.JwtParser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtVerificationTests {

    @Autowired ApplicationContext applicationContext;
    @Autowired JwtParser jwtParser;

    @Test
    @DisplayName("Validate signed JWT")
    @Order(1)
    void validateSignedJwt() {
        // Fetch sample JWT from config
        String jwtForTesting = applicationContext
            .getEnvironment()
            .getProperty("config-graph.tests.variables.jwt-token");

        // Parse
        jwtParser.parseClaimsJws(jwtForTesting);
    }

}
