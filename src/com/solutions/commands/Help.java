package com.solutions.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageUpdater;
import org.javacord.api.event.message.MessageCreateEvent;

import com.solutions.utilities.Markdown;

public class Help {
	public static final List<String> names = new ArrayList<String>(
			Arrays.asList("help", "?"));
	public static final String parameters = "",
			description = "Displays a list of commands for this bot";

	public Help(MessageCreateEvent event, Message sentMessage)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		String helpMessage = Markdown.ANSI("Commands\n", Markdown.BOLD,
				Markdown.RED);
		
		try {
			for (Object object : Files.list(Paths.get(System.getProperty("user.dir")
					+ "/src/com/solutions/commands")).toArray()) {
				Class<?> currentClass = null;

				Path file = (Path) object;

				try {
					currentClass = Class.forName("com.solutions.commands."
							+ file.getFileName().toString().substring(0, file
									.getFileName().toString().lastIndexOf(".")));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				helpMessage += "\t"
						+ Markdown.ANSI(currentClass.getSimpleName().toLowerCase(),
								Markdown.BOLD, Markdown.CYAN);

				String parameters = (String) currentClass
						.getDeclaredField("parameters").get(this);

				if (!parameters.isEmpty()) {
					helpMessage += Markdown.ANSI(" <", Markdown.GRAY)
							+ Markdown.ANSI(parameters, Markdown.RED)
							+ Markdown.ANSI(">", Markdown.GRAY);
				}

				@SuppressWarnings("unchecked")
				List<String> names = (List<String>) currentClass
						.getDeclaredField("names").get(this);

				if (names.size() > 1) {
					helpMessage += " (aliases: ";

					for (int i = 1; i < names.size(); i++) {
						helpMessage += Markdown.ANSI(names.get(i), Markdown.CYAN);

						if (i != names.size() - 1) {
							helpMessage += Markdown.ANSI(", ", Markdown.CYAN);
						}
					}

					helpMessage += ")";
				}

				helpMessage += "\n\t\t- "
						+ Markdown.ANSI(
								(String) currentClass
										.getDeclaredField("description").get(this),
								Markdown.BLUE)
						+ "\n\n";
			}
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException | IOException e) {
			e.printStackTrace();
			return;
		}

		new MessageUpdater(sentMessage).appendCode("ansi", helpMessage)
				.replaceMessage().join();
	}
}