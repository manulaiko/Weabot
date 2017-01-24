package com.manulaiko.weabot.dao.scrappers;

import com.manulaiko.weabot.launcher.Main;

/**
 * Image Scrapper object.
 *
 * Represents an image scrapper from the database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Scrapper
{
    /**
     * Scrapper ID.
     */
    public int id;

    /**
     * Scrapper channel.
     */
    public String channelID;

    /**
     * Scrapper path.
     */
    public String path;

    /**
     * Constructor.
     *
     * @param id        Image ID.
     * @param channelID Image link.
     * @param path      Image category.
     */
    public Scrapper(int id, String channelID, String path)
    {
        this.id        = id;
        this.channelID = channelID;
        this.path      = path;
    }

    /**
     * Saves a new image scrapper in the database.
     */
    public void save()
    {
        if(this.id == 0) {
            this.id = Main.database.insert(
                    "INSERT INTO `scrappers` (`channel_id`, `path`) VALUES (?, ?)",
                    this.channelID,
                    this.path
            );

            return;
        }

        Main.database.update(
                "UPDATE `scrappers` SET `channel_id`=? `path`=? WHERE `id`=?",
                this.channelID,
                this.path,
                this.id
        );
    }
}
