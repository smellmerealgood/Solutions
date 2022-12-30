package solutions.commands;

import org.javacord.api.event.message.MessageCreateEvent;

public class Help {
	public Help(MessageCreateEvent event) {
		event.getChannel().sendMessage(
				"```fix\nCommands\n\thelp - Displays this message\n\t\tAliases\n\t\t\t?\n\tpins - Displays this channel's pins\n\t\tAliases\n\t\t\tpin```");
	}
}