package Commands;

import Systems.Storage;
import Database.GetData;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends ListenerAdapter {
    private static String prefix;

    public static String help(String prefix) {
        return prefix + "ping - You know what it does.";
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        if (message[0].equalsIgnoreCase(prefix + "ping")) {
            event.getJDA().getRestPing()
                    .queue((time) -> event.getChannel().sendMessageFormat("Ping: %dms", time).queue());
        }
    }
}
