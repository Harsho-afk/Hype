package Commands.FunCommands;

import Systems.Storage;
import Database.GetData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DogCommand extends ListenerAdapter {
    private static String prefix;

    public static String help(String prefix) {
        return prefix + "dog - sends a random dog pic.";
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        String sURL;
        if (message[0].equalsIgnoreCase(prefix + "dog")) {
            try {
                URL url = new URL("https://dog.ceo/api/breeds/image/random");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    throw new RuntimeException("HttpResponseCode: " + responseCode);
                } else {
                    Scanner scanner = new Scanner(url.openStream());
                    StringBuilder inline = new StringBuilder();
                    while (scanner.hasNext()) {
                        inline.append(scanner.nextLine());
                    }
                    scanner.close();
                    JSONObject jsonObject = new JSONObject(String.valueOf(inline));
                    sURL = jsonObject.getString("message");
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(Color.red);
                    embedBuilder.setImage(sURL);
                    event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                    embedBuilder.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
