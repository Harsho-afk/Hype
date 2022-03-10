package Harsho.Hype.Bot.Commands.ConfigCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.MySQL.UpdateData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class SetPrefixCommand extends ListenerAdapter {
    private static String prefix;

    public static String help() {
        return prefix + "setprefix {new prefix} - sets a custom prefix.";
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        Collection<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.ADMINISTRATOR);
        if (message[0].equalsIgnoreCase(prefix + "setprefix")) {
            Member author = event.getMember();
            assert author != null;
            if (!author.hasPermission(permissions)) {
                event.getChannel().sendMessage("You do not have the required permission to change the prefix").queue();
                return;
            }
            if (message.length < 2) {
                event.getChannel().sendMessage("Mention the prefix you want").queue();
                return;
            }
            String newPrefix = message[1];
            try {
                UpdateData.updatePrefix(guildID, newPrefix);
                event.getChannel().sendMessageFormat("Prefix has been set to `%s`", message[1]).queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
