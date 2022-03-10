package Harsho.Hype.Bot.Commands.FunCommands;

import Harsho.Hype.Bot.MySQL.GetData;
import Harsho.Hype.Bot.Storage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MemeCommand extends ListenerAdapter {

    private static String prefix;

    public static String help() {
        return prefix + "meme - sends a random meme.";
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        String[] message = event.getMessage().getContentRaw().split("\\s+");
        long guildID = event.getGuild().getIdLong();
        prefix = GetData.getPrefix(guildID);
        Storage.PREFIXES.put(guildID, prefix);
        String postLink, subreddit, title, author, image;
        boolean nsfw, spoiler;
        if (message[0].equalsIgnoreCase(prefix + "meme")) {
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
                        if (event.getChannel().isNSFW() && !nsfw) {
                            embedBuilder.setTitle(title, postLink);
                            embedBuilder.setAuthor("by " + author);
                            embedBuilder.setColor(Color.RED);
                            embedBuilder.setFooter("from r/" + subreddit);
                            embedBuilder.setImage(image);
                            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                            embedBuilder.clear();
                        } else if (!event.getChannel().isNSFW() && nsfw) {
                            event.getChannel().sendMessage("Try again").queue();
                        } else if (event.getChannel().isNSFW() && nsfw) {
                            embedBuilder.setTitle(title, postLink);
                            embedBuilder.setAuthor("by " + author);
                            embedBuilder.setColor(Color.RED);
                            embedBuilder.setFooter("from r/" + subreddit);
                            embedBuilder.setImage(image);
                            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                            embedBuilder.clear();
                        } else if (!event.getChannel().isNSFW() && !nsfw) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
