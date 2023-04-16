package com.example.max.config;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.example.max.service.TelegramBot;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotInitializer {

	private final TelegramBot bot;
	
	@EventListener({ContextRefreshedEvent.class})
	public void init() throws TelegramApiException {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		try {
			telegramBotsApi.registerBot(bot);
		} catch (TelegramApiException e) {
			log.error("Error occurred: " + e.getMessage());
		}
	}
}
