package edu.fhda.uportal.confgraph;

import edu.fhda.uportal.confgraph.web.security.JwtAuthenticationFilter;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.nio.charset.Charset;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "edu.fhda.uportal.confgraph.impl.jpa")
@EnableTransactionManagement
@PropertySource(value = "file:${portal.home}/uPortal.properties", ignoreResourceNotFound = true)
@ServletComponentScan
public class SpringConfig {
    
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> adminRoutesFilterRegistration(
        @Autowired JwtParser jwtParser){
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtParser, "admin"));
        registrationBean.setName("adminRoutesJwtFilter");
        registrationBean.addUrlPatterns("/admin/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> endUserRoutesFilterRegistration(
        @Autowired JwtParser jwtParser){
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtParser));
        registrationBean.setName("endUserRoutesJwtFilter");
        registrationBean.addUrlPatterns("/graph/*");
        return registrationBean;
    }

    @Bean
    public JwtParser jwtParser(@Value("${org.apereo.portal.soffit.jwt.signatureKey:NOTSECURE}") String jwtKey) {
        return Jwts
            .parser()
            .setSigningKey(jwtKey.getBytes(Charset.defaultCharset()));
    }

}
