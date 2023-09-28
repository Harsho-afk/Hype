package Harsho.Hype.Bot.Database;

import Harsho.Hype.Bot.Bot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {
    public static Connection connect;

    public static void readDataBase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:discordDatabase.db");
            Statement statement = connect.createStatement();
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS guilds(id INT PRIMARY KEY AUTOINCREMENT," + "name LONGTEXT,"
                            + "guildID VARCHAR(20) NOT NULL UNIQUE," + "prefix VARCHAR(255) NOT NULL DEFAULT '"
                            + Bot.defPrefix + "'," + "channelID VARCHAR(20) DEFAULT 0,"
                            + "memberJoin BOOL NOT NULL DEFAULT 1," + "memberLeave BOOL NOT NULL DEFAULT 1" + ");");
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS members(" + "id INT PRIMARY KEY AUTOINCREMENT," + "name LONGTEXT,"
                            + "memberID VARCHAR(20) NOT NULL UNIQUE," + "amount INT UNSIGNED NOT NULL DEFAULT 100,"
                            + "lastDaily DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00',"
                            + "streak INT UNSIGNED DEFAULT 0" + ");");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
