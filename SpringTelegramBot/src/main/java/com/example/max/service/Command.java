package com.example.max.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Command {

	SendMessage getCommand(long chatId);
}
