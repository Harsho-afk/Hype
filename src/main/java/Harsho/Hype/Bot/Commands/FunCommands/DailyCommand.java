package Harsho.Hype.Bot.Commands.FunCommands;

import java.sql.Timestamp;

import Harsho.Hype.Bot.Storage;
import Harsho.Hype.Bot.TimeConverter;
import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.MySQL.UpdateData;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DailyCommand extends ListenerAdapter {
    private static String prefix;

    public static String help(String prefix) {
        return prefix + "daily - gives you daily reward.";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        if (message[0].equalsIgnoreCase(prefix + "daily")) {
            if (GetData.subtime(event.getMember().getIdLong(), new Timestamp(System.currentTimeMillis()))) {
                int streak = GetData.getStreak(event.getMember().getIdLong());
                UpdateData.updateStreak(event.getMember().getIdLong(), (streak + 1));
                UpdateData.updateAmount(event.getMember().getIdLong(), (GetData.getAmount(event.getMember().getIdLong())
                        + 10 * (GetData.getStreak(event.getMember().getIdLong()))));
                UpdateData.updateTime(event.getMember().getIdLong(), new Timestamp(System.currentTimeMillis()));
                int ammount = GetData.getAmount(event.getMember().getIdLong());
                streak = GetData.getStreak(event.getMember().getIdLong());
                event.getChannel().sendMessageFormat("You got %d cash. Streak - %s", ammount, streak).queue();
            } else {
                event.getChannel()
                        .sendMessageFormat("Come after %s. Streak - %d",
                                TimeConverter.diffHours(GetData.getTime(event.getMember().getIdLong()),
                                        new Timestamp(System.currentTimeMillis())),
                                GetData.getStreak(event.getMember().getIdLong()))
                        .queue();
            }
        }
    }
}
