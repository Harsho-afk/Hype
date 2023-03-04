package Harsho.Hype.Bot.Commands.FunCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.MySQL.UpdateData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GiveCashCommand extends ListenerAdapter {
    private static String prefix;

    public static String help(String prefix) {
        return prefix + "givecash {user} {amount} - Gives the mentioned cash to the user.";
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        if (message[0].equalsIgnoreCase(prefix + "givecash")) {
            if (message.length < 2) {
                event.getChannel().sendMessage("Mention a user you want to send money to").queue();
                return;
            }
            Member member = event.getMessage().getMentions().getMembers().get(0);
            if (member == null) {
                event.getChannel().sendMessage("Mention a real user").queue();
                return;
            }
            if (message.length < 3) {
                event.getChannel().sendMessage("Mention the amount of cash you want to send").queue();
                return;
            }
            int amu = 0;
            try {
                amu = Integer.parseInt(message[2]);
            } catch (Exception e) {
                return;
            }
            if (amu > GetData.getAmount(event.getMember().getIdLong())) {
                event.getChannel().sendMessage("You do not have enough cash").queue();
                return;
            }
            // gives money
            UpdateData.updateAmount(member.getIdLong(), GetData.getAmount(member.getIdLong()) + amu);
            // removes money
            UpdateData.updateAmount(event.getMember().getIdLong(),
                    GetData.getAmount(event.getMember().getIdLong()) - amu);
            event.getChannel().sendMessageFormat("Gave %d cash to %s. You now have %d cash", amu, member.getAsMention(),
                    GetData.getAmount(event.getMember().getIdLong())).queue();
        }
    }
}
