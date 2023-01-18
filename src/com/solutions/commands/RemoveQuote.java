package com.solutions.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageUpdater;
import org.javacord.api.event.message.MessageCreateEvent;

import com.solutions.utilities.Markdown;
import com.solutions.utilities.Responses;

public class RemoveQuote {
	public static final List<String> names = new ArrayList<String>(
			Arrays.asList("removequote"));
	public static final String parameters = "message id or link",
			description = "Removes a quote from the quotes group chat";

	public RemoveQuote(MessageCreateEvent event, Message sentMessage,
			String messageToRemove) throws IllegalArgumentException,
			InterruptedException, ExecutionException {
		int lastSlash = messageToRemove.lastIndexOf("/") + 1;

		if (lastSlash != -1) {
			messageToRemove = messageToRemove.substring(lastSlash,
					messageToRemove.length());
		}

		event.getApi().getTextChannelById("1040832617773285376").get()
				.deleteMessages(new String[]{messageToRemove})
				.exceptionally(e -> {
					Responses.error(sentMessage, true,
							"Could not delete message\nMaybe the arguments provided were invalid or the message was not sent by this bot");
					return null;
				}).thenAccept(complete -> {
					new MessageUpdater(sentMessage).appendCode("ansi", Markdown
							.ANSI("Successfully removed quote", Markdown.CYAN))
							.replaceMessage();
				});

	}
}
