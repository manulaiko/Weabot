package com.manulaiko.weabot.commands;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.utils.WebClient;
import com.manulaiko.weabot.launcher.Main;

import net.dv8tion.jda.core.EmbedBuilder;
import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 * Animu command.
 *
 * Search for animes with MAL api.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Animu extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "animu";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Search for animes with MAL api.";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    @Override
    public String getUsage()
    {
        return this.getFullName() +" ([-all]) [name]";
    }

    /**
     * Executes the command.
     *
     * @param e    Event that fired the command.
     * @param args Command arguments.
     */
    @Override
    public void execute(MessageReceivedEvent e, String[] args)
    {
        if(!Main.configuration.getBoolean("myanimelist.enabled")) {
            e.getTextChannel().sendMessage("This command requires the MyAnimeList settings properly configured!").queue();

            return;
        }

        if(args.length <= 1) {
            super.printUsage(e.getTextChannel());

            return;
        }

        boolean all   = false;
        String  anime = "";
        for(int i = 1; i < args.length; i++) {
            if(args[i].equalsIgnoreCase("-all")) {
                all = true;

                continue;
            }

            anime += args[i] +" ";
        }
        anime = anime.substring(0, anime.length() - 1); // Last whitespace

        if(anime.isEmpty()) {
            super.printUsage(e.getTextChannel());

            return;
        }

        List<Anime> animes = this._search(anime);

        if(animes.size() == 0) {
            e.getTextChannel().sendMessage("No anime found with name `"+ anime +"`!").queue();

            return;
        }

        int i = 0;
        for(Anime a : animes) {
            EmbedBuilder b = new EmbedBuilder();
            b.setThumbnail(a.image);
            b.setUrl(a.link);

            b.addField("Title", a.title, true);
            b.addField("Rate", a.rate +"", true);
            b.addField("Episodes", a.episodes +" ("+ a.type +")", true);
            b.addField("Status", a.status +" (From "+ a.start +" to "+ a.end +")", true);
            b.addField("Synopsis", a.description, false);

            e.getTextChannel().sendMessage(b.build()).queue();

            i++;

            if(
                !all &&
                i < animes.size()
            ) {
                e.getTextChannel().sendMessage("There are "+ (animes.size() - i) +" more results, use `-all` to show them all.").queue();

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
    private List<Anime> _search(String anime)
    {
        List<Anime> animes = new ArrayList<>();

        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("q", URLEncoder.encode(anime, "UTF-8"));

            WebClient wc = new WebClient(
                    Main.configuration.getString("myanimelist.username"),
                    Main.configuration.getString("myanimelist.password")
            );

            String response = wc.get("https://myanimelist.net/api/anime/search.xml", params);

            animes = this.parseXML(response);
        } catch(Exception e) {
            Console.println("Couldn't execute GET request!");
            Console.println(e.getMessage());
        }

        return animes;
    }

    /**
     * Parses a XML.
     *
     * @param xml XML to parse
     *
     * @return Messages to send
     */
    public ArrayList<Anime> parseXML(String xml)
    {
        ArrayList<Anime> animes = new ArrayList<>();

        Document doc = this.loadXML(xml);

        NodeList entries = doc.getElementsByTagName("entry");
        Console.debug(entries.getLength());

        for(int i = 0; i < entries.getLength(); i++) {
            Element entry   = (Element)entries.item(i);

            Anime anime = new Anime(
                    Integer.parseInt(entry.getElementsByTagName("id").item(0).getTextContent()),
                    entry.getElementsByTagName("image").item(0).getTextContent(),
                    entry.getElementsByTagName("title").item(0).getTextContent(),
                    Integer.parseInt(entry.getElementsByTagName("episodes").item(0).getTextContent()),
                    entry.getElementsByTagName("type").item(0).getTextContent(),
                    entry.getElementsByTagName("status").item(0).getTextContent(),
                    entry.getElementsByTagName("start_date").item(0).getTextContent(),
                    entry.getElementsByTagName("end_date").item(0).getTextContent(),
                    this._parseSynopsis(entry.getElementsByTagName("synopsis").item(0).getTextContent()),
                    Float.parseFloat(entry.getElementsByTagName("score").item(0).getTextContent())
            );

            animes.add(anime);
        }

        return animes;
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

    /**
     * Represents an anime entry.
     *
     * @author Manulaiko <manulaiko@gmail.com>
     */
    public class Anime
    {
        /**
         * Anime image.
         */
        public String image;

        /**
         * Anime title.
         */
        public String title;

        /**
         * Episodes.
         */
        public int episodes;

        /**
         * Anime type.
         */
        public String type;

        /**
         * Anime status.
         */
        public String status;

        /**
         * Start airing date.
         */
        public String start;

        /**
         * End airing date.
         */
        public String end;

        /**
         * Description.
         */
        public String description;

        /**
         * Rate.
         */
        public float rate;

        /**
         * MAL link
         */
        public String link;

        /**
         * Constructor.
         *
         * @param id          Anime id.
         * @param image       Anime image.
         * @param title       Anime title.
         * @param episodes    Episodes.
         * @param type        Anime type.
         * @param status      Anime status.
         * @param start       Start airing date.
         * @param end         End airing date.
         * @param description Description.
         * @param rate        Rate.
         */
        public Anime(int id, String image, String title, int episodes, String type, String status, String start, String end, String description, float rate)
        {
            this.image       = image;
            this.title       = title;
            this.episodes    = episodes;
            this.type        = type;
            this.status      = status;
            this.start       = start;
            this.end         = end;
            this.rate        = rate;
            this.link        = "https://myanimelist.net/anime/"+ id;
            this.description = description;

            String readmore = "...\nRead more at "+ this.link;
            if((this.description.length() + readmore.length()) >= 1000) {
                this.description = this.description.substring(0, (1000 - readmore.length())) + readmore;
            }
        }
    }
}
