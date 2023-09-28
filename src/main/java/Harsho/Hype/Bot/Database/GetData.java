package Harsho.Hype.Bot.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import Harsho.Hype.Bot.Bot;

public class GetData {
    public static int getAmount(long memberID) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("SELECT amount FROM members WHERE memberID = ?;")) {
            preparedStatement.setLong(1, memberID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("amount");
                }
            }

            try (PreparedStatement preparedStatement1 = DataSource.connect
                    .prepareStatement("INSERT INTO members(memberID) VALUES(?)")) {
                preparedStatement1.setLong(1, memberID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 100;
    }

    public static Long getLogID(long guildID) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("SELECT channelID FROM guilds WHERE guildID = ?;")) {
            preparedStatement.setLong(1, guildID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("channelID");
                }
            }

            try (PreparedStatement insertStatement = DataSource.connect
                    .prepareStatement("INSERT INTO guilds(guildID) VALUES(?)")) {
                insertStatement.setLong(1, guildID);
                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static boolean getJoin(long guildID) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("SELECT memberJoin FROM guilds WHERE guildID = ?")) {
            preparedStatement.setLong(1, guildID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("memberJoin");
                }
            }

            try (PreparedStatement insertStatement = DataSource.connect
                    .prepareStatement("INSERT INTO guilds(guildID) VALUES(?)")) {
                insertStatement.setLong(1, guildID);
                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean getLeave(long guildID) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("SELECT memberLeave FROM guilds WHERE guildID = ?")) {
            preparedStatement.setLong(1, guildID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("memberLeave");
                }
            }

            try (PreparedStatement insertStatement = DataSource.connect
                    .prepareStatement("INSERT INTO guilds(guildID) VALUES(?)")) {
                insertStatement.setLong(1, guildID);
                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getPrefix(long guildID) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("SELECT prefix FROM guilds WHERE guildID = ?;")) {
            preparedStatement.setLong(1, guildID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefix");
                }
            }

            try (PreparedStatement insertStatement = DataSource.connect
                    .prepareStatement("INSERT INTO guilds(guildID) VALUES(?);")) {
                insertStatement.setLong(1, guildID);

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Bot.defPrefix;
    }

    public static boolean subtime(long memberID, Timestamp time) {
        long finalTimestamp = Timestamp.valueOf("1000-01-01 00:00:00").getTime();
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("SELECT ROUND(? - 8.64e+7) AS finaltime")) {
            preparedStatement.setLong(1, time.getTime());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    finalTimestamp = resultSet.getLong("finaltime");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long lastTimestamp = getTime(memberID).getTime();
        if (finalTimestamp - lastTimestamp > 86400000) {
            if(finalTimestamp - lastTimestamp > 172800000) {
                UpdateData.updateStreak(memberID, 0);
            }
            return true;
        } else {
            return false;
        }
    }

    public static Timestamp getTime(long memberID) {
        try (PreparedStatement preparedStatement = DataSource.connect
                .prepareStatement("SELECT lastDaily FROM members WHERE memberID = ?")) {
            preparedStatement.setLong(1, memberID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getTimestamp("lastDaily");
                }
            }

            try (PreparedStatement preparedStatement1 = DataSource.connect
                    .prepareStatement("INSERT INTO members(memberID) VALUES(?)")) {
                preparedStatement1.setLong(1, memberID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getStreak(long memberID) {
        try (PreparedStatement preparedStatement = DataSource.connect.prepareStatement("SELECT streak FROM members WHERE memberID = ?")){
            preparedStatement.setLong(1, memberID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getInt("streak");
                }
            }

            try (PreparedStatement preparedStatement1 = DataSource.connect
                    .prepareStatement("INSERT INTO members(memberID) VALUES(?)")) {
                preparedStatement1.setLong(1, memberID);
            }    
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
