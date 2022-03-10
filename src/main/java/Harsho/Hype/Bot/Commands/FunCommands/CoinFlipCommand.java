package Harsho.Hype.Bot.Commands.FunCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Systems.CoinFlipSys;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CoinFlipCommand extends ListenerAdapter {
    private static String prefix;

    public static String help() {
        return prefix + "cf [heads or tails] [amount] - heads or tails.";
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        if (message[0].equalsIgnoreCase(prefix + "cf") || message[0].equalsIgnoreCase(prefix + "coinFlip")) {
            CoinFlipSys.getInstance();
            String response = CoinFlipSys.coinFlipSys(event.getMember(), message, event);
            CoinFlipSys.INSTANCE.coinFlipManagers.remove(event.getGuild(), event.getMember().getIdLong());
            event.getChannel().sendMessage(response).queue();
        }
    }
}
