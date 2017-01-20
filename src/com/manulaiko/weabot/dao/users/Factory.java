package com.manulaiko.weabot.dao.users;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.dao.permissions.Permission;
import com.manulaiko.weabot.launcher.Main;

/**
 * Users factory.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Factory
{
    /**
     * Finds an user from its JDA object.
     *
     * @param user JDA user object.
     *
     * @return User from database.
     */
    public static User find(net.dv8tion.jda.core.entities.User user)
    {
        User u = null;

        ResultSet rs = Main.database.query("SELECT * FROM `users` WHERE `discord_id`='?'", user.getId());
        try {
            if(!rs.isBeforeFirst()) {
                Console.println("User not found!");

                return Factory.create(user);
            }

            u = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("discord_id"),
                    rs.getString("join_date"),
                    rs.getInt("rank")
            );

            Factory._setPermissions(u);
        } catch(Exception e) {
            Console.println("Couldn't build user!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return u;
    }

    /**
     * Creates and returns an user from its JDA object.
     *
     * @param user JDA user object.
     *
     * @return User from database.
     */
    public static User create(net.dv8tion.jda.core.entities.User user)
    {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        User u = new User(0, user.getName(), user.getId(), date, 0);

        u.save();

        return u;
    }

    /**
     * Sets user's permissions.
     *
     * @param user User to set.
     */
    private static void _setPermissions(User user)
    {
        List<Permission> permissions = com.manulaiko.weabot.dao.permissions.Factory.findByUserID(user.id);

        user.permissions = permissions;
    }
}
