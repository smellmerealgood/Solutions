package com.solutions.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageUpdater;
import org.javacord.api.event.message.MessageCreateEvent;

import com.solutions.Solutions;
import com.solutions.utilities.Markdown;
import com.solutions.utilities.Responses;

public class Quote {
	public static final List<String> names = new ArrayList<String>(
			Arrays.asList("quote"));
	public static final String parameters = "text or attachment",
			description = "Quotes a person and puts it into the quotes group chat";

	public Quote(MessageCreateEvent event, Message sentMessage, String quote,
			List<MessageAttachment> attachments) {
		Message message = event.getMessage();

		new MessageUpdater(sentMessage).appendCode("ansi", Markdown.ANSI(
				"Who said this quote?\nMention the user(s) or provide text input",
				Markdown.CYAN)).replaceMessage();

		Object[] wrapper = {"", false};

		event.getChannel().addMessageCreateListener(input -> {
			if (!(boolean) wrapper[1]
					&& input.getMessageAuthor().getIdAsString()
							.equals(event.getMessageAuthor().getIdAsString())) {
				wrapper[0] = input.getMessageContent();

				MessageBuilder mb = new MessageBuilder()
						.copy(event.getMessage()).removeContent()
						.setContent(input.getMessageContent() + " at ")
						.appendTimestamp(message.getCreationTimestamp());

				if (!quote.isEmpty()) {
					mb.append("\n\n>>> " + quote);
				}

				mb.send(event.getApi()
						.getTextChannelById(Solutions.CHANNEL_QUOTES).get())
						.join();

				wrapper[1] = true;
				return;
			}
		}).removeAfter(1, TimeUnit.MINUTES).addRemoveHandler(new Runnable() {
			public void run() {
				if (((String) wrapper[0]).isEmpty()) {
					Responses.tooLongToRespond(message, false);
				}
			}
		});
	}
}