package Harsho.Hype.Bot.Events.BotEvents;

import Harsho.Hype.Bot.MySQL.DataSource;
import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class BotReadyEvent extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        List<Guild> guilds = event.getJDA().getGuilds();
        long guildID = 0;
        // guilds members name
        for (Guild guild : guilds) {
            guildID = guild.getIdLong();
            GetData.getPrefix(guildID);
            GetData.getLogID(guildID);
            GetData.getLeave(guildID);
            GetData.getJoin(guildID);
            String name = Objects.requireNonNull(event.getJDA().getGuildById(guildID)).getName();
            List<Member> guildMembers = guild.getMembers();
            try (PreparedStatement preparedStatement = DataSource.connect
                    .prepareStatement("update guilds " + "set name = ? " + "where guildID = ?")) {
                preparedStatement.setString(1, name);
                preparedStatement.setLong(2, guildID);
                preparedStatement.executeUpdate();
                for (Member member : guildMembers) {
                    try (PreparedStatement preparedStatement1 = DataSource.connect
                            .prepareStatement("insert ignore into members(name, memberID) " + "values(?, ?)")) {
                        preparedStatement1.setString(1, member.getEffectiveName());
                        preparedStatement1.setLong(2, member.getIdLong());
                        preparedStatement1.execute();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // guilds
        int num = 0;
        try (Statement statement = DataSource.connect.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("select count(id) " + "from guilds;")) {
                if (resultSet.next()) {
                    num = resultSet.getInt("count(id)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= num; i++) {
            try (PreparedStatement preparedStatement = DataSource.connect
                    .prepareStatement("select guildID " + "from guilds " + "where id = ?;")) {
                preparedStatement.setInt(1, i);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        guildID = resultSet.getLong("guildID");
                        Storage.PREFIXES.put(guildID, GetData.getPrefix(guildID));
                        Storage.logChannel.put(guildID, GetData.getLogID(guildID));
                        Storage.memberJoin.put(guildID, GetData.getJoin(guildID));
                        Storage.memberLeave.put(guildID, GetData.getLeave(guildID));
                    }
                }
                try (Statement statement = DataSource.connect.createStatement()) {
                    statement.execute("select id " + "from guilds " + "order by id ASC;");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
