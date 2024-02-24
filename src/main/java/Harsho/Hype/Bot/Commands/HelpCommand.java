package Harsho.Hype.Bot.Commands;

import Harsho.Hype.Bot.Commands.ConfigCommands.*;
import Harsho.Hype.Bot.Commands.FunCommands.*;
import Harsho.Hype.Bot.Commands.LogCommands.*;
import Harsho.Hype.Bot.Commands.ModCommands.*;
import Harsho.Hype.Bot.Database.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HelpCommand extends ListenerAdapter {
        private static String prefix;

        public static String help(String prefix) {
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
                        embedBuilder.setDescription(HelpCommand.help(prefix) + "\n" + AvatarCommand.help(prefix) + "\n"
                                        + PingCommand.help(prefix) + "\n" + PermissionCommand.help(prefix));
                        embedBuilder.addField("Fun Commands",
                                        CatCommand.help(prefix) + "\n" + CoinFlipCommand.help(prefix) + "\n" + DogCommand.help(prefix)
                                                        + "\n" + MemeCommand.help(prefix) + "\n" + CashCommand.help(prefix) + "\n"
                                                        + GiveCashCommand.help(prefix) + "\n" + DailyCommand.help(prefix),
                                        false);
                        embedBuilder.addField("Log Commands", SetLogCommand.help(prefix) + "\n" + RemoveLogCommand.help(prefix)
                                        + "\n" + SetEventsCommand.help(prefix), false);
                        embedBuilder.addField("Mod Commands",
                                        BanCommand.help(prefix) + "\n" + KickCommand.help(prefix) + "\n" + CleanCommand.help(prefix)
                                                        + "\n" + MuteCommand.help(prefix) + "\n" + UnMuteCommand.help(prefix),
                                        false);
                        embedBuilder.addField("Config Commands", SetPrefixCommand.help(prefix), false);
                        embedBuilder.setColor(Color.RED);
                        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                        embedBuilder.clear();
                }
        }
}
