package edu.fhda.uportal.confgraph.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Basic implementation of a servlet filter to verify that all incoming requests are authenticated with properly signed
 * JWT token using the secret configured for uPortal. Optional support for subject verification for access restriction
 * if even a valid signed key is presented.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class JwtAuthenticationFilter implements Filter {

    private static final Logger log = LogManager.getLogger();

    private JwtParser jwtParser;
    private String requiredSubject = null;

    /**
     * Create a new filter instance without subject verification.
     * @param jwtParser Pre-configured JJWT parser
     */
    public JwtAuthenticationFilter(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    /**
     * Create a new filter instance with subject verification.
     * @param jwtParser Pre-configured JJWT parser
     * @param requiredSubject Value expected for subject in order to clear authentication
     */
    public JwtAuthenticationFilter(JwtParser jwtParser, String requiredSubject) {
        this.jwtParser = jwtParser;
        this.requiredSubject = requiredSubject;
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Get HTTP objects
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get authorization header
        String header = httpRequest.getHeader("Authorization");
        log.debug("Authorization header '{}'", header);

        // Validate the possible presence of a token
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ServletException("No JWT token found in request headers");
        }
        log.debug("Identified possible candidate for authorization header");

        // Extract just the token
        String rawToken = header.substring(7);

        // Parse claims from token
        try {
            Claims claims = jwtParser
                .parseClaimsJws(rawToken)
                .getBody();
            
            log.debug("Claims parsed successfully {}", claims);

            // Should the subject be validated?
            if(requiredSubject != null) {
                if(!(claims.getSubject().equals(requiredSubject))) {
                    httpResponse.sendError(401, "Unauthorized subject");
                    return;
                }
            }

            // Attach claims to HTTP request
            request.setAttribute("jwt-claims", claims);

            // If we made it here, JWT was validated successfully
            chain.doFilter(request, response);
        }
        catch(JwtException jwtException) {
            httpResponse.sendError(401, "Invalid JWT");
            log.error("Invalid JWT", jwtException);
        }
    }

}
