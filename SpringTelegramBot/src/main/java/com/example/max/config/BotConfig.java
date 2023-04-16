package com.example.max.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.Data;

@Data
@Configuration
@PropertySource("application.properties")
@EnableScheduling
public class BotConfig {

	@Value("${bot.name}")
	private String name;
	
	@Value("${bot.token}")
	private String token;
	
	@Value("${bot.owner}")
    private Long ownerId;
}
