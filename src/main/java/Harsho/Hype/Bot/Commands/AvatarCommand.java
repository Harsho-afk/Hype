package Harsho.Hype.Bot.Commands;

import Harsho.Hype.Bot.Storage;
import Harsho.Hype.Bot.MySQL.GetData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class AvatarCommand extends ListenerAdapter {
    private static String prefix;

    public static String help() {
        return prefix + "ava [user] - sends avatar of the mentioned user or of yourself.";
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        String url;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (message[0].equalsIgnoreCase(prefix + "ava")) {
            if (message.length < 2) {
                url = event.getAuthor().getEffectiveAvatarUrl();
                embedBuilder.setColor(Color.red);
                embedBuilder.setImage(url);
                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                embedBuilder.clear();
                return;
            }
            Member member = event.getMessage().getMentionedMembers().get(0);
            if (member == null) {
                url = event.getAuthor().getEffectiveAvatarUrl();
                embedBuilder.setColor(Color.red);
                embedBuilder.setImage(url);
                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                embedBuilder.clear();
                return;
            }
            url = member.getUser().getEffectiveAvatarUrl();
            embedBuilder.setColor(Color.red);
            embedBuilder.setImage(url);
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            embedBuilder.clear();
        }
    }
}
