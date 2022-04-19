package Harsho.Hype.Bot.Commands.FunCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MemeCommand extends ListenerAdapter {

    private static String prefix;

    public static String help(String prefix) {
        return prefix
                + "meme [number] - sends a random meme times the number mentioned(limit is 5). If number is not mentioned it will only send 1 meme.";
    }

    private static void post(MessageReceivedEvent event) {
        String postLink, subreddit, title, author, image;
        boolean nsfw = false, spoiler = false;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        try {
            URL url = new URL("https://meme-api.herokuapp.com/gimme");
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
                postLink = jsonObject.getString("postLink");
                subreddit = jsonObject.getString("subreddit");
                title = jsonObject.getString("title");
                image = jsonObject.getString("url");
                author = jsonObject.getString("author");
                nsfw = jsonObject.getBoolean("nsfw");
                spoiler = jsonObject.getBoolean("spoiler");
                if (!spoiler) {
                    if (event.getTextChannel().isNSFW() && !nsfw) {
                        embedBuilder.setTitle(title, postLink);
                        embedBuilder.setAuthor("by " + author);
                        embedBuilder.setColor(Color.RED);
                        embedBuilder.setFooter("from r/" + subreddit);
                        embedBuilder.setImage(image);
                        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                        embedBuilder.clear();
                    } else if (!event.getTextChannel().isNSFW() && nsfw) {
                        event.getChannel().sendMessage("Try again").queue();
                    } else if (event.getTextChannel().isNSFW() && nsfw) {
                        embedBuilder.setTitle(title, postLink);
                        embedBuilder.setAuthor("by " + author);
                        embedBuilder.setColor(Color.RED);
                        embedBuilder.setFooter("from r/" + subreddit);
                        embedBuilder.setImage(image);
                        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                        embedBuilder.clear();
                    } else if (!event.getTextChannel().isNSFW() && !nsfw) {
                        embedBuilder.setTitle(title, postLink);
                        embedBuilder.setAuthor("by " + author);
                        embedBuilder.setColor(Color.RED);
                        embedBuilder.setFooter("from r/" + subreddit);
                        embedBuilder.setImage(image);
                        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                        embedBuilder.clear();
                    }
                } else {
                    event.getChannel().sendMessage("Try again").queue();
                }
            }
        } catch (Exception ea) {
            ea.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        int num = 0;
        if (message[0].equalsIgnoreCase(prefix + "meme")) {
            try {
                num = Integer.parseInt(message[1]);
            } catch (Exception e) {
                num = 1;
            }
            int i = 0;
            if (num < 6) {
                do {
                    post(event);
                    i++;
                } while (i < num);
            } else {
                event.getChannel().sendMessage("You cant get more than 5 memes").queue();
            }
        }
    }
}
