package Harsho.Hype.Bot.Commands;

import Harsho.Hype.Bot.Commands.ConfigCommands.*;
import Harsho.Hype.Bot.Commands.FunCommands.*;
import Harsho.Hype.Bot.Commands.LogCommands.*;
import Harsho.Hype.Bot.Commands.ModCommands.*;
import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HelpCommand extends ListenerAdapter {
        private static String prefix;

        public static String help() {
                return prefix + "help - shows all commands.";
        }

        @Override
        public void onMessageReceived(@NotNull MessageReceivedEvent event) {
                if (event.getAuthor().isBot())
                        return;
                String[] message = event.getMessage().getContentRaw().split("\\s+");
                long guildID = event.getGuild().getIdLong();
                prefix = GetData.getPrefix(guildID);
                Storage.PREFIXES.put(guildID, prefix);
                if (message[0].equalsIgnoreCase(prefix + "help")) {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle("Command List");
                        embedBuilder.setDescription(HelpCommand.help() + "\n" + AvatarCommand.help() + "\n"
                                        + PingCommand.help() + "\n" + PermissionCommand.help());
                        embedBuilder.addField("Fun Commands",
                                        CatCommand.help() + "\n" + CoinFlipCommand.help() + "\n" + DogCommand.help()
                                                        + "\n" + MemeCommand.help() + "\n" + CashCommand.help() + "\n"
                                                        + GiveCashCommand.help() + "\n" + DailyCommand.help(),
                                        false);
                        embedBuilder.addField("Log Commands", SetLogCommand.help() + "\n" + RemoveLogCommand.help()
                                        + "\n" + SetEventsCommand.help(), false);
                        embedBuilder.addField("Mod Commands",
                                        BanCommand.help() + "\n" + KickCommand.help() + "\n" + CleanCommand.help()
                                                        + "\n" + MuteCommand.help() + "\n" + UnMuteCommand.help(),
                                        false);
                        embedBuilder.addField("Config Commands", SetPrefixCommand.help(), false);
                        embedBuilder.setColor(Color.RED);
                        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                        embedBuilder.clear();
                }
        }
}
