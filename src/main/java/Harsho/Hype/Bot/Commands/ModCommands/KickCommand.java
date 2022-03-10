package Harsho.Hype.Bot.Commands.ModCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class KickCommand extends ListenerAdapter {
    private static String prefix;

    public static String help() {
        return prefix + "kick {user} [reason] - kicks the mentioned user.";
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
        String reason;
        Collection<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.KICK_MEMBERS);
        if (message[0].equalsIgnoreCase(prefix + "kick")) {
            if (message.length < 2) {
                event.getChannel().sendMessage("Mention any user you want to kick").queue();
                return;
            }
            Member target = event.getMessage().getMentionedMembers().get(0);
            assert member != null;
            if (!(member.hasPermission(permissions))) {
                event.getChannel().sendMessage("You do not have permission to kick that person").queue();
                return;
            }
            if (!event.getGuild().getSelfMember().canInteract(target)) {
                event.getChannel().sendMessage("I cant kick that person").queue();
                return;
            }
            if (!member.canInteract(target)) {
                event.getChannel().sendMessage("You cant kick that person").queue();
                return;
            }
            if (message.length < 3) {
                reason = "No Reason";
            } else {
                reason = message[2];
            }
            try {
                Objects.requireNonNull(event.getGuild().getMemberById(target.getIdLong())).kick(reason).complete();
                event.getChannel()
                        .sendMessageFormat("%s has been kicked \n" + "Reason - %s", target.getEffectiveName(), reason)
                        .queue();
            } catch (Exception e) {
                event.getChannel().sendMessage("Something went wrong").queue();
                e.printStackTrace();
            }
        }
    }
}
