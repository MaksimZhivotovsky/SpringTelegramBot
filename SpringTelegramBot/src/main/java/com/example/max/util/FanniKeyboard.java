package com.example.max.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component("fanniKeyboard")
public class FanniKeyboard implements Keyboard{

	@Override
	public ReplyKeyboardMarkup getKeyboard() { //ReplyKeyboardMarkup
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboardRows = new ArrayList<>();
		
		KeyboardRow row = new KeyboardRow();
		row.add("weater");
		row.add("get random joke");

		keyboardRows.add(row);
		
		row = new KeyboardRow();
		row.add("register");
		row.add("chek my data");
		row.add("delete my data");
		
		keyboardRows.add(row);
		keyboardMarkup.setKeyboard(keyboardRows);
		return keyboardMarkup;
	}

}
