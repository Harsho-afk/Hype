package Harsho.Hype.Bot.Commands.FunCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CashCommand extends ListenerAdapter {
    private static String prefix;

    public static String help() {
        return prefix + "cash - shows how rich you are.";
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        if (message[0].equalsIgnoreCase(prefix + "cash")) {
            if (message.length < 2) {
                event.getChannel().sendMessageFormat("You have %d cash",
                        GetData.getAmount(Objects.requireNonNull(event.getMember()).getIdLong())).queue();
                return;
            }
            Member member = event.getMessage().getMentionedMembers().get(0);
            if (member == null) {
                event.getChannel().sendMessageFormat("You have %d cash",
                        GetData.getAmount(Objects.requireNonNull(event.getMember()).getIdLong())).queue();
                return;
            }
            int amount = GetData.getAmount(member.getIdLong());
            event.getChannel().sendMessageFormat("%s has %d cash", member.getAsMention(), amount).queue();
        }
    }
}
