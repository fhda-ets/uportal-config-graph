package edu.fhda.uportal.confgraph

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.options.Option

import javax.crypto.SecretKey
import javax.xml.ws.Action

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
abstract class AbstractImportTask extends DefaultTask {

    // Default parameters
    String pathPortalHome = "config"

    // Miscellaneous objects
    String configGraphUrl
    OkHttpClient httpClient = new OkHttpClient()
    String jwtSignedKey
    Properties portalProperties = new Properties()

    @Option(option = "portal-home", description = "Set the portal home directory")
    void setPathPortalHome(final String path) {
        this.pathPortalHome = path
    }

    static RequestBody createHttpBody(File source) {
        if(source.name ==~ /^.*[.]json/) {
            return RequestBody.create(ContentType.JSON, source)
        }
        else if(source.name ==~ /^.*[.](yml|yaml)/) {
            return RequestBody.create(ContentType.YAML, source)
        }

        throw new RuntimeException("Source file ${source.name} has an unrecognized extension")
    }

    static boolean isSupportedFile(File source) {
        if(source.name ==~ /^.*[.]json/) {
            return Boolean.TRUE
        }
        else if(source.name ==~ /^.*[.](yml|yaml)/) {
            return Boolean.TRUE
        }

        return Boolean.FALSE
    }

    @Action
    def action() {
        // Configure portal home
        File portalHome = new File(pathPortalHome)

        // Load uPortal.properties
        portalProperties.load(new File(portalHome, "uPortal.properties").newDataInputStream())

        // Get configured config graph URL for API calls
        configGraphUrl = portalProperties.getProperty("edu.fhda.confgraph.url")

        // Confgure JWT token signing key
        String jwtSignatureKey = portalProperties.getProperty("org.apereo.portal.soffit.jwt.signatureKey")
        SecretKey jwtSecretKey = Keys.hmacShaKeyFor(jwtSignatureKey.bytes)
        jwtSignedKey = Jwts
            .builder()
            .setSubject("admin")
            .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
            .compact()
    }

}
