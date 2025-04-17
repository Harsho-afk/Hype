package Commands.FunCommands;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import Systems.Storage;
import Database.GetData;
import Database.UpdateData;
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
            Timestamp lastDaily = GetData.getTime(event.getMember().getIdLong());
            Timestamp now = new Timestamp(System.currentTimeMillis());

            if (lastDaily == null || Duration.between(lastDaily.toLocalDateTime(), now.toLocalDateTime()).toHours() >= 24) {
                int streak = GetData.getStreak(event.getMember().getIdLong());
                UpdateData.updateStreak(event.getMember().getIdLong(), (streak + 1));
                UpdateData.updateAmount(event.getMember().getIdLong(), (GetData.getAmount(event.getMember().getIdLong())
                        + 10 * (GetData.getStreak(event.getMember().getIdLong()))));
                UpdateData.updateTime(event.getMember().getIdLong(), now);
                int amount = GetData.getAmount(event.getMember().getIdLong());
                streak = GetData.getStreak(event.getMember().getIdLong());
                event.getChannel().sendMessageFormat("You got %d cash. Streak - %s", amount, streak).queue();
            } else {
                Duration timeSinceLastDaily = Duration.between(lastDaily.toLocalDateTime(), now.toLocalDateTime());
                Duration timeLeft = Duration.ofHours(24).minus(timeSinceLastDaily);

                long hours = timeLeft.toHours();
                long minutes = timeLeft.toMinutesPart();
                long seconds = timeLeft.toSecondsPart();

                event.getChannel()
                        .sendMessageFormat("Come after %d hrs %d min %d sec. Streak - %d",
                                Math.max(0, hours), Math.max(0, minutes), Math.max(0, seconds), GetData.getStreak(event.getMember().getIdLong()))
                        .queue();
            }
        }
    }
}
