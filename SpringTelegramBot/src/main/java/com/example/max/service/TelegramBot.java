package com.example.max.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.example.max.config.BotConfig;
import com.example.max.model.Ads;
import com.example.max.model.User;
import com.example.max.repository.AdsRepository;
import com.example.max.repository.UserRepository;
import com.example.max.util.Keyboard;
import com.vdurmont.emoji.EmojiParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

	static final String HELP_TEXT = "This bot is created for my cheerful mood\n"
			+ "HAHA\n\n"
			+ "Type /start to see welcome message\n"
			+ "Type /mydata to see your data stored\n"
			+ "Type /deletedata to see delete my data\n"
			+ "Type /register to see your registration\n"
			+ "Type /settin to see your preferences\n";
	
	static final String YES_BUTTON = "YES_BUTTON";
	static final String NO_BUTTON = "NO_BUTTON";
	static final String ERROR_TEXT = "Error occurred: ";
	
	private final BotConfig botConfig;
	private UserRepository userRepository;
	private Keyboard keyboard;
	private AdsRepository adsRepository;
	
	public TelegramBot(BotConfig botConfig, UserRepository userRepository,
			 @Qualifier("fanniKeyboard") Keyboard keyboard, AdsRepository adsRepository) {

		this.botConfig = botConfig;
		this.userRepository = userRepository;
		this.keyboard = keyboard;
		this.adsRepository = adsRepository;
		
		List<BotCommand> listOfCommands = new ArrayList<>();
		listOfCommands.add(new BotCommand("/start", "get a welcome message"));
		listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
		listOfCommands.add(new BotCommand("/deletedata", "delete my data"));
		listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
		listOfCommands.add(new BotCommand("/setting", "set your preferences"));
		listOfCommands.add(new BotCommand("/register", "your registration"));
		
		try {
			this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
		} catch (TelegramApiException e) {
			log.error("Error setting bot's command list: " + e.getMessage());
		}
	}

	@Override
	public String getBotUsername() {
		return botConfig.getName();
	}
	
	@Override
	public String getBotToken() {
		return botConfig.getToken();
	}
	
	@Override
	public void onUpdateReceived(Update update) {
		if(update.hasMessage() && update.getMessage().hasText()) {
			String messageText = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();
			
			if(messageText.contains("/send")  && botConfig.getOwnerId() == chatId) {
				String textToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
				Iterable<User> users = userRepository.findAll();
				for(User user : users) {
					prepareAndSendMessage(user.getId(), textToSend);
				}
			} 
			else {
				switch(messageText) {
					case "/start": startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
						registerUser(update.getMessage());	
						break;
					case "/help": prepareAndSendMessage(chatId, HELP_TEXT);
						break;
					case "/register": register(chatId);
						break;
					default: prepareAndSendMessage(chatId, "Sorry I'am dont");
				}
			}
		}
		else if (update.hasCallbackQuery()) {
			String callbeackData = update.getCallbackQuery().getData();
			long messageId = update.getCallbackQuery().getMessage().getMessageId();
			long chatId = update.getCallbackQuery().getMessage().getChatId();
			
			if (callbeackData.equals(YES_BUTTON)) {
				String text = "you pressed Yes button";
				executeEditMessageText(text,chatId, messageId);
			} 
			else if (callbeackData.equals(NO_BUTTON)) {
				String text = "you pressed No button";
				executeEditMessageText(text,chatId, messageId);
			}
		}
		
	}

	private void register(long chatId) {
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
		
		executeMessage(message);
	}

	private void registerUser(Message message) {
		if(userRepository.findById(message.getChatId()).isEmpty()) {
			long chatId = message.getChatId();
			Chat chat = message.getChat();
			
			User user = new User();
			user.setId(chatId);
			user.setFirstName(chat.getFirstName());
			user.setLastName(chat.getLastName());
			user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
		
			userRepository.save(user);
			log.info("user saved: " + user);
		}
	}

//	https://emojipedia.org/
	private void startCommandReceived(long chatId, String name) {
		String answer = EmojiParser.parseToUnicode("Hi, " + name + ", nice to meet you! " + ":blush:");
		log.info("Replaid to user " + name);
		sendMessage(chatId, answer);
	}

	private void sendMessage(long chatId, String textToSend) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));
		message.setText(textToSend);
		message.setReplyMarkup(keyboard.getKeyboard());
		
		executeMessage(message);
	}
	
	private void executeEditMessageText(String text, long chatId, long messageId) {
	
		EditMessageText message = new EditMessageText();
		message.setChatId(chatId); 
		message.setText(text);
		message.setMessageId((int) messageId );
		
		try {
			execute(message);
		} catch (TelegramApiException e) {
			log.error(ERROR_TEXT + e.getMessage());
		}
	}
	
	private void executeMessage(SendMessage message) {
		try {
			execute(message);
		} catch (TelegramApiException e) {
			log.error(ERROR_TEXT + e.getMessage());
		}
	}
	
	private void prepareAndSendMessage(long chatId, String textToSend) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));
		message.setText(textToSend);
		executeMessage(message);
	}
	
//	секунда, минута, час, дата(15), месяц, день недели(вторник)
	@Scheduled(cron = "${cron.scheduler}")
	private void sendAds() {
		
		Iterable<User> users = userRepository.findAll();
		Iterable<Ads> ads = adsRepository.findAll();
		
		for(Ads ad : ads) {
			for(User user : users) {
				prepareAndSendMessage(user.getId(), ad.getAd());
			}
		}
	}
}
