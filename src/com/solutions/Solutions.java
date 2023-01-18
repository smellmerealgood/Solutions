package com.solutions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.message.Message;

import com.solutions.commands.Debug;
import com.solutions.commands.Help;
import com.solutions.commands.Pins;
import com.solutions.commands.Quote;
import com.solutions.commands.RemoveQuote;
import com.solutions.utilities.Responses;

public class Solutions {
	public static void main(String[] args) throws LoginException,
			InterruptedException, ExecutionException, IOException {
		System.out.println(new File(System.getProperty("user.dir")
				+ "\\src\\com\\solutions\\commands").getName());

		DiscordApi api = new DiscordApiBuilder()
				.setAccountType(AccountType.CLIENT)
				.setToken(System.getenv("TOKEN")).login().join();
		api.updateActivity(ActivityType.PLAYING, "sol help");

		api.addMessageCreateListener(event -> {
			String raw = event.getMessage().getContent().toLowerCase();
			String[] splitRaw = raw.split(" ");

			if (!event.getMessageAuthor().asUser().get().getDiscriminatedName()
					.equals(event.getApi().getYourself().getDiscriminatedName())
					&& splitRaw[0].equals("sol")) {
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
			}
		});
	}
}