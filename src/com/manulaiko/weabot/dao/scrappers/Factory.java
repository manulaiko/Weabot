package com.manulaiko.weabot.dao.scrappers;

import java.io.File;
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
     * Finds and returns all scrappers from its channel.
     *
     * @param channel Scrapper channel.
     *
     * @return Scrappers from database.
     */
    public static List<Scrapper> byChannel(TextChannel channel)
    {
        return Factory.byChannel(channel.getId());
    }

    /**
     * Finds and returns all scrappers from its channel ID.
     *
     * @param channelID Scrapper channel ID.
     *
     * @return Scrappers from database.
     */
    public static List<Scrapper> byChannel(String channelID)
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
     * Finds and returns all scrappers from its path.
     *
     * @param path Scrapper path.
     *
     * @return Scrappers from database.
     */
    public static List<Scrapper> byPath(File path)
    {
        return Factory.byChannel(path.getAbsolutePath());
    }

    /**
     * Finds and returns all scrappers from its path.
     *
     * @param path Scrapper path.
     *
     * @return Scrappers from database.
     */
    public static List<Scrapper> byPath(String path)
    {
        List<Scrapper> scrappers = new ArrayList<>();

        ResultSet rs = Main.database.query("SELECT * FROM `scrappers` WHERE `path`=?", path);
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
     * Returns all scrappers.
     *
     * @return All scrappers from database
     */
    public static List<Scrapper> all()
    {
        List<Scrapper> scrappers = new ArrayList<>();

        ResultSet rs = Main.database.query("SELECT * FROM `scrappers`");
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
     * Deletes a scrapper from the database.
     *
     * @param scrapper Scrapper to delete.
     *
     * @return `true` if the scrapper was deleted successfully, `false` if not.
     */
    public static boolean delete(Scrapper scrapper)
    {
        int rows = Main.database.update("DELETE * FROM `scrappers` WHERE `id`=?", scrapper.id);

        return (rows > 0);
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
