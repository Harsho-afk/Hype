package Harsho.Hype.Bot.Events.BotEvents;

import Harsho.Hype.Bot.MySQL.DataSource;
import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BotJoinEvent extends ListenerAdapter {
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        long guildID = event.getGuild().getIdLong();
        String prefix = GetData.getPrefix(guildID);
        long channelID = GetData.getLogID(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        Storage.logChannel.put(guildID, channelID);
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("insert into guilds (guildID, name, prefix, channelID) " + "values (?, ?, ?, ?);")) {
            preparedStatement.setLong(1, guildID);
            preparedStatement.setString(2, event.getGuild().getName());
            preparedStatement.setString(3, prefix);
            preparedStatement.setLong(4, channelID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
