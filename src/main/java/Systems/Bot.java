package Systems;

import Database.DataSource;
import Events.BotEvents.BotReadyEvent;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

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
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MESSAGES,GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build();
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}
