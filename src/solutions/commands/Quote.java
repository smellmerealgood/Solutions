package solutions.commands;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;

public class Quote {
	public Quote(MessageCreateEvent event, String raw) {
		TextChannel channel = event.getChannel();
		
		String quote = "";
		
		if (raw.contains("https://discord.com/channels/@me/")) {
			quote = channel.getMessageById(raw.substring(raw.indexOf(channel.getIdAsString() + "/") + (channel.getIdAsString() + "/").length(), raw.length()));
		}
	}
}