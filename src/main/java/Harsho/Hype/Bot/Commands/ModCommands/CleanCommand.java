package Harsho.Hype.Bot.Commands.ModCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CleanCommand extends ListenerAdapter {
    private static String prefix;

    public static String help() {
        return prefix + "clean || " + prefix + "purge [Number of messages] - Deletes the number of message mentioned.";
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        Member member = event.getMember();
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        Collection<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_CHANNEL);
        permissions.add(Permission.MESSAGE_MANAGE);
        if (message[0].equalsIgnoreCase(prefix + "clean") || message[0].equalsIgnoreCase(prefix + "purge")) {
            if (message.length < 2) {
                event.getChannel().sendMessage("Mention number of messages").queue();
                return;
            }
            assert member != null;
            if (!(member.hasPermission(permissions))) {
                event.getChannel().sendMessage("You do not have permission to delete message").queue();
                return;
            }
            String numberS = message[1];
            try {
                Integer.parseInt(numberS);
            } catch (Exception e) {
                return;
            }
            int number = Integer.parseInt(numberS);
            if (number > 100) {
                event.getChannel().sendMessage("Cant delete more than 100 message").queue();
                return;
            }
            List<Message> retrievedHistory = event.getChannel().getHistoryBefore(event.getMessage(), number).complete()
                    .getRetrievedHistory();
            event.getChannel().purgeMessages(retrievedHistory);
            event.getMessage().delete().complete();
        }
    }
}
