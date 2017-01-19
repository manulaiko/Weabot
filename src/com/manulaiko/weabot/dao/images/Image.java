package com.manulaiko.weabot.dao.images;

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
     * Image category.
     */
    public String category;

    /**
     * Constructor.
     *
     * @param id        Image ID.
     * @param link      Image link.
     * @param category Image category.
     */
    public Image(int id, String link, String category)
    {
        this.id       = id;
        this.link     = link;
        this.category = category;
    }

    /**
     * Saves a new image in the database.
     */
    public void save()
    {
        if(this.id == 0) {
            this.id = Main.database.insert(
                    "INSERT INTO `images` (`link`, `category`) VALUES (?, ?)",
                    this.link,
                    this.category
            );

            return;
        }

        Main.database.update(
                "UPDATE `images` SET `link`=? `category`=? WHERE `id`=?",
                this.link,
                this.category,
                this.id
        );
    }
}
