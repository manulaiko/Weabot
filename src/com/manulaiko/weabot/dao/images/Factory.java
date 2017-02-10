package com.manulaiko.weabot.dao.images;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.dao.categories.Category;
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

                return Factory.create(id, "", new ArrayList<>());
            }

            i = new Image(
                    rs.getInt("id"),
                    rs.getString("link"),
                    new ArrayList<>()
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

        Category cat = com.manulaiko.weabot.dao.categories.Factory.find(category);

        if(cat == null) {
            return images;
        }

        ResultSet rs = Main.database.query("SELECT * FROM `images_categories` WHERE `categories_id`=?", cat.id);
        try {
            while(rs.next()) {
                images.add(Factory.find(rs.getInt("images_id")));
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
     * Deletes an image from the database.
     *
     * @param image Image to delete.
     *
     * @return `true` if the image was deleted successfully, `false` if not.
     */
    public static boolean delete(Image image)
    {
        int rows = Main.database.update("DELETE FROM `images` WHERE `id`=?", image.id);

        return (rows > 0);
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

        if(images.size() == 0) {
            return null;
        }

        int i = ThreadLocalRandom.current().nextInt(0, images.size());

        return images.get(i);
    }

    /**
     * Creates and returns a new image.
     *
     * @param id         Image ID.
     * @param link       Image link.
     * @param categories Image categories.
     *
     * @return Image from database.
     */
    public static Image create(int id, String link, List<Category> categories)
    {
        Image i = new Image(id, link, categories);

        i.save();

        for(Category c : categories) {
            Main.database.insert(
                    "INSERT INTO `images_categories` (`images_id`, `categories_id`) VALUES (?, ?)",
                    i.id,
                    c.id
             );
        }

        return i;
    }

    /**
     * Creates and returns a new image.
     *
     * @param link       Image link.
     * @param categories Image categories.
     *
     * @return Image from database.
     */
    public static Image create(String link, List<Category> categories)
    {
        return Factory.create(0, link, categories);
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
        return Factory.create(0, link, new ArrayList<>());
    }
}
