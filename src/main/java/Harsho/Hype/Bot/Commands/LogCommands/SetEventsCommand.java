package Harsho.Hype.Bot.Commands.LogCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.MySQL.UpdateData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class SetEventsCommand extends ListenerAdapter {
    private static String prefix;

    public static String help(String prefix) {
        return prefix
                + "events {'all' or event name} {'true' or 'false'} - Enables or disables event message in log channel.\n Events - MemberJoin and MemberLeave.";
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        boolean send;
        Collection<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_SERVER);
        if (message[0].equalsIgnoreCase(prefix + "events")) {
            Member author = event.getMember();
            assert author != null;
            if (!author.hasPermission(permissions)) {
                event.getChannel().sendMessage("You do not have the required permission").queue();
                return;
            }
            if (message.length < 2) {
                return;
            }
            // all
            if (message[1].equalsIgnoreCase("all")) {
                String sendS = message[2];
                if (sendS.isEmpty()) {
                    event.getChannel().sendMessage("Mention what you want to do with the events('true' or 'false'")
                            .queue();
                    return;
                }
                send = Boolean.parseBoolean(sendS);
                try {
                    UpdateData.updateAllEvents(guildID, send);
                    if (send) {
                        event.getChannel().sendMessage("All log events are enabled").queue();
                    } else {
                        event.getChannel().sendMessage("All log events are disabled").queue();
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            // memberJoin
            if (message[1].equalsIgnoreCase("memberjoin")) {
                String sendS = message[2];
                if (sendS.isEmpty()) {
                    event.getChannel().sendMessage("Mention what you want to do with the events('true' or 'false'")
                            .queue();
                    return;
                }
                send = Boolean.parseBoolean(sendS);
                try {
                    UpdateData.updateMemberJoin(guildID, send);
                    if (send) {
                        event.getChannel().sendMessage("Member Join event is enabled").queue();
                    } else {
                        event.getChannel().sendMessage("Member Join event is disabled").queue();
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            // memberLeave
            if (message[1].equalsIgnoreCase("memberleave")) {
                String sendS = message[2];
                if (sendS.isEmpty()) {
                    event.getChannel().sendMessage("Mention what you want to do with the events('true' or 'false'")
                            .queue();
                    return;
                }
                send = Boolean.parseBoolean(sendS);
                try {
                    UpdateData.updateMemberLeave(guildID, send);
                    if (send) {
                        event.getChannel().sendMessage("Member Leave event is enabled").queue();
                    } else {
                        event.getChannel().sendMessage("Member Leave event is disabled").queue();
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
