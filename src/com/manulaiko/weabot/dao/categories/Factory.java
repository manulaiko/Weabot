package com.manulaiko.weabot.dao.categories;

import java.sql.ResultSet;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.launcher.Main;

/**
 * Categories factory.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Factory
{
    /**
     * Finds a category from its id.
     *
     * @param id Category ID.
     *
     * @return Category from database.
     */
    public static Category find(int id)
    {
        Category c = null;

        ResultSet rs = Main.database.query("SELECT * FROM `categories` WHERE `id`=?", id);
        try {
            if(!rs.isBeforeFirst()) {
                Console.println("Category not found!");

                return Factory.create(id, "", "");
            }

            c = new Category(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")
            );
        } catch(Exception e) {
            Console.println("Couldn't build category!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return c;
    }
    /**
     * Finds a category from its name.
     *
     * @param name Category name.
     *
     * @return Category from database.
     */
    public static Category find(String name)
    {
        Category c = null;

        ResultSet rs = Main.database.query("SELECT * FROM `categories` WHERE `name`=?", name);
        try {
            if(!rs.isBeforeFirst()) {
                Console.println("Category not found!");

                return Factory.create(0, name, "");
            }

            c = new Category(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")
            );
        } catch(Exception e) {
            Console.println("Couldn't build category!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return c;
    }

    /**
     * Deletes a category from the database.
     *
     * @param category Category to delete.
     *
     * @return `true` if the category was deleted successfully, `false` if not.
     */
    public static boolean delete(Category category)
    {
        int rows = Main.database.update("DELETE FROM `categories` WHERE `id`=?", category.id);

        return (rows > 0);
    }

    /**
     * Creates and returns a new category.
     *
     * @param id          Category ID.
     * @param name        Category name.
     * @param description Category description.
     *
     * @return Category from database.
     */
    public static Category create(int id, String name, String description)
    {
        Category c = new Category(id, name, description);

        c.save();

        return c;
    }

    /**
     * Creates and returns a new category.
     *
     * @param name        Category name.
     * @param description Category description.
     *
     * @return Category from database.
     */
    public static Category create(String name, String description)
    {
        return Factory.create(0, name, description);
    }

    /**
     * Creates and returns a new category.
     *
     * @param name Category name.
     *
     * @return Category from database.
     */
    public static Category create(String name)
    {
        return Factory.create(0, name, "");
    }
}
