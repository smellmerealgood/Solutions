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

import javax.imageio.ImageIO;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.message.MessageBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Countdown {
	// key : AIzaSyCAoPw48ZlxqEb9_6geUQGtdzQgHWdsLo8
	// cx : 30205d639fbd94f38

	public Countdown(DiscordApi api) {
		Timer timer = new Timer();
		Calendar date = Calendar.getInstance();
		date.set(Calendar.MINUTE, 3);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		LocalDateTime now = LocalDateTime.now();

		if (now.getHour() < 9 && now.getMinute() < 11) {
			date.set(Calendar.HOUR_OF_DAY, 9);
		} else {
			date.set(Calendar.HOUR_OF_DAY, 5);
		}

		timer.schedule(new TimerTask() {
			public void run() {
				LocalDateTime now = LocalDateTime.now();

				if ((now.getHour() != 9 || now.getHour() != 21)
						&& now.getMinute() != 11) {
					return;
				}

				if (date.get(Calendar.HOUR_OF_DAY) == 9) {
					date.set(Calendar.HOUR_OF_DAY, 21);
				} else {
					date.set(Calendar.HOUR_OF_DAY, 9);
				}

				for (int count = 0; count < 10; count++) {
					try {
						request(api);
						break;
					} catch (Exception e) {
					}
				}
			}
		}, date.getTime(), 1000 * 60 * 60 * 24);
	}

	public static void request(DiscordApi api) {
		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) new URL(
					"https://customsearch.googleapis.com/customsearch/v1?cx=30205d639fbd94f38&q=9/11+meme&searchType=image&start="
							+ new Random().nextInt(250)
							+ "&key=AIzaSyCAoPw48ZlxqEb9_6geUQGtdzQgHWdsLo8")
					.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(connection.getHeaderFields());

		InputStream responseStream = null;

		try {
			responseStream = connection.getResponseCode() / 100 == 2
					? connection.getInputStream()
					: connection.getErrorStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");

		String result = scanner.hasNext() ? scanner.next() : "";

		System.out.println(result);

		JSONArray images = new JSONObject(result).getJSONArray("items");

		scanner.close();

		String link = images
				.getJSONObject(new Random().nextInt(images.length()))
				.getString("link");

		try {
			new MessageBuilder()
					.addAttachment(ImageIO.read(new URL(link)),
							link.substring(link.lastIndexOf("/") + 1))
					.send(api.getTextChannelById("1057444809125666856").get());
		} catch (MalformedURLException | JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}