package Harsho.Hype.Bot;

import Harsho.Hype.Bot.Database.DataSource;
import Harsho.Hype.Bot.Events.BotEvents.BotReadyEvent;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter {
    public static String defPrefix = "!";
    private String token;
    public static String host;
    public static String user;
    public static String password;

    Bot() throws LoginException {
        Dotenv dotenv = Dotenv.load();
        token = dotenv.get("TOKEN");
        DataSource.readDataBase();
        JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListeners(new Listener())
                .addEventListeners(new BotReadyEvent())
                .setActivity(Activity.playing("Type /ping"))
                .build();
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}
