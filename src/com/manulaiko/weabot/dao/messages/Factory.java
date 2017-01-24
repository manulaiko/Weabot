package com.manulaiko.weabot.dao.messages;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.manulaiko.tabitha.Console;
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
                    rs.getString("category")
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

        ResultSet rs = Main.database.query("SELECT * FROM `messages` WHERE `category`=?", category);
        try {
            while(rs.next()) {
                Message m = new Message(
                        rs.getInt("id"),
                        rs.getString("text"),
                        rs.getString("category")
                );

                messages.add(m);
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

        int m = ThreadLocalRandom.current().nextInt(0, messages.size());

        return messages.get(m);
    }

    /**
     * Creates and returns a new message.
     *
     * @param id       Message ID.
     * @param text     Message text.
     * @param category Message category.
     *
     * @return message from database.
     */
    public static Message create(int id, String text, String category)
    {
        Message m = new Message(id, text, category);

        m.save();

        return m;
    }

    /**
     * Creates and returns a new message.
     *
     * @param link     Message link.
     * @param category Message category.
     *
     * @return Message from database.
     */
    public static Message create(String link, String category)
    {
        return Factory.create(0, link, category);
    }

    /**
     * Creates and returns a new message.
     *
     * @param link Message link.
     *
     * @return Message from database.
     */
    public static Message create(String link)
    {
        return Factory.create(0, link, "");
    }
}
