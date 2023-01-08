package com.solutions.utilities;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageUpdater;

public class Responses {
	public final static String ERROR = Markdown.ANSI("Error", Markdown.BOLD, Markdown.RED),
			UNKNOWN_COMMAND = Markdown.ANSI("Unknown command\nFor help do \"sol help\"", Markdown.BOLD, Markdown.RED),
			TOO_LONG_TO_RESPOND = Markdown.ANSI("You took too long to respond!", Markdown.BOLD, Markdown.RED),
			WORKING_ON_IT = Markdown.ANSI("Working on it...", Markdown.BOLD, Markdown.CYAN),
			UNKNOWN_ARGUMENTS = Markdown.ANSI("Unknown arguments\nFor help do \"sol help\"", Markdown.BOLD,
					Markdown.RED);

	public static Message error(Message message, boolean edit) {
		String response = Markdown.ANSI(ERROR + "\nSomething went wrong", Markdown.BOLD, Markdown.RED);

		if (edit) {
			return new MessageUpdater(message).appendCode("ansi", response).replaceMessage().join();
		} else {
			return new MessageBuilder().replyTo(message).appendCode("ansi", response).send(message.getChannel()).join();
		}
	}

	public static Message error(Message message, boolean edit, String reason) {
		String response = Markdown.ANSI(ERROR + "\n" + reason, Markdown.BOLD, Markdown.RED);

		if (edit) {
			return new MessageUpdater(message).appendCode("ansi", response).replaceMessage().join();
		} else {
			return new MessageBuilder().replyTo(message).appendCode("ansi", response).send(message.getChannel()).join();
		}
	}

	public static Message unknownCommand(Message message, boolean edit) {
		String response = Markdown.ANSI(UNKNOWN_COMMAND, Markdown.BOLD, Markdown.RED);

		if (edit) {
			return new MessageUpdater(message).appendCode("ansi", response).replaceMessage().join();
		} else {
			return new MessageBuilder().replyTo(message).appendCode("ansi", response).send(message.getChannel()).join();
		}
	}

	public static Message tooLongToRespond(Message message, boolean edit) {
		String response = Markdown.ANSI(TOO_LONG_TO_RESPOND, Markdown.BOLD, Markdown.RED);

		if (edit) {
			return new MessageUpdater(message).appendCode("ansi", response).replaceMessage().join();
		} else {
			return new MessageBuilder().replyTo(message).appendCode("ansi", response).send(message.getChannel()).join();
		}
	}

	public static Message workingOnIt(Message message, boolean edit) {
		String response = Markdown.ANSI(WORKING_ON_IT, Markdown.BOLD, Markdown.CYAN);

		if (edit) {
			return new MessageUpdater(message).appendCode("ansi", response).replaceMessage().join();
		} else {
			return new MessageBuilder().replyTo(message).appendCode("ansi", response).send(message.getChannel()).join();
		}
	}

	public static Message unknownArguments(Message message, boolean edit) {
		String response = Markdown.ANSI(UNKNOWN_ARGUMENTS, Markdown.BOLD, Markdown.CYAN);

		if (edit) {
			return new MessageUpdater(message).appendCode("ansi", response).replaceMessage().join();
		} else {
			return new MessageBuilder().replyTo(message).appendCode("ansi", response).send(message.getChannel()).join();
		}
	}
}
