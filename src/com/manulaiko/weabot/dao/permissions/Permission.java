package com.manulaiko.weabot.dao.permissions;

import com.manulaiko.weabot.launcher.Main;

/**
 * User object.
 *
 * Represents an user from the database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Permission
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
     * Constructor.
     *
     * @param id   User ID.
     * @param name User name.
     */
    public Permission(int id, String name)
    {
        this.id   = id;
        this.name = name;
    }

    /**
     * Saves a new user in the database.
     */
    public void save()
    {
        if(this.id == 0) {
            this.id = Main.database.update(
                    "INSERT INTO `permissions` (`name`) VALUES (?)",
                    this.name
            );

            return;
        }

        Main.database.update(
                "UPDATE `users` SET `name`=? WHERE `id`=?",
                this.name,
                this.id
        );
    }
}
