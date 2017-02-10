package com.manulaiko.weabot.dao.images;

import java.util.ArrayList;
import java.util.List;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.dao.categories.Category;
import com.manulaiko.weabot.dao.categories.Factory;
import com.manulaiko.weabot.launcher.Main;

/**
 * Image object.
 *
 * Represents an image from the database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Image
{
    /**
     * Image ID.
     */
    public int id;

    /**
     * Image link.
     */
    public String link;

    /**
     * Categories.
     */
    public List<Category> categories = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param id         Image ID.
     * @param link       Image link.
     * @param categories Categories
     */
    public Image(int id, String link, List<Category> categories)
    {
        this.id         = id;
        this.link       = link;
        this.categories = categories;
    }

    /**
     * Saves a new image in the database.
     */
    public void save()
    {
        if(this.id == 0) {
            this.id = Main.database.insert(
                    "INSERT INTO `images` (`link`) VALUES (?)",
                    this.link
            );

            return;
        }

        Main.database.update(
                "UPDATE `images` SET `link`=? WHERE `id`=?",
                this.link,
                this.id
        );
    }

    /**
     * Adds the image to a category.
     *
     * @param category Category to add the image.
     */
    public void addToCategory(String category)
    {
        this.addToCategory(Factory.find(category));
    }

    /**
     * Adds the image to a category.
     *
     * @param category Category to add the image.
     */
    public void addToCategory(Category category)
    {
        if(this.categories.contains(category)) {
            return;
        }

        int id = Main.database.update(
                "INSERT INTO `images_categories` (`images_id`, `categories_id`) VALUES (?, ?)",
                this.id,
                category.id
         );

        if(id == 0) {
            Console.println("Couldn't add image `"+ this.id +"` to category `"+ category.name +"`!");

            return;
        }

        this.categories.add(category);
    }
}
