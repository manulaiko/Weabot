package com.manulaiko.weabot.dao.scrappers;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * Images Scrapper factory.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Factory
{
    /**
     * Finds an scrapper from its id.
     *
     * @param id Scrapper ID.
     *
     * @return Scrapper from database.
     */
    public static Scrapper find(int id)
    {
        Scrapper s = null;

        ResultSet rs = Main.database.query("SELECT * FROM `scrappers` WHERE `id`=?", id);
        try {
            if(!rs.isBeforeFirst()) {
                Console.println("Scrapper not found!");

                return null;
            }

            s = new Scrapper(
                    rs.getInt("id"),
                    rs.getString("channel_id"),
                    rs.getString("path")
            );
        } catch(Exception e) {
            Console.println("Couldn't build scrapper!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return s;
    }

    /**
     * Finds and returns an scrapper from its channel.
     *
     * @param channel Scrapper channel.
     *
     * @return Scrappers from database.
     */
    public static List<Scrapper> find(TextChannel channel)
    {
        return Factory.find(channel.getId());
    }


    /**
     * Finds and returns an scrapper from its channel ID.
     *
     * @param channelID Scrapper channel ID.
     *
     * @return Scrappers from database.
     */
    public static List<Scrapper> find(String channelID)
    {
        List<Scrapper> scrappers = new ArrayList<>();

        ResultSet rs = Main.database.query("SELECT * FROM `scrappers` WHERE `channel_id`=?", channelID);
        try {
            while(rs.next()) {
                Scrapper s = new Scrapper(
                        rs.getInt("id"),
                        rs.getString("channel_id"),
                        rs.getString("path")
                );

                scrappers.add(s);
            }
        } catch(Exception e) {
            Console.println("Couldn't build scrapper!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return scrappers;
    }

    /**
     * Creates a new scrapper.
     *
     * @param channel Channel to scrap.
     * @param path    Path to save the images.
     *
     * @return Created scrapper.
     */
    public static Scrapper create(TextChannel channel, String path)
    {
        Scrapper s = new Scrapper(0, channel.getId(), path);

        s.save();

        return s;
    }
}
