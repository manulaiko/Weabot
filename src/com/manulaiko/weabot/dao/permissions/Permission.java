package com.manulaiko.weabot.dao.permissions;

import com.manulaiko.weabot.launcher.Main;

/**
 * Permission object.
 *
 * Represents an permission from the database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Permission
{
    /**
     * Permission ID.
     */
    public int id;

    /**
     * Permission name.
     */
    public String name;

    /**
     * Permission rank.
     */
    public int rank;

    /**
     * Permission description.
     */
    public String description;

    /**
     * Constructor.
     *
     * @param id   Permission ID.
     * @param name Permission name.
     * @param rank Permission rank.
     */
    public Permission(int id, String name, int rank, String description)
    {
        this.id          = id;
        this.name        = name;
        this.rank        = rank;
        this.description = description;
    }

    /**
     * Saves a new permission in the database.
     */
    public void save()
    {
        if(this.id == 0) {
            this.id = Main.database.insert(
                    "INSERT INTO `permissions` (`name`, `rank`, `description`) VALUES (?, ?, ?)",
                    this.name,
                    this.rank,
                    this.description
            );

            return;
        }

        Main.database.update(
                "UPDATE `users` SET `name`=?, `rank`=?, `description`=? WHERE `id`=?",
                this.name,
                this.rank,
                this.description,
                this.id
        );
    }

    /**
     * Checks that this permission is the same as `permission`.
     *
     * @param permission Permission to compare.
     *
     * @return Whether `this == permission`.
     */
    public boolean equals(Object permission)
    {
        if(!(permission instanceof Permission)) {
            return false;
        }

        return this.id == ((Permission)permission).id;
    }
}
