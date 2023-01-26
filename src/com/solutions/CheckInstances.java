package com.solutions;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;

public class CheckInstances {
	public CheckInstances(DiscordApi api) {
		TextChannel channel = api.getTextChannelById("1067961290745724928")
				.get();

		long sentMessage = Long.valueOf(new MessageBuilder()
				.setContent(
						ManagementFactory.getRuntimeMXBean().getUptime() + "")
				.send(channel).join().getContent());

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		List<Long> instances = new ArrayList<String>();

		for (Object object : channel.getMessages(100).join().toArray()) {
			String string = object + "";

			Message message = channel.getMessageById(string
					.substring(string.indexOf(":") + 2, string.indexOf(",")))
					.join();

			instances.add(message.getContent());

			try {
				channel.deleteMessages(message).join();
			} catch (Exception e) {
				instances.remove(instances.size() - 1);
			}
		}

		int earliestInstant = sentMessage.compareTo(instances.get(0));

		for (int i = 1; i < instances.size(); i++) {
			if (sentMessage)
				System.out.println(sentMessage.getEpochSecond()
						- instances.get(0).getEpochSecond());
		}
	}
}