package com.manulaiko.weabot.dao.permissions;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.launcher.Main;

/**
 * Permission factory.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Factory
{

    /**
     * Finds all permissions.
     *
     * @return All permissions.
     */
    public static List<Permission> all()
    {
        List<Permission> permissions = new ArrayList<>();

        ResultSet rs = Main.database.query("SELECT * FROM `users_permissions`");

        try {
            while(rs.next()) {
                permissions.add(Factory.find(rs.getInt("permissions_id")));
            }
        } catch(Exception e) {
            Console.println("Couldn't find user's permission!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return permissions;
    }
    /**
     * Finds a permission from its id.
     *
     * @param id Permission ID
     *
     * @return Permission from database.
     */
    public static Permission find(int id)
    {
        Permission p = null;

        ResultSet rs = Main.database.query("SELECT * FROM `permissions` WHERE `id`='?'", id);
        try {
            if(!rs.isBeforeFirst()) {
                Console.println("Permission with id `"+ id +"` not found!");

                return null;
            }

            p = new Permission(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("rank"),
                    rs.getString("description")
            );
        } catch(Exception e) {
            Console.println("Couldn't build permission!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return p;
    }
    /**
     * Finds an user from its name.
     *
     * @param name Permission name
     *
     * @return Permission from database.
     */
    public static Permission find(String name)
    {
        Permission p = null;

        ResultSet rs = Main.database.query("SELECT * FROM `permissions` WHERE `name`='?'", name);
        try {
            if(!rs.isBeforeFirst()) {
                Console.println("Permission `"+ name +"` not found!");

                return null;
            }

            p = new Permission(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("rank"),
                    rs.getString("description")
            );
        } catch(Exception e) {
            Console.println("Couldn't build permission!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return p;
    }

    /**
     * Creates and returns a new permission.
     *
     * @param name        Permission name.
     * @param rank        Permission rank.
     * @param description Permission description.
     *
     * @return Permission from database.
     */
    public static Permission create(String name, int rank, String description)
    {
        Permission p = new Permission(0, name, rank, description);

        p.save();

        return p;
    }

    /**
     * Finds all permissions from an user.
     *
     * @param id User ID.
     *
     * @return All permissions from user.
     */
    public static List<Permission> findByUserID(int id)
    {
        List<Permission> permissions = new ArrayList<>();

        ResultSet rs = Main.database.query("SELECT * FROM `users_permissions` WHERE `users_id`=?", id);

        try {
            while(rs.next()) {
                permissions.add(Factory.find(rs.getInt("permissions_id")));
            }
        } catch(Exception e) {
            Console.println("Couldn't find user's permission!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return permissions;
    }

    /**
     * Finds a permissions from an user.
     *
     * @param user       User ID.
     * @param permission Permission ID.
     *
     * @return Permission from user (or null).
     */
    public static Permission findByUserID(int user, int permission)
    {
        ResultSet rs = Main.database.query("SELECT * FROM `users_permissions` WHERE `users_id`=? AND permissions_id=?", user, permission);

        try {
            if(rs.isBeforeFirst()) {
                return Factory.find(permission);
            }
        } catch(Exception e) {
            Console.println("Couldn't find user's permission!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Adds a permission to the user.
     *
     * @param user       User ID.
     * @param permission Permission ID.
     */
    public static void add(int user, int permission)
    {
        Main.database.insert("INSERT INTO `users_permissions` (`users_id`, `permissions_id`) VALUES (?, ?)", user, permission);
    }

    /**
     * Removes a permission to the user.
     *
     * @param user       User ID.
     * @param permission Permission ID.
     */
    public static void remove(int user, int permission)
    {
        Main.database.insert("DELETE FROM `users_permissions` WHERE `users_id`=? AND `permissions_id`=?", user, permission);
    }
}
