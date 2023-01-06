package com.solutions.commands;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageUpdater;
import org.javacord.api.event.message.MessageCreateEvent;

public class Help {
	public Help(MessageCreateEvent event, Message sentMessage) {
		new MessageUpdater(sentMessage).appendCode("fix", "Commands\n\t• help (aliases: ?)"
				+ "\n\t\t- Displays this message" + "\n\t• pins (aliases: pin, listpins, listpin, pinslist, pinlist)"
				+ "\n\t\t- Displays this channel's pins"
				+ "\n\t• quote <text or attachment>\n\t\t- Quotes a person and puts it into the quotes group chat\n\t• removequote <message id or link>\n\t\t- Removes a quote from the quotes group chat")
				.replaceMessage();
	}
}