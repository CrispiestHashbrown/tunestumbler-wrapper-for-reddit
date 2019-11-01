package ca.tunestumbler.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping("/**")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedOrigins("http://www.tunestumbler.com", "https://www.tunestumbler.com", "https://www.reddit.com")
				.allowedHeaders("Authorization", "Cache-Control", "Content-Type", "X-Requested-With",
						"Access-Control-Allow-Headers", "Origin", "Accept", "User-Agent");
	}

}
