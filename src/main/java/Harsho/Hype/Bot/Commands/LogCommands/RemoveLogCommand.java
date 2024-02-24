package Harsho.Hype.Bot.Commands.LogCommands;

import Harsho.Hype.Bot.Storage;
import Harsho.Hype.Bot.Database.GetData;
import Harsho.Hype.Bot.Database.UpdateData;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class RemoveLogCommand extends ListenerAdapter {
    private static String prefix;

    public static String help(String prefix) {
        return prefix + "removelog - stops sending log to the log channel.";
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        Collection<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_SERVER);
        if (message[0].equalsIgnoreCase(prefix + "removelog")) {
            Member author = event.getMember();
            assert author != null;
            if (!author.hasPermission(permissions)) {
                event.getChannel().sendMessage("You do not have the required permission to set the log channel")
                        .queue();
                return;
            }
            if (GetData.getLogID(guildID) == 0) {
                event.getChannel().sendMessageFormat("There is no log channel").queue();
                return;
            }
            String asMention = Objects.requireNonNull(event.getGuild().getGuildChannelById(GetData.getLogID(guildID)))
                    .getAsMention();
            event.getChannel().sendMessage("You wont receive any log in " + asMention).queue();
            UpdateData.updateLog(guildID, 0L);
            Storage.logChannel.remove(guildID);
        }
    }
}
