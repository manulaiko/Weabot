package com.manulaiko.weabot.dao.stats;

import com.manulaiko.weabot.launcher.Main;

/**
 * Stats object.
 *
 * Represents a stat from the database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Stat
{
    /**
     * Stats ID.
     */
    public String id;

    /**
     * Stats text.
     */
    public String text;

    /**
     * Constructor.
     *
     * @param id   Stats ID.
     * @param text Stats text.
     */
    public Stat(String id, String text)
    {
        this.id   = id;
        this.text = text;
    }

    /**
     * Saves a new message in the database.
     */
    public void save()
    {
        Main.database.update(
                "UPDATE `messages` SET `text`=? WHERE `id`=?",
                this.text,
                this.id
        );
    }
}
