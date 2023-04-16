package com.example.max.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.example.max.service.Command;

@Component
public class RegisterCommand implements Command {
	
	static final String YES_BUTTON = "YES_BUTTON";
	static final String NO_BUTTON = "NO_BUTTON";

	@Override
	public SendMessage getCommand(long chatId) {

		SendMessage message = new SendMessage();
		message.setChatId(chatId);
		message.setText("Do you want register?");
		
		InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
		List<InlineKeyboardButton> rowInLine = new ArrayList<>(); 
		
		InlineKeyboardButton yesButton = new InlineKeyboardButton();
		yesButton.setText("Yes");
		yesButton.setCallbackData(YES_BUTTON);
		
		InlineKeyboardButton noButton = new InlineKeyboardButton();
		noButton.setText("No");
		noButton.setCallbackData(NO_BUTTON);
		
		rowInLine.add(yesButton);
		rowInLine.add(noButton);
		
		rowsInLine.add(rowInLine);
		markupInLine.setKeyboard(rowsInLine);
		message.setReplyMarkup(markupInLine);
			
		return message;
	}

}
