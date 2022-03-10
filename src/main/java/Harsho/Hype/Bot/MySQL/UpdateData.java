package Harsho.Hype.Bot.MySQL;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import Harsho.Hype.Bot.Storage;

public class UpdateData {
    public static void updateAmount(long memberID, int amount) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("update members " + "set amount = ? " + "where memberID = ?")) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setLong(2, memberID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAllEvents(long guildID, boolean send) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("update guilds " + "set memberJoin = ?,memberLeave = ? " + "where guildID = ?;")) {
            preparedStatement.setBoolean(1, send);
            preparedStatement.setBoolean(2, send);
            preparedStatement.setLong(3, guildID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMemberJoin(long guildID, boolean send) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("update guilds " + "set memberJoin = ? " + "where guildID = ?;")) {
            preparedStatement.setBoolean(1, send);
            preparedStatement.setLong(2, guildID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMemberLeave(long guildID, boolean send) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("update guilds " + "set memberLeave = ? " + "where guildID = ?;")) {
            preparedStatement.setBoolean(1, send);
            preparedStatement.setLong(2, guildID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateLog(long guildID, long channelID) {
        Storage.logChannel.put(guildID, channelID);
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("update guilds " + "set channelID = ? " + "where guildID = ?;")) {
            preparedStatement.setLong(1, channelID);
            preparedStatement.setLong(2, guildID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePrefix(long guildID, String prefix) {
        Storage.PREFIXES.put(guildID, prefix);
        try (PreparedStatement updateStatement = DataSource.connect
                .prepareStatement("update guilds " + "set prefix = ? " + "where guildID = ?;")) {
            updateStatement.setString(1, prefix);
            updateStatement.setLong(2, guildID);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTime(long memberID, Timestamp time) {
        try (PreparedStatement preparableStatement = DataSource.connect
                .prepareStatement("update members set lastDaily = ? where memberID = ?")) {
            preparableStatement.setTimestamp(1, time);
            preparableStatement.setLong(2, memberID);

            preparableStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateStreak(long memberID, int increase) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("update members set streak = ? where memberID = ?")) {
            preparedStatement.setInt(1, increase);
            preparedStatement.setLong(2, memberID);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
