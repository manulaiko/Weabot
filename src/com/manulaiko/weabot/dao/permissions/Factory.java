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
     * Finds an user from its id.
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
                Console.println("Permission not found!");

                return Factory.create(id);
            }

            p = new Permission(
                    rs.getInt("id"),
                    rs.getString("name")
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
     * Creates and returns an permission from its id.
     *
     * @param id Permission ID.
     *
     * @return User from database.
     */
    public static Permission create(int id)
    {
        Permission p = new Permission(0, "");

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
}
