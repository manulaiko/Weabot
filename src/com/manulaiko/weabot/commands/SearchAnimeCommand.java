package com.manulaiko.weabot.commands;

import java.beans.XMLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.utils.WebClient;
import com.manulaiko.weabot.launcher.Main;
import com.sun.org.apache.xerces.internal.parsers.XMLParser;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Search anime command.
 *
 * Search for an anime using MyAnimeList API.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class SearchAnimeCommand extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "search_anime";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        return "Search for an anime using MyAnimeList API.\n" +
                "Usage:\n" +
                "    "+ this.getFullName() +" [anime]\n" +
                "Example:\n" +
                "    "+ this.getFullName() +" shigatsu wa kimi no uso";
    }

    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        if(!Main.configuration.getBoolean("myanimelist.enabled")) {
            event.getTextChannel().sendMessage("This command requires the MyAnimeList settings properly configured!");

            return;
        }

        String anime = "";
        for(int i = 1; i < args.length; i++) {
            anime += args[i] +" ";
        }
        anime = anime.substring(0, anime.length() - 1); // Last whitespace

        if(anime.isEmpty()) {
            event.getTextChannel().sendMessage("```\n"+ this.getDescription() +"```");

            return;
        }

        String response = this._search(anime);
        if(response.isEmpty()) {
            event.getTextChannel().sendMessage("The anime does not exist :/");

            return;
        } else if(response.contains("Invalid credentials")) {
            event.getTextChannel().sendMessage("Invalid MyAnimeList credentials.");

            return;
        }

    }

    /**
     * Search for an anime in MyAnimeList API.
     *
     * @param anime Anime to search.
     *
     * @return API Response.
     */
    private String _search(String anime)
    {
        String response = "";

        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("q", URLEncoder.encode(anime, "UTF-8"));

            WebClient wc = new WebClient(
                    Main.configuration.getString("myanimelist.username"),
                    Main.configuration.getString("myanimelist.password")
            );

            response = wc.get("https://myanimelist.net/api/anime/search.xml", params);
            Console.println(response);
        } catch(Exception e) {
            Console.println("Couldn't execute GET request!");
            Console.println(e.getMessage());
        }

        return response;
    }
}
