package edu.fhda.uportal.confgraph;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.crypto.SecretKey;
import java.util.Base64;

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

    static String signedJwt;

    static void setSignedJwt(String jwt) {
        signedJwt = jwt;
    }

    @Test
    @DisplayName("Generate signed JWT")
    @Order(1)
    void createSignedJwt() {
        // Create JWT token signing key
        String jwtSignatureKey = applicationContext
            .getEnvironment()
            .getProperty("org.apereo.portal.soffit.jwt.signatureKey");
        
        System.out.format("Using secret key %s%n", jwtSignatureKey);

        byte[] decodedKey = Base64.getDecoder().decode(jwtSignatureKey.getBytes());

        SecretKey jwtSecretKey = Keys.hmacShaKeyFor(decodedKey);

        setSignedJwt(Jwts
            .builder()
            .setSubject("admin")
            .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
            .compact());

        System.out.format("Generated JWT: %s%n", signedJwt);
    }

    @Test
    @DisplayName("Verify signed JWT")
    @Order(2)
    void verifySignedJwt() {
        // Parse
        jwtParser.parseClaimsJws(signedJwt);
    }

}
