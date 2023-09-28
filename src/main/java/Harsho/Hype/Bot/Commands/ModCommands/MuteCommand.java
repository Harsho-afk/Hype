package Harsho.Hype.Bot.Commands.ModCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MuteCommand extends ListenerAdapter {
    private static String prefix;

    public static String help(String prefix) {
        return prefix + "mute {member} {minutes} [reason] - mutes the mentioned member.";
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
        int minutes;
        Collection<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.MANAGE_ROLES);
        permissions.add(Permission.MANAGE_CHANNEL);
        if (message[0].equalsIgnoreCase(prefix + "mute")) {
            if (message.length < 2) {
                event.getChannel().sendMessage("Mention any user you want to mute").queue();
                return;
            }
            Member target = event.getMessage().getMentions().getMembers().get(0);
            assert member != null;
            if (!(member.hasPermission(permissions))) {
                event.getChannel().sendMessage("You do not have permission to mute that person").queue();
                return;
            }
            if (!event.getGuild().getSelfMember().canInteract(target)) {
                event.getChannel().sendMessage("I cant mute that person").queue();
                return;
            }
            if (!member.canInteract(target)) {
                event.getChannel().sendMessage("You cant mute that person").queue();
                return;
            }
            if (message.length < 3) {
                minutes = 0;
            } else {
                try {
                    minutes = Integer.parseInt(message[2]);
                } catch (Exception e) {
                    return;
                }
            }
            if (message.length < 4) {
                reason = "No Reason";
            } else {
                reason = message[3];
            }
            Role muted;
            try {
                muted = event.getGuild().getRolesByName("MUTED", false).get(0);
            } catch (IndexOutOfBoundsException e) {
                muted = event.getGuild().createRole().setName("MUTED").setPermissions(65536L).complete();
            }
            assert muted != null;
            event.getGuild().modifyRolePositions().selectPosition(muted.getPosition())
                    .moveTo(Objects.requireNonNull(event.getGuild().getBotRole()).getPosition() - 1).queue();
            for (TextChannel channel : event.getGuild().getTextChannels()) {
                channel.getManager().putPermissionOverride(muted, 65536L, 2048L).complete();
            }
            try {
                event.getGuild().addRoleToMember(target, muted).complete();
                event.getChannel().sendMessageFormat("%s has been muted by %s for %d minute. Reason - %s",
                        target.getAsMention(), member.getAsMention(), minutes, reason).queue();
                Role finalMuted = muted;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        event.getGuild().removeRoleFromMember(target, finalMuted).complete();
                        event.getChannel().sendMessageFormat("%s has been unmuted by timeout", target.getAsMention())
                                .queue();
                    }
                };
                LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(minutes, ChronoUnit.MINUTES));
                Date formattedDateTime = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
                Timer timer = new Timer();
                timer.schedule(task, formattedDateTime);
            } catch (Exception e) {
                event.getChannel().sendMessage("Something went wrong").queue();
                e.printStackTrace();
            }
        }
    }
}
