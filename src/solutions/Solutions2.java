package solutions;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.LoginException;

import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;

public class Solutions2 {
	public static final String TOKEN = "OTM1Mjk1MjA3NDQyOTAzMDUw.GirkDV.VIRTmk75HUVun0IuYcTaQmG1GQo1-N0ZuYv2MM";

	public static void main(String[] args)
			throws LoginException, InterruptedException, ExecutionException, IOException {
		DiscordApi api = new DiscordApiBuilder().setAccountType(AccountType.CLIENT)
				.addMessageCreateListener(new MessageListener()).setToken(TOKEN).login().join();
		api.updateActivity(ActivityType.PLAYING, "sol");
	}
}