//make >>> not appear when there is no text

package solutions.commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import solutions.utilities.Responses;

public class Quote {
	public Quote(MessageCreateEvent event, Message sentMessage, String quote, List<MessageAttachment> attachments) {
		TextChannel channel = event.getChannel();
		Message message = event.getMessage();

		sentMessage.edit("```fix\nWho said this quote?\nMention the user(s) or provide text input```");

		var wrapper = new Object() {
			String authors = "";
			boolean gotResponse = false;
		};

		event.getChannel().addMessageCreateListener(input -> {
			if (!wrapper.gotResponse
					&& input.getMessageAuthor().getIdAsString().equals(event.getMessageAuthor().getIdAsString())) {
				wrapper.authors = input.getMessageContent();

				MessageBuilder mb = new MessageBuilder().copy(event.getMessage()).removeContent()
						.setContent(input.getMessageContent() + " at ").appendTimestamp(message.getCreationTimestamp());

				if (!quote.isBlank()) {
					mb.append("\n\n>>> " + quote);
				}

				mb.send(event.getApi().getTextChannelById("1040832617773285376").get()).join();

				wrapper.gotResponse = true;
				return;
			}
		}).removeAfter(1, TimeUnit.MINUTES).addRemoveHandler(new Runnable() {
			public void run() {
				if (wrapper.authors.isBlank()) {
					Responses.tooLongToRespond(message, false);
				}
			}
		});
	}
}