package Harsho.Hype.Bot;

import Harsho.Hype.Bot.Events.BotEvents.BotReadyEvent;
import Harsho.Hype.Bot.MySQL.DataSource;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
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
        host = dotenv.get("HOST");
        user = dotenv.get("USER");
        password = dotenv.get("PASS");
        DataSource.readDataBase();
        JDABuilder.createDefault(token).setStatus(OnlineStatus.IDLE).enableIntents(GatewayIntent.GUILD_MEMBERS).enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setChunkingFilter(ChunkingFilter.ALL).addEventListeners(new Listener())
                .addEventListeners(new BotReadyEvent()).build();
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}
