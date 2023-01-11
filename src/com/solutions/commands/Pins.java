package com.solutions.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageUpdater;
import org.javacord.api.event.message.MessageCreateEvent;

import com.solutions.utilities.Markdown;
import com.solutions.utilities.Responses;
import com.vdurmont.emoji.EmojiParser;

public class Pins {
	public static final String parameters = "";
	public static final List<String> names = new ArrayList<String>(
			Arrays.asList("pins", "pin", "listpins", "listpin", "pinslist", "pinlist"));
	public static final String description = "Displays this channel's pins";

	public Pins(MessageCreateEvent event, Message sentMessage) {
		TextChannel channel = event.getChannel();
		Object[] pins = null;

		try {
			pins = channel.getPins().get().descendingSet().toArray();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		if (pins.length == 0) {
			new MessageUpdater(sentMessage)
					.appendCode("ansi", Markdown.ANSI("There are no pins in this channel", Markdown.CYAN))
					.replaceMessage();
			return;
		}

		List<String> messageIds = new ArrayList<String>();

		for (int i = 0; i < pins.length; i++) {
			messageIds.add(pins[i].toString().substring(13, 32));
		}

		int[] wrapper = { 0 };

		getPin(event, messageIds, wrapper[0], sentMessage);

		sentMessage.addReactions(
				new String[] { EmojiParser.parseToUnicode(":rewind:"), EmojiParser.parseToUnicode(":arrow_backward:"),
						EmojiParser.parseToUnicode(":arrow_forward:"), EmojiParser.parseToUnicode(":fast_forward:") })
				.thenAccept(reaction -> {
					sentMessage.addReactionAddListener(addReaction -> {
						if (!addReaction.getUserIdAsString()
								.equals(addReaction.getApi().getYourself().getIdAsString())) {
							wrapper[0] = onReaction(event, addReaction.getEmoji(), messageIds, wrapper[0], sentMessage);
						}
					}).removeAfter(30, TimeUnit.MINUTES);

					sentMessage.addReactionRemoveListener(removeReaction -> {
						if (!removeReaction.getUserIdAsString()
								.equals(removeReaction.getApi().getYourself().getIdAsString())) {
							wrapper[0] = onReaction(event, removeReaction.getEmoji(), messageIds, wrapper[0],
									sentMessage);
						}
					}).removeAfter(30, TimeUnit.MINUTES);
				});
	}

	private int onReaction(MessageCreateEvent event, Emoji emoji, List<String> messageIds, int iteration,
			Message sentMessage) {
		Responses.workingOnIt(sentMessage, true);

		if (emoji.equalsEmoji(EmojiParser.parseToUnicode(":rewind:"))) {
			return Pins.getPin(event, messageIds, 0, sentMessage);
		} else if (emoji.equalsEmoji(EmojiParser.parseToUnicode(":arrow_backward:"))) {
			return Pins.getPin(event, messageIds, iteration - 1, sentMessage);
		} else if (emoji.equalsEmoji(EmojiParser.parseToUnicode(":arrow_forward:"))) {
			return Pins.getPin(event, messageIds, iteration + 1, sentMessage);
		} else if (emoji.equalsEmoji(EmojiParser.parseToUnicode(":fast_forward:"))) {
			return Pins.getPin(event, messageIds, messageIds.size() - 1, sentMessage);
		}

		return iteration;
	}

	private static int getPin(MessageCreateEvent event, List<String> messageIds, int iteration, Message sentMessage) {
		if (iteration < 0) {
			iteration = 0;
		} else if (iteration > messageIds.size() - 1) {
			iteration = messageIds.size() - 1;
		}

		Message current = event.getChannel().getMessageById(messageIds.get(iteration)).join();

		String codeBlock = "`" + (iteration + 1) + "/" + messageIds.size() + "`\n";

		String currentContent = current.getContent();

		MessageUpdater mu = new MessageUpdater(sentMessage)
				.append(codeBlock + current.getUserAuthor().get().getMentionTag() + " at ")
				.appendTimestamp(current.getCreationTimestamp());

		if (!currentContent.isEmpty()) {
			mu.append("\n\n>>> " + currentContent);
		}

		for (MessageAttachment attachment : current.getAttachments()) {
			if (attachment.isSpoiler()) {
				mu.addAttachmentAsSpoiler(attachment.getUrl());
			} else {
				mu.addAttachment(attachment.getUrl());
			}
		}

		mu.replaceMessage().join();

		return iteration;

	}
}