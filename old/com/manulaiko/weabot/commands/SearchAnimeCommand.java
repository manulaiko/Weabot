package com.manulaiko.weabot.commands;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.utils.WebClient;
import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.*;
import org.xml.sax.InputSource;


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

        boolean all = false;

        String anime = "";
        for(int i = 1; i < args.length; i++) {
            if(args[i].equalsIgnoreCase("-all")) {
                all = true;

                continue;
            }

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

        ArrayList<String> messages = this.parseXML(response);
        if(messages.size() > 1) {
            event.getTextChannel().sendMessage("Found "+ messages.size() +" animes!");

            if(!all) {
                event.getTextChannel().sendMessage("Use `&search_anime -all "+ anime +"` for showing all results.");
            }
        }

        for(String message : messages) {
            event.getTextChannel().sendMessage(message);

            if(!all) {
                break;
            }
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
        } catch(Exception e) {
            Console.println("Couldn't execute GET request!");
            Console.println(e.getMessage());
        }

        return response;
    }

    /**
     * Parses a XML.
     *
     * @param xml XML to parse
     *
     * @return Messages to send
     */
    public ArrayList<String> parseXML(String xml)
    {
        ArrayList<String> messages = new ArrayList<>();

        Document doc = this.loadXML(xml);

        NodeList entries = doc.getElementsByTagName("entry");
        Console.debug(entries.getLength());

        for(int i = 0; i < entries.getLength(); i++) {
            Console.debug("Entry "+ i);
            String  message = "";
            Element entry   = (Element)entries.item(i);

            message += entry.getElementsByTagName("image").item(0).getTextContent()    +"\n\n";
            message += "**"+ entry.getElementsByTagName("title").item(0).getTextContent()    +"**\n";
            message += entry.getElementsByTagName("episodes").item(0).getTextContent() +" episode(s), "+ entry.getElementsByTagName("score").item(0).getTextContent() +" score, "+ entry.getElementsByTagName("type").item(0).getTextContent() +".\n\n";
            message += this._parseSynopsis(entry.getElementsByTagName("synopsis").item(0).getTextContent());

            messages.add(message);
        }

        return messages;
    }

    /**
     * Cleans and returns synopsis.
     *
     * @param synopsis Synopsis to parse.
     *
     * @return Cleaned and formatted synopsis.
     */
    private String _parseSynopsis(String synopsis)
    {
        synopsis = StringEscapeUtils.unescapeXml(synopsis);

        return StringEscapeUtils.unescapeHtml4(synopsis)
                                .replaceAll("<br />", "\n")
                                .replaceAll("\\[/?i]", "*");
    }

    /**
     * Parses a XML
     *
     * @param xml XML to parse.
     *
     * @return Parsed XML file.
     */
    public Document loadXML(String xml)
    {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            InputSource input = new InputSource(new StringReader(xml));

            Document doc = dBuilder.parse(input);

            doc.normalizeDocument();

            return doc;
        } catch(Exception e) {
            Console.debug(e.getMessage());
            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
