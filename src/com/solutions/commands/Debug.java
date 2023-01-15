package com.solutions.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageUpdater;
import org.javacord.api.event.message.MessageCreateEvent;

import com.solutions.utilities.Markdown;

public class Debug {
	public static final String parameters = "";
	public static final List<String> names = new ArrayList<String>(Arrays.asList("debug"));
	public static final String description = "Displays current system and JVM information";

	public Debug(MessageCreateEvent event, Message sentMessage) {
		final int mb = 1048576;
		long maxMemory = Runtime.getRuntime().maxMemory();

		new MessageUpdater(sentMessage)
				.appendCode("ansi",
						"Available processors (cores): "
								+ Markdown.ANSI("" + Runtime.getRuntime().availableProcessors(), Markdown.BLUE)
								+ "\nFree memory: "
								+ Markdown.ANSI(Runtime.getRuntime().freeMemory() / mb + " MB", Markdown.BLUE)
								+ "\nMaximum memory: "
								+ Markdown.ANSI((maxMemory == Long.MAX_VALUE ? "Unlimited" : maxMemory / mb + " MB"),
										Markdown.BLUE)
								+ "\nTotal memory available to JVM: "
								+ Markdown.ANSI(Runtime.getRuntime().totalMemory() / mb + " MB", Markdown.BLUE))
				.replaceMessage();
	}
}