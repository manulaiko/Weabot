package com.manulaiko.weabot.dao.messages;

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
     * Message category.
     */
    public String category;

    /**
     * Constructor.
     *
     * @param id       Message ID.
     * @param text     Message text.
     * @param category Message category.
     */
    public Message(int id, String text, String category)
    {
        this.id       = id;
        this.text     = text;
        this.category = category;
    }

    /**
     * Saves a new message in the database.
     */
    public void save()
    {
        if(this.id == 0) {
            this.id = Main.database.insert(
                    "INSERT INTO `messages` (`text`, `category`) VALUES (?, ?)",
                    this.text,
                    this.category
            );

            return;
        }

        Main.database.update(
                "UPDATE `messages` SET `text`=? `category`=? WHERE `id`=?",
                this.text,
                this.category,
                this.id
        );
    }
}
