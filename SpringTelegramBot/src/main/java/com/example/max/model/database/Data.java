package com.example.max.model.database;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.max.service.Command;
import com.example.max.service.impl.HelpCommand;
import com.example.max.service.impl.RegisterCommand;

@Component
public class Data {
	
	private HelpCommand helpCommand = new HelpCommand();
	private RegisterCommand registerCommand = new RegisterCommand();
//	private StartCommand startCommand = new StartCommand();

	public final static Map<String, Command> datas = new HashMap<>();
	
	public Data() {
		if (datas.isEmpty()) {
//			datas.put("/red", commandImpl);
//			datas.put("/start", startCommand);
			datas.put("/help", helpCommand);
			datas.put("/register", registerCommand);
//			datas.put("/red", commandImpl);
//			datas.put("/red", commandImpl);
		}
	}
}
