package com.manulaiko.weabot.dao.images;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.launcher.Main;

/**
 * Images factory.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Factory
{
    /**
     * Finds an image from its id.
     *
     * @param id Image ID.
     *
     * @return Image from database.
     */
    public static Image find(int id)
    {
        Image i = null;

        ResultSet rs = Main.database.query("SELECT * FROM `images` WHERE `id`=?", id);
        try {
            if(!rs.isBeforeFirst()) {
                Console.println("Image not found!");

                return Factory.create(id, "", "");
            }

            i = new Image(
                    rs.getInt("id"),
                    rs.getString("link"),
                    rs.getString("category")
            );
        } catch(Exception e) {
            Console.println("Couldn't build image!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return i;
    }

    /**
     * Finds and returns an image from its category.
     *
     * @param category Image category.
     *
     * @return Images from database.
     */
    public static List<Image> findByCategory(String category)
    {
        List<Image> images = new ArrayList<>();

        ResultSet rs = Main.database.query("SELECT * FROM `images` WHERE `category`=?", category);
        try {
            while(rs.next()) {
                Image i = new Image(
                        rs.getInt("id"),
                        rs.getString("link"),
                        rs.getString("category")
                );

                images.add(i);
            }
        } catch(Exception e) {
            Console.println("Couldn't build image!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return images;
    }

    /**
     * Builds and returns a random image.
     *
     * @param category Image category.
     *
     * @return Random image from database.
     */
    public static Image getRandomImage(String category)
    {
        List<Image> images = Factory.findByCategory(category);

        int i = ThreadLocalRandom.current().nextInt(0, images.size());

        return images.get(i);
    }

    /**
     * Creates and returns a new image.
     *
     * @param id       Image ID.
     * @param link     Image link.
     * @param category Image category.
     *
     * @return Image from database.
     */
    public static Image create(int id, String link, String category)
    {
        Image i = new Image(id, link, category);

        i.save();

        return i;
    }

    /**
     * Creates and returns a new image.
     *
     * @param link     Image link.
     * @param category Image category.
     *
     * @return Image from database.
     */
    public static Image create(String link, String category)
    {
        return Factory.create(0, link, category);
    }

    /**
     * Creates and returns a new image.
     *
     * @param link Image link.
     *
     * @return Image from database.
     */
    public static Image create(String link)
    {
        return Factory.create(0, link, "");
    }
}
