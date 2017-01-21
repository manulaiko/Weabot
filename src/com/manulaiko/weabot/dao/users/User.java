package com.manulaiko.weabot.dao.users;

import java.util.ArrayList;
import java.util.List;

import com.manulaiko.tabitha.Console;
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
     * User rank.
     */
    public int rank;

    /**
     * Permissions.
     */
    public List<Permission> permissions = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param id        User ID.
     * @param name      User name.
     * @param discordID User's discord ID.
     * @param joinDate  Join date.
     * @param rank      User rank.
     */
    public User(int id, String name, String discordID, String joinDate, int rank)
    {
        this.id        = id;
        this.name      = name;
        this.discordID = discordID;
        this.joinDate  = joinDate;
        this.rank      = rank;
    }

    /**
     * Saves a new user in the database.
     */
    public void save()
    {
        if(this.id == 0) {
            this.id = Main.database.insert(
                    "INSERT INTO `users` (`name`, `discord_id`, `join_date`) VALUES (?, ?, ?)",
                    this.name,
                    this.discordID,
                    this.joinDate
            );

            Console.println("User created!");
            Console.println("Name: "+ this.name +", ID: "+ this.id);

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

    /**
     * Checks that users can pet me.
     *
     * @return `true` if no users can pet me, `false` if not.
     */
    public boolean noneCanPetMe()
    {
        for(Permission permission : this.permissions) {
            if(permission.name.equalsIgnoreCase("none_can_pet_me")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks that I can modify other's configuration.
     *
     * @return `true` if I can change other's configuration, `false` if not.
     */
    public boolean canChangeOthersConfig()
    {
        for(Permission permission : this.permissions) {
            if(permission.name.equalsIgnoreCase("can_change_others_config")) {
                return true;
            }
        }

        return false;
    }
}
