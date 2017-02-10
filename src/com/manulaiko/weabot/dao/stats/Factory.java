package com.manulaiko.weabot.dao.stats;

import java.sql.ResultSet;

import com.manulaiko.tabitha.Console;

import com.manulaiko.weabot.launcher.Main;

/**
 * Stats factory.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Factory
{
    /**
     * Finds a stat from its id.
     *
     * @param id Stat ID.
     *
     * @return Stat from database.
     */
    public static Stat find(String id)
    {
        Stat s = new Stat("", "undefined");

        ResultSet rs = Main.database.query("SELECT * FROM `stats` WHERE `id`=?", id);
        try {
            if(!rs.isBeforeFirst()) {
                Console.println("Stat not found!");

                return s;
            }

            s = new Stat(
                    rs.getString("id"),
                    rs.getString("text")
            );
        } catch(Exception e) {
            Console.println("Couldn't build stat!");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return s;
    }
}
