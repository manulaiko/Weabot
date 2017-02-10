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
     * Checks that I have a certain permission.
     *
     * @param name Permission name.
     *
     * @return `true` if I have the `name` permission, `false` if not.
     */
    public boolean permission(String name)
    {
        for(Permission permission : this.permissions) {
            if(permission.name.equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks that users can pet me.
     *
     * @return `true` if no users can pet me, `false` if not.
     */
    public boolean noneCanPetMe()
    {
        return this.permission("none_can_pet_me");
    }

    /**
     * Checks that users can grope me.
     *
     * @return `true` if no users can grope me, `false` if not.
     */
    public boolean noneCanGropeMe()
    {
        return this.permission("none_can_grope_me");
    }

    /**
     * Checks that I can modify other's configuration.
     *
     * @return `true` if I can change other's configuration, `false` if not.
     */
    public boolean canChangeOthersConfig()
    {
        return this.permission("can_change_others_config");
    }

    /**
     * Checks that I can change my rank (or other's rank)
     *
     * @return `true` if I can change ranks, `false` if not.
     */
    public boolean canChangeRank()
    {
        return this.permission("can_change_rank");
    }

    /**
     * Checks that I can add new images to the database.
     *
     * @return `true` if I can add new images, `false` if not.
     */
    public boolean canAddImages()
    {
        return this.permission("can_add_images");
    }

    /**
     * Checks that I can add new scrappers to the database.
     *
     * @return `true` if I can add new scrappers, `false` if not.
     */
    public boolean canAddScrappers()
    {
        return this.permission("can_add_scrappers");
    }

    /**
     * Checks that I can add new messages to the database.
     *
     * @return `true` if I can add new messages, `false` if not.
     */
    public boolean canAddMessages()
    {
        return this.permission("can_add_messages");
    }

    /**
     * Checks that I can add new permissions to the database.
     *
     * @return `true` if I can add new permissions, `false` if not.
     */
    public boolean canAddPermissions()
    {
        return this.permission("can_add_permissions");
    }

    /**
     * Checks that I can add new categories to the database.
     *
     * @return `true` if I can add new categories, `false` if not.
     */
    public boolean canAddCategories()
    {
        return this.permission("can_add_categories");
    }

    /**
     * Checks that I can delete images from the database.
     *
     * @return `true` if I can delete images, `false` if not.
     */
    public boolean canDeleteImages()
    {
        return this.permission("can_delete_images");
    }

    /**
     * Checks that I can delete scrappers from the database.
     *
     * @return `true` if I can delete scrappers, `false` if not.
     */
    public boolean canDeleteScrappers()
    {
        return this.permission("can_delete_scrappers");
    }

    /**
     * Checks that I can delete messages from the database.
     *
     * @return `true` if I can delete messages, `false` if not.
     */
    public boolean canDeleteMessages()
    {
        return this.permission("can_delete_messages");
    }

    /**
     * Checks that I can delete permissions from the database.
     *
     * @return `true` if I can delete permissions, `false` if not.
     */
    public boolean canDeletePermissions()
    {
        return this.permission("can_delete_permissions");
    }

    /**
     * Checks that I can delete categories from the database.
     *
     * @return `true` if I can delete categories, `false` if not.
     */
    public boolean canDeleteCategories()
    {
        return this.permission("can_delete_categories");
    }
}
