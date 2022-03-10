package Harsho.Hype.Bot.Commands.ModCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class UnMuteCommand extends ListenerAdapter {
    private static String prefix;

    public static String help(String prefix) {
        return prefix + "unmute {user} - unmutes the mentioned user.";
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
        permissions.add(Permission.MANAGE_ROLES);
        if (message[0].equalsIgnoreCase(prefix + "unmute")) {
            if (message.length < 2) {
                event.getChannel().sendMessage("Mention any user you want to unmute").queue();
                return;
            }
            Member target = event.getMessage().getMentionedMembers().get(0);
            assert member != null;
            if (!(member.hasPermission(permissions))) {
                event.getChannel().sendMessage("You do not have permission to unmute that person").queue();
                return;
            }
            if (!event.getGuild().getSelfMember().canInteract(target)) {
                event.getChannel().sendMessage("I cant unmute that person").queue();
                return;
            }
            if (!member.canInteract(target)) {
                event.getChannel().sendMessage("You cant unmute that person").queue();
                return;
            }
            Role muted = event.getGuild().getRolesByName("MUTED", false).get(0);
            if (!target.getRoles().contains(muted)) {
                event.getChannel().sendMessage("That user is not muted").queue();
                return;
            }
            event.getGuild().removeRoleFromMember(target, muted).complete();
            event.getChannel()
                    .sendMessageFormat("%s has been unmuted by %s", target.getAsMention(), member.getAsMention())
                    .queue();
        }
    }
}
