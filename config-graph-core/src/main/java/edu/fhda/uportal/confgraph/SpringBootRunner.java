package edu.fhda.uportal.confgraph;

import org.springframework.boot.SpringApplication;

/**
 * Runs an instance of config graph as a standalone Spring Boot application with embedded Tomcat.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
public class SpringBootRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfig.class, args);
    }

}
