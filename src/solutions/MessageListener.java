package solutions;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import solutions.commands.Help;
import solutions.commands.Quote;
import solutions.commands.Pins;

public class MessageListener implements MessageCreateListener {
	public void onMessageCreate(MessageCreateEvent event) {
		String raw = event.getMessage().getContent().toLowerCase();
		String[] splitRaw = raw.split(" ");

		if (!event.getMessageAuthor().asUser().get().getDiscriminatedName()
				.equals(event.getApi().getYourself().getDiscriminatedName()) && splitRaw[0].equals("sol")) {
			event.getChannel().type();

			if (splitRaw[1].equals("help") || splitRaw[1].equals("?")) {
				new Help(event);
			} else if (splitRaw[1].equals("pins") || splitRaw[1].equals("pin")) {
				new Pins(event);
			} else if (splitRaw[1].equals("quote")) {
				new Quote(event, raw);
			}
		}
	}
}