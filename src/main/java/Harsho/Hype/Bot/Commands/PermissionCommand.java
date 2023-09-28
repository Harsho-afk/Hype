package Harsho.Hype.Bot.Commands;

import Harsho.Hype.Bot.Storage;
import Harsho.Hype.Bot.Database.GetData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PermissionCommand extends ListenerAdapter {
    private static String prefix;

    public static String help(String prefix) {
        return prefix + "perm - Shows what permission is needed for commands.";
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        if (message[0].equalsIgnoreCase(prefix + "perm")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Permissions");
            embedBuilder.addField("Log Commands", "Manage Server", false);
            embedBuilder.addField("Mod Commands",
                    "Manage Roles, Manage Channel, Kick Members, Ban Members, and Manage Message", false);
            embedBuilder.addField("Config Commands", "Administrator", false);
            embedBuilder.setColor(Color.RED);
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            embedBuilder.clear();
        }
    }
}
