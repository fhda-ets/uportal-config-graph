package edu.fhda.uportal.confgraph;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import edu.fhda.uportal.confgraph.util.YamlPropertySourceFactory;
import edu.fhda.uportal.confgraph.web.security.JwtAuthenticationFilter;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;

/**
 * Spring application definition.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "edu.fhda.uportal.confgraph.impl.jpa")
@EnableTransactionManagement
@PropertySource(value = "file:${portal.home}/uPortal.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:${portal.home}/config-graph.yml", ignoreResourceNotFound = true, factory = YamlPropertySourceFactory.class)
@ServletComponentScan
public class SpringConfig {

    private static final Logger log = LogManager.getLogger();

    @Autowired LocalContainerEntityManagerFactoryBean entityManagerFactoryBean;

    /**
     * Create and configure a JWT authentication filter for /admin/* API routes with subject verification.
     * @param jwtParser Inject an preconfigured instance of a JJWT parser
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> adminRoutesFilterRegistration(
        @Autowired JwtParser jwtParser){
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtParser, "admin"));
        registrationBean.setName("adminRoutesJwtFilter");
        registrationBean.addUrlPatterns("/admin/*");
        return registrationBean;
    }

    /**
     * Create and configure a JWT authentication filter for /graph/* routes for portal end users.
     * @param jwtParser Inject an preconfigured instance of a JJWT parser
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> endUserRoutesFilterRegistration(
        @Autowired JwtParser jwtParser){
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtParser));
        registrationBean.setName("endUserRoutesJwtFilter");
        registrationBean.addUrlPatterns("/graph/*");
        return registrationBean;
    }

    /**
     * Configure a JJWT parser using the existing open ID secret taken from <code>uPortal.properties</code>.
     * @param jwtKey Inject the <code>org.apereo.portal.soffit.jwt.signatureKey</code> property.
     */
    @Bean
    public JwtParser jwtParser(
        @Value("${org.apereo.portal.soffit.jwt.signatureKey:NOTSECURE}") String jwtKey,
        @Value("${config-graph.jwt.signing-algorithm:HS256}") String jwtSigningAlgorithm) {

        log.debug("Setting up JWT parser secretKey={}", jwtKey);
        
        // Generate secret key object
        Key signingKey =
            new SecretKeySpec(
                jwtKey.getBytes(
                    Charset.defaultCharset()),
                    SignatureAlgorithm.forName(jwtSigningAlgorithm).getJcaName());

        // Create parser
        return Jwts
            .parser()
            .setSigningKey(signingKey);
    }

    @Bean("jacksonJsonMapper")
    @Primary
    public ObjectMapper jacksonJsonMapper() {
        return new ObjectMapper();
    }

    @Bean("jacksonYamlMapper")
    public ObjectMapper jacksonYamlMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

}
