package org.coursera.capstone.server;

import org.coursera.capstone.server.auth.OAuth2SecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// Inject automatically the dependencies marked as @Autowired
@EnableAutoConfiguration
// This object is the Configuration for the application
@Configuration
// Scan packages for controllers
@ComponentScan
// Include OAuth2SecurityConfiguration as part of the configuration
@Import(OAuth2SecurityConfiguration.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
