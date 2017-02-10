package com.manulaiko.weabot.dao.categories;

import com.manulaiko.weabot.launcher.Main;

/**
 * Category object.
 *
 * Represents a category from the database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Category
{
    /**
     * Category ID.
     */
    public int id;

    /**
     * Category name.
     */
    public String name;

    /**
     * Category description.
     */
    public String description;

    /**
     * Constructor.
     *
     * @param id          Category ID.
     * @param name        Category name.
     * @param description Category description.
     */
    public Category(int id, String name, String description)
    {
        this.id       = id;
        this.name     = name;
        this.description = description;
    }

    /**
     * Saves a new category in the database.
     */
    public void save()
    {
        if(this.id == 0) {
            this.id = Main.database.insert(
                    "INSERT INTO `categories` (`name`, `description`) VALUES (?, ?)",
                    this.name,
                    this.description
            );

            return;
        }

        Main.database.update(
                "UPDATE `categories` SET `name`=? `description`=? WHERE `id`=?",
                this.name,
                this.description,
                this.id
        );
    }
}
