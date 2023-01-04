package com.solutions.utilities;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageUpdater;

public class Responses {
	public final static String ERROR = "Error\nSomething went wrong",
			UNKNOWN_COMMAND = "Unknown command\nFor help do \"sol help\"",
			TOO_LONG_TO_RESPOND = "You took too long to respond!", WORKING_ON_IT = "Working on it...";

	public static Message error(Message message, boolean edit) {
		if (edit) {
			return new MessageUpdater(message).setContent("```fix\n" + ERROR + "```").replaceMessage().join();
		} else {
			return new MessageBuilder().replyTo(message).appendCode("fix", ERROR).send(message.getChannel()).join();
		}
	}

	public static Message unknownCommand(Message message, boolean edit) {
		if (edit) {
			return new MessageUpdater(message).setContent("```fix\n" + UNKNOWN_COMMAND + "```").replaceMessage().join();
		} else {
			return new MessageBuilder().replyTo(message).appendCode("fix", UNKNOWN_COMMAND).send(message.getChannel()).join();
		}
	}

	public static Message tooLongToRespond(Message message, boolean edit) {
		if (edit) {
			return new MessageUpdater(message).setContent("```fix\n" + TOO_LONG_TO_RESPOND + "```").replaceMessage().join();
		} else {
			return new MessageBuilder().replyTo(message).appendCode("fix", TOO_LONG_TO_RESPOND).send(message.getChannel()).join();
		}
	}

	public static Message workingOnIt(Message message, boolean edit) {
		if (edit) {
			return new MessageUpdater(message).setContent("```fix\n" + WORKING_ON_IT + "```").replaceMessage().join();
		} else {
			return new MessageBuilder().replyTo(message).appendCode("fix", WORKING_ON_IT).send(message.getChannel()).join();
		}
	}
}
