package com.manulaiko.weabot.dao.users;

import java.util.List;

import com.manulaiko.weabot.dao.permissions.Permission;
import com.manulaiko.weabot.launcher.Main;

/**
 * User object.
 *
 * Represents an user from the database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class User
{
    /**
     * User ID.
     */
    public int id;

    /**
     * User name.
     */
    public String name;

    /**
     * User's discord ID.
     */
    public String discordID;

    /**
     * Join date.
     */
    public String joinDate;

    /**
     * Permissions.
     */
    public List<Permission> permissions;

    /**
     * Constructor.
     *
     * @param id        User ID.
     * @param name      User name.
     * @param discordID User's discord ID.
     * @param joinDate  Join date.
     */
    public User(int id, String name, String discordID, String joinDate)
    {
        this.id        = id;
        this.name      = name;
        this.discordID = discordID;
        this.joinDate   = joinDate;
    }

    /**
     * Saves a new user in the database.
     */
    public void save()
    {
        if(this.id == 0) {
            this.id = Main.database.update(
                    "INSERT INTO `users` (`name`, `discord_id`, `join_date`) VALUES (?, ?, ?)",
                    this.name,
                    this.discordID,
                    this.joinDate
            );

            return;
        }

        Main.database.update(
                "UPDATE `users` SET `name`=? `discord_id`=? `join_date`=? WHERE `id`=?",
                this.name,
                this.discordID,
                this.joinDate,
                this.id
        );
    }
}
