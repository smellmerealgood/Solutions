package com.solutions;

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

import com.solutions.commands.Help;
import com.solutions.commands.Pins;
import com.solutions.commands.Quote;
import com.solutions.commands.RemoveQuote;
import com.solutions.utilities.Responses;

public class Solutions {
	public static void main(String[] args)
			throws LoginException, InterruptedException, ExecutionException, IOException {
		DiscordApi api = new DiscordApiBuilder().setAccountType(AccountType.CLIENT).setToken(System.getenv("TOKEN"))
				.login().join();
		api.updateActivity(ActivityType.PLAYING, "sol help");

		api.addMessageCreateListener(event -> {
			String raw = event.getMessage().getContent().toLowerCase();
			String[] splitRaw = raw.split(" ");

			if (!event.getMessageAuthor().asUser().get().getDiscriminatedName()
					.equals(event.getApi().getYourself().getDiscriminatedName()) && splitRaw[0].equals("sol")) {
				Message sentMessage = Responses.workingOnIt(event.getMessage(), false);

				event.getChannel().type();

				if (splitRaw[1].equals("help") || splitRaw[1].equals("?")) {
					new Help(event, sentMessage);
				} else if (splitRaw[1].equals("pins") || splitRaw[1].equals("pin") || splitRaw[1].equals("listpins")
						|| splitRaw[1].equals("listpin") || splitRaw[1].equals("pinslist")
						|| splitRaw[1].equals("pinlist")) {
					new Pins(event, sentMessage);
				} else if (splitRaw[1].equals("quote")) {
					if (splitRaw.length > 2 || !event.getMessageAttachments().isEmpty()) {
						List<String> arguments = new LinkedList<String>(Arrays.asList(splitRaw));

						arguments.remove(0);
						arguments.remove(0);

						new Quote(event, sentMessage, String.join(" ", arguments), event.getMessageAttachments());
					}
				} else if (splitRaw[1].equals("removequote")) {
					new RemoveQuote(event, sentMessage, splitRaw[2]);
				} else {
					Responses.unknownCommand(sentMessage, true);
				}
			}
		});
	}
}