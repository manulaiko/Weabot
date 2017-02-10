package com.manulaiko.weabot.dao.messages;

import java.util.List;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.dao.categories.Category;
import com.manulaiko.weabot.launcher.Main;

/**
 * Message object.
 *
 * Represents a message from the database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Message
{
    /**
     * Message ID.
     */
    public int id;

    /**
     * Message text.
     */
    public String text;

    /**
     * Message categories.
     */
    public List<Category> categories;

    /**
     * Constructor.
     *
     * @param id         Message ID.
     * @param text       Message text.
     * @param categories Message categories.
     */
    public Message(int id, String text, List<Category> categories)
    {
        this.id         = id;
        this.text       = text;
        this.categories = categories;
    }

    /**
     * Saves a new message in the database.
     */
    public void save()
    {
        if(this.id == 0) {
            this.id = Main.database.insert(
                    "INSERT INTO `messages` (`text`) VALUES (?)",
                    this.text
            );

            return;
        }

        Main.database.update(
                "UPDATE `messages` SET `text`=? WHERE `id`=?",
                this.text,
                this.id
        );
    }
    /**
     * Adds the message to a category.
     *
     * @param category Category to add the message.
     */
    public void addToCategory(Category category)
    {
        if(this.categories.contains(category)) {
            return;
        }

        int id = Main.database.update(
                "INSERT INTO `messages_categories` (`messages_id`, `categories_id`) VALUES (?, ?)",
                this.id,
                category.id
        );

        if(id == 0) {
            Console.println("Couldn't add message `"+ this.id +"` to category `"+ category.name +"`!");

            return;
        }

        this.categories.add(category);
    }
}
