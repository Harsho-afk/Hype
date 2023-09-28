package Harsho.Hype.Bot.Events.GuildMemberEvents;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import Harsho.Hype.Bot.MySQL.GetData;

import java.util.Objects;

public class MemberLeaveEvent extends ListenerAdapter {
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        long guildID = event.getGuild().getIdLong();
        long logID = GetData.getLogID(guildID);
        boolean send = GetData.getLeave(guildID);
        if (logID == 0)
            return;
        if (!send)
            return;
            TextChannel channel = event.getGuild().getChannelById(null, logID);
        if (channel == null)
            return;
        String asMention = Objects.requireNonNull(event.getMember()).getAsMention();
        String name = event.getGuild().getName();
        channel.sendMessageFormat("%s left %s", asMention, name).queue();
    }
}
