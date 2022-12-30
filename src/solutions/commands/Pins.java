//make it so the actual files are sent; not the links

package solutions.commands;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.event.message.MessageCreateEvent;

import com.vdurmont.emoji.EmojiParser;

import solutions.utilities.Date;

public class Pins {
	public Pins(MessageCreateEvent event) {
		TextChannel channel = event.getChannel();

		Object[] pins = null;

		try {
			pins = channel.getPins().get().descendingSet().toArray();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		if (pins.length == 0) {
			channel.sendMessage("```fix\nThere are no pins in this channel```");
			return;
		}

		List<String> messageIds = new ArrayList<String>();

		for (int i = 0; i < pins.length; i++) {
			messageIds.add(pins[i].toString().substring(13, 32));
		}

		var wrapper = new Object() {
			int iteration = 0;
		};

		Message sentMessage = getPin(channel, messageIds, wrapper.iteration, -1).getMessage();

		sentMessage.addReactions(
				new String[] { EmojiParser.parseToUnicode(":rewind:"), EmojiParser.parseToUnicode(":arrow_backward:"),
						EmojiParser.parseToUnicode(":arrow_forward:"), EmojiParser.parseToUnicode(":fast_forward:") })
				.thenAccept(reaction -> {
					sentMessage.addReactionAddListener(addReaction -> {
						wrapper.iteration = onReaction(addReaction.getEmoji(), channel, messageIds, wrapper.iteration,
								sentMessage);
					}).removeAfter(30, TimeUnit.MINUTES);

					sentMessage.addReactionRemoveListener(removeReaction -> {
						wrapper.iteration = onReaction(removeReaction.getEmoji(), channel, messageIds,
								wrapper.iteration, sentMessage);
					}).removeAfter(30, TimeUnit.MINUTES);
				});
	}

	public int onReaction(Emoji emoji, TextChannel channel, List<String> messageIds, int iteration,
			Message currentMessage) {
		if (emoji.equalsEmoji(EmojiParser.parseToUnicode(":rewind:"))) {
			return Pins.getPin(channel, messageIds, 0, currentMessage.getId()).getIteration();
		} else if (emoji.equalsEmoji(EmojiParser.parseToUnicode(":arrow_backward:"))) {
			return Pins.getPin(channel, messageIds, iteration - 1, currentMessage.getId()).getIteration();
		} else if (emoji.equalsEmoji(EmojiParser.parseToUnicode(":arrow_forward:"))) {
			return Pins.getPin(channel, messageIds, iteration + 1, currentMessage.getId()).getIteration();
		} else if (emoji.equalsEmoji(EmojiParser.parseToUnicode(":fast_forward:"))) {
			return Pins.getPin(channel, messageIds, messageIds.size() - 1, currentMessage.getId()).getIteration();
		}

		return iteration;
	}

	public static Pair getPin(TextChannel channel, List<String> messageIds, int iteration, long messageId) {
		if (iteration < 0) {
			iteration = 0;
		} else if (iteration > messageIds.size() - 1) {
			iteration = messageIds.size() - 1;
		}

		Message current = channel.getMessageById(messageIds.get(iteration)).join();

		List<MessageAttachment> attachments = current.getAttachments();
		List<URL> URLs = new ArrayList<URL>();

		for (MessageAttachment attachment : attachments) {
			URLs.add(attachment.getUrl());
		}

		String message = "\n```fix\n" + current.getAuthor().getDiscriminatedName() + "\n"
				+ Date.convertDate(current.getCreationTimestamp()) + "\n" + (iteration + 1) + "/" + messageIds.size()
				+ "```\n" + current.getContent();

		for (URL url : URLs) {
			message += "\n" + url;
		}

		if (messageId == -1) {
			return new Pair(channel.sendMessage(message).join(), iteration);
		} else {
			return new Pair(channel.getMessageById(messageId).join().edit(message).join(), iteration);
		}
	}
}

class Pair {
	Message MESSAGE;
	int ITERATION;

	public Pair(Message message, int iteration) {
		MESSAGE = message;
		ITERATION = iteration;
	}

	public Message getMessage() {
		return MESSAGE;
	}

	public int getIteration() {
		return ITERATION;
	}
}