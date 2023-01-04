package com.solutions;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;

public class Solutions {
	public static void main(String[] args)
			throws LoginException, InterruptedException, ExecutionException, IOException {
		DiscordApi api = new DiscordApiBuilder().setAccountType(AccountType.CLIENT)
				.addMessageCreateListener(new MessageListener()).setToken("OTM1Mjk1MjA3NDQyOTAzMDUw.Gnnxy6._Qoz1A63agARq64A7pK6uWggWAGnYhbGiG-c7s").login().join();
		api.updateActivity(ActivityType.PLAYING, "sol help");
	}
}