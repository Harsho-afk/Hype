package Harsho.Hype.Bot.Events.GuildMemberEvents;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import Harsho.Hype.Bot.MySQL.GetData;

public class MemberJoinEvent extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent event) {
        long guildID = event.getGuild().getIdLong();
        long logID = GetData.getLogID(guildID);
        boolean send = GetData.getJoin(guildID);
        if (logID == 0)
            return;
        if (!send)
            return;
        TextChannel channel = event.getGuild().getTextChannelById(logID);
        if (channel == null)
            return;
        String asMention = event.getMember().getAsMention();
        String name = event.getGuild().getName();
        channel.sendMessageFormat("%s joined %s", asMention, name).queue();
    }
}
