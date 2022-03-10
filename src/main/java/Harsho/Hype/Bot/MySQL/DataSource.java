package Harsho.Hype.Bot.MySQL;

import Harsho.Hype.Bot.Bot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {
    public static Connection connect;

    public static void readDataBase() {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://" + Bot.host, Bot.user, Bot.password);
            Statement statement = connect.createStatement();
            // language=mysql
            statement.execute("create database if not exists discordDatabase;");
            statement.execute("use discordDatabase;");
            statement.execute(
                    "create table if not exists guilds(" + "id int primary key auto_increment," + "name longtext,"
                            + "guildID varchar(20) not null unique," + "prefix varchar(255) not null default '"
                            + Bot.defPrefix + "'," + "channelID varchar(20) default 0,"
                            + "memberJoin bool not null default 1," + "memberLeave bool not null default 1" + ");");
            statement.execute(
                    "create table if not exists members(" + "id int primary key auto_increment," + "name longtext,"
                            + "memberID varchar(20) not null unique," + "amount int unsigned not null default 100,"
                            + "lastDaily DATETIME not null default '1000-01-01 00:00:00',"
                            + "streak int unsigned default 0" + ");");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
