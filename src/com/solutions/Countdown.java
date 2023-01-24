package com.solutions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Countdown {
	public Countdown(DiscordApi api) {
		Timer timer = new Timer();
		Calendar date = Calendar.getInstance();
		date.set(Calendar.MINUTE, 11);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		LocalDateTime now = LocalDateTime.now();

		if (now.getHour() < 9 && now.getMinute() < 11) {
			date.set(Calendar.HOUR_OF_DAY, 9);
		} else {
			date.set(Calendar.HOUR_OF_DAY, 21);
		}

		timer.schedule(new TimerTask() {
			public void run() {
				if (date.get(Calendar.HOUR_OF_DAY) == 9) {
					date.set(Calendar.HOUR_OF_DAY, 21);
				} else {
					date.set(Calendar.HOUR_OF_DAY, 9);
				}

				HttpURLConnection connection = null;

				try {
					connection = (HttpURLConnection) new URL(
							"https://serpapi.com/search.json?q=9/11&tbm=isch&ijn=0")
							.openConnection();
				} catch (IOException e) {
					e.printStackTrace();
				}

				connection.setRequestProperty("api_key",
						"2022c0b0108f54ff817077d292c618feb2cca14ce80fcd04796dbd87c8d290a6");

				InputStream responseStream = null;

				try {
					responseStream = connection.getResponseCode() / 100 == 2
							? connection.getInputStream()
							: connection.getErrorStream();
				} catch (IOException e) {
					e.printStackTrace();
				}

				Scanner scanner = new Scanner(responseStream)
						.useDelimiter("\\A");

				String result = scanner.hasNext() ? scanner.next() : "";

				JSONArray images = new JSONObject(result)
						.getJSONArray("images_results");

				scanner.close();

				try {
					new MessageBuilder()
							.addAttachment(new URL(images
									.getJSONObject(new Random()
											.nextInt(images.length()))
									.getString("original")))
							.send(api.getTextChannelById("1025957114671280179")
									.get());
				} catch (MalformedURLException | JSONException e) {
					e.printStackTrace();
				}
			}
		}, date.getTime(), 1000 * 60 * 60 * 24);
	}
}
