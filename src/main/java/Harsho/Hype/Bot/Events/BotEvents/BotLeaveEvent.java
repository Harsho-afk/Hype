package Harsho.Hype.Bot.Events.BotEvents;

import Harsho.Hype.Bot.Storage;
import Harsho.Hype.Bot.Database.DataSource;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BotLeaveEvent extends ListenerAdapter {
    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        long guildID = event.getGuild().getIdLong();
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("DELETE FROM guilds WHERE guildID = ?")) {
            preparedStatement.setLong(1, guildID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Storage.logChannel.remove(guildID);
        Storage.PREFIXES.remove(guildID);
    }
}
