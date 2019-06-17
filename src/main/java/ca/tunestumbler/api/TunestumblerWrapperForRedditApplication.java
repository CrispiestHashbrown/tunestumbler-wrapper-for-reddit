package ca.tunestumbler.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import ca.tunestumbler.api.security.ApplicationProperties;

@SpringBootApplication
public class TunestumblerWrapperForRedditApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(
			org.springframework.boot.builder.SpringApplicationBuilder application) {
		return application.sources(TunestumblerWrapperForRedditApplication.class);
	}

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

	@Bean(name = "ApplicationProperties")
	public ApplicationProperties applicationProperties() {
		return new ApplicationProperties();
	}

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}

}
