package com.manulaiko.weabot.dao.messages;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.dao.categories.Category;
import com.manulaiko.weabot.launcher.Main;

/**
 * Messages factory.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Factory
{
    /**
     * Finds a message from its id.
     *
     * @param id Message ID.
     *
     * @return Message from database.
     */
    public static Message find(int id)
    {
        Message m = null;

        ResultSet rs = Main.database.query("SELECT * FROM `messages` WHERE `id`=?", id);
        try {
            if(!rs.isBeforeFirst()) {
                Console.println("Message not found!");

                return null;
            }

            m = new Message(
                    rs.getInt("id"),
                    rs.getString("text"),
                    new ArrayList<>()
            );
        } catch(Exception e) {
            Console.println("Couldn't build message!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return m;
    }

    /**
     * Finds and returns all messages from a category.
     *
     * @param category Message category.
     *
     * @return Messages from database.
     */
    public static List<Message> findByCategory(String category)
    {
        List<Message> messages = new ArrayList<>();

        Category cat = com.manulaiko.weabot.dao.categories.Factory.find(category);

        if(cat == null) {
            return messages;
        }

        ResultSet rs = Main.database.query("SELECT * FROM `messages_categories` WHERE `categories_id`=?", cat.id);
        try {
            while(rs.next()) {
                messages.add(Factory.find(rs.getInt("messages_id")));
            }
        } catch(Exception e) {
            Console.println("Couldn't build message!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return messages;
    }

    /**
     * Deletes a message from the database.
     *
     * @param message Message to delete.
     *
     * @return `true` if the message was deleted successfully, `false` if not.
     */
    public static boolean delete(Message message)
    {
        int rows = Main.database.update("DELETE FROM `messages` WHERE `id`=?", message.id);

        return (rows > 0);
    }

    /**
     * Builds and returns a random message.
     *
     * @param category Message category.
     *
     * @return Random message from database.
     */
    public static Message getRandomMessage(String category)
    {
        List<Message> messages = Factory.findByCategory(category);

        if(messages.size() == 0) {
            return null;
        }

        int m = ThreadLocalRandom.current().nextInt(0, messages.size());

        return messages.get(m);
    }

    /**
     * Creates and returns a new message.
     *
     * @param id         Message ID.
     * @param text       Message text.
     * @param categories Message categories.
     *
     * @return message from database.
     */
    public static Message create(int id, String text, List<Category> categories)
    {
        Message m = new Message(id, text, categories);

        m.save();

        for(Category c : categories) {
            Main.database.insert(
                    "INSERT INTO `messages_categories` (`messages_id`, `categories_id`) VALUES (?, ?)",
                    m.id,
                    c.id
            );
        }

        return m;
    }

    /**
     * Creates and returns a new message.
     *
     * @param text       Message text.
     * @param categories Message categories.
     *
     * @return Message from database.
     */
    public static Message create(String text, List<Category> categories)
    {
        return Factory.create(0, text, categories);
    }

    /**
     * Creates and returns a new message.
     *
     * @param text Message text.
     *
     * @return Message from database.
     */
    public static Message create(String text)
    {
        return Factory.create(0, text, new ArrayList<>());
    }
}
