package com.example.max.service.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.example.max.service.Command;

import lombok.extern.slf4j.Slf4j;

@Component
public class HelpCommand implements Command {

	static final String HELP_TEXT = "This bot is created for my cheerful mood\n"
			+ "HAHA\n\n"
			+ "Type /start to see welcome message\n"
			+ "Type /mydata to see your data stored\n"
			+ "Type /deletedata to see delete my data\n"
			+ "Type /register to see your registration\n"
			+ "Type /settin to see your preferences\n";
	@Override
	public SendMessage getCommand(long chatId) {
		
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));
		message.setText(HELP_TEXT);
		return message;
	}
}
