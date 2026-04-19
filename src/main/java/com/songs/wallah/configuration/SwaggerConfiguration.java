package com.songs.wallah.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfiguration {

	@Bean
	OpenAPI myConfiguration() {
		// customizing swagger.html
		// OpenAPI has fields info,tag,extension,security,path etc.
		OpenAPI openAPI = new OpenAPI();
		Info info = new Info();
		// setting up info
		// INFO => title, description, contact, Terms of service, summary, version
		info.setTitle("Song Wallah");
		info.setDescription("Hear whatever you love, from Moj Masti to Devotional all songs available!");
		// Contact ===> name, url ,email
		// Contact details of owner :-
		Contact contact = new Contact();
		contact.setName("Vedant Maheshwari");
		contact.setEmail("zippyjese@gmail.com");
		contact.setUrl("https://github.com/JustTheGreenPanther28");
		info.setContact(contact);
		info.setSummary(
				"Make songs favorite, create public/private Playlist & search songs on the basis of your need!");
		info.setVersion("4.0.1");
		openAPI.setInfo(info);

		// setting up tags
		// Ordering of tag
		// In latest version this is gone
//		openAPI.setTags(Arrays.asList(new Tag().name("User APIs"),new Tag().name("Address APIs")));

		// server => url,description,variable
		openAPI.setServers(Arrays.asList(new Server().url("https://music-apis-eokm.onrender.com").description("Public"),
				new Server().url("http://localhost:8080").description("Local")));

		// setting up TOKEN TYPE VERIFICATION
//		SecurityScheme jwtScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
//				.bearerFormat("JWT");

//		openAPI.components(new Components().addSecuritySchemes("bearerAuth", jwtScheme))
////				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
		return openAPI;
	}
}
