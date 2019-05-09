package ca.tunestumbler.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ca.tunestumbler.api.security.ApplicationProperties;

@SpringBootApplication
public class TunestumblerWrapperForRedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(TunestumblerWrapperForRedditApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean(name="ApplicationProperties")
	public ApplicationProperties applicationProperties() {
		return new ApplicationProperties();
	}
	
}
