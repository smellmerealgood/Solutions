package com.solutions;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;

import com.solutions.commands.Debug;
import com.solutions.commands.Help;
import com.solutions.commands.Pins;
import com.solutions.commands.Quote;
import com.solutions.commands.RemoveQuote;
import com.solutions.utilities.Responses;

public class Solutions {
	public static final long PID = new Random().nextLong();
	public static final String BOT_PREFIX = "sol",
			CHANNEL_MAIN = "1025957114671280179",
			CHANNEL_QUOTES = "1040832617773285376",
			CHANNEL_INSTANCE_CHECKER = "1067961290745724928";

	public static void main(String[] args) throws LoginException,
			InterruptedException, ExecutionException, IOException {
		DiscordApi api = new DiscordApiBuilder()
				.setAccountType(AccountType.CLIENT)
				.setToken(System.getenv("TOKEN")).login().join();
		api.updateActivity(ActivityType.PLAYING, BOT_PREFIX + " help");

		new MessageBuilder().setContent("pid " + PID)
				.send(api.getTextChannelById(CHANNEL_INSTANCE_CHECKER).get());

		new Countdown(api);

		api.addMessageCreateListener(event -> {
			String raw = event.getMessage().getContent().toLowerCase();
			String[] splitRaw = raw.split(" ");

			if (!event.getMessageAuthor().asUser().get().getDiscriminatedName()
					.equals(event.getApi().getYourself().getDiscriminatedName())
					&& splitRaw[0].equals(BOT_PREFIX)) {
				Message sentMessage = Responses.workingOnIt(event.getMessage(),
						false);

				if (splitRaw.length < 2) {
					Responses.unknownCommand(sentMessage, true);
					return;
				}

				if (Help.names.contains(splitRaw[1])) {
					try {
						new Help(event, sentMessage);
					} catch (NoSuchFieldException | SecurityException
							| IllegalArgumentException
							| IllegalAccessException e) {
						e.printStackTrace();
					}
				} else if (Pins.names.contains(splitRaw[1])) {
					new Pins(event, sentMessage);
				} else if (Quote.names.contains(splitRaw[1])) {
					if (splitRaw.length > 2
							|| !event.getMessageAttachments().isEmpty()) {
						List<String> arguments = new LinkedList<String>(
								Arrays.asList(splitRaw));

						arguments.remove(0);
						arguments.remove(0);

						new Quote(event, sentMessage,
								String.join(" ", arguments),
								event.getMessageAttachments());
					}
				} else if (RemoveQuote.names.contains(splitRaw[1])) {
					if (splitRaw.length > 2) {
						try {
							new RemoveQuote(event, sentMessage, splitRaw[2]);
						} catch (IllegalArgumentException | InterruptedException
								| ExecutionException e) {
							e.printStackTrace();
						}
					} else {
						Responses.unknownArguments(sentMessage, true);
					}
				} else if (Debug.names.contains(splitRaw[1])) {
					new Debug(event, sentMessage);
				} else {
					Responses.unknownCommand(sentMessage, true);
				}
			} else if (splitRaw[0].equals("pid")
					&& event.getMessageAuthor().asUser().get()
							.getDiscriminatedName()
							.equals(event.getApi().getYourself()
									.getDiscriminatedName())
					&& event.getChannel().getIdAsString()
							.equals(CHANNEL_INSTANCE_CHECKER)
					&& !splitRaw[1].equals(PID + "")) {
				System.out.println(
						"Detected extra instance running! Closing process #"
								+ PID);
				System.exit(0);
			}
		});
	}
}