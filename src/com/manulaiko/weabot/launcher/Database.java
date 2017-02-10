package com.manulaiko.weabot.launcher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.filesystem.File;
import com.manulaiko.weabot.dao.permissions.Permission;
import com.manulaiko.weabot.dao.stats.Stat;
import org.sqlite.JDBC;

/**
 * Database class.
 *
 * Connects to the SQLite database and that shit.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Database
{
    /**
     * Connection to the database.
     */
    private Connection _connection;

    /**
     * Constructor.
     *
     * @param path Path to the database.
     *
     * @throws Exception In case something failed.
     */
    public Database(String path) throws Exception
    {
        boolean dbExists = File.exists(path);

        this._connection = JDBC.createConnection("jdbc:sqlite:"+ path, new Properties());

        if(!dbExists) {
            this._createDatabase();
        }
    }

    /***
     * Creates the database.
     */
    private void _createDatabase()
    {
        // First create the tables.
        this._createTables();

        // Then dump the initial data.
        this._insertRows();
    }

    /**
     * Creates the initial tables in the database.
     */
    private void _createTables()
    {
        // Users table, contains the users with their join dates and discord ID
        this.update(
                "CREATE TABLE `users` (\n" +
                "    `id`         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    `discord_id` TEXT NOT NULL UNIQUE,\n" +
                "    `name`       TEXT NOT NULL DEFAULT '',\n" +
                "    `join_date`  TEXT NOT NULL DEFAULT '',\n" +
                "    `rank`       INTEGER NOT NULL DEFAULT '0'\n"+
                ");"
        );

        // Relation table for the users and permissions table
        this.update(
                "CREATE TABLE `users_permissions` (\n" +
                "    `id`             INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    `users_id`       INTEGER NOT NULL,\n" +
                "    `permissions_id` INTEGER NOT NULL" +
                ");"
        );

        // Permissions table, contains the different permissions
        this.update(
                "CREATE TABLE `permissions` (\n" +
                "    `id`          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    `name`        TEXT NOT NULL,\n" +
                "    `rank`        INTEGER NOT NULL DEFAULT '0',\n"+
                "    `description` TEXT NOT NULL DEFAULT 'no description available'\n"+
                ");"
        );

        // Categories table, contains categories used by images and messages
        this.update(
                "CREATE TABLE `categories` (\n" +
                "    `id`          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    `name`        TEXT NOT NULL,\n" +
                "    `description` TEXT NOT NULL DEFAULT 'no description available'\n" +
                ");"
        );

        // Images table, contains the images used by different commands
        this.update(
                "CREATE TABLE `images` (\n" +
                "    `id`       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    `link`     TEXT NOT NULL\n" +
                ");"
        );

        // Many to many relations
        this.update(
                "CREATE TABLE `images_categories` (\n" +
                "    `id`            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    `categories_id` INTEGER NOT NULL,\n" +
                "    `images_id`     INTEGER NOT NULL\n" +
                ");"
        );

        // Messages table, contains the messages used by different commands
        this.update(
                "CREATE TABLE `messages` (\n" +
                "    `id`       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    `text`     TEXT NOT NULL,\n" +
                "    `category` TEXT NOT NULL DEFAULT ''\n" +
                ");"
        );

        // Many to many relations
        this.update(
                "CREATE TABLE `messages_categories` (\n" +
                "   `id`            INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "   `categories_id` INTEGER NOT NULL,\n" +
                "   `messages_id`   INTEGER NOT NULL\n" +
                ");"
        );

        // Scrappers table, contains the image scrappers
        this.update(
                "CREATE TABLE `scrappers` (\n" +
                "    `id`         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    `channel_id` TEXT NOT NULL,\n" +
                "    `path`       TEXT NOT NULL DEFAULT ''\n" +
                ");"
        );

        // Stats table
        this.update(
                "CREATE TABLE `stats` (\n" +
                "   `id`   TEXT NOT NULL,\n" +
                "   `text` TEXT NOT NULL\n" +
                ");"
        );
    }

    /**
     * Inserts the initial rows to the database.
     */
    private void _insertRows()
    {
        // Permissions
        this.update(
                "INSERT INTO `permissions` (`name`, `rank`, `description`) VALUES \n"                         +
                "('can_change_rank', 3, 'Allows the user to change its rank'),\n"                             +
                "('can_change_others_config', 2, 'Allows the user to change others config'),\n"               +
                "('can_add_images', 2, 'Allows the user to add images to the database'),\n"                   +
                "('can_add_permissions', 2, 'Allows the user to add permissions to the database'),\n"         +
                "('can_add_messages', 2, 'Allows the user to add messages to the database'),\n"               +
                "('can_add_categories', 2, 'Allows the user to add categories to the database'),\n"           +
                "('can_add_scrappers', 3, 'Allows the user to add image scrappers'),\n"                       +
                "('can_delete_images', 2, 'Allows the user to delete images from the database'),\n"           +
                "('can_delete_permissions', 2, 'Allows the user to delete permissions from the database'),\n" +
                "('can_delete_messages', 2, 'Allows the user to delete messages from the database'),\n"       +
                "('can_delete_categories', 2, 'Allows the user to delete categories'),\n"                     +
                "('can_delete_scrappers', 3, 'Allows the user to delete image scrappers'),\n"                 +
                "('none_can_spank_me', 0, 'Disallows users from spanking you'),\n"                            +
                "('none_can_pet_me', 0, 'Disallows users from petting you'),\n"                               +
                "('none_can_grope_me', 0, 'Disallows users from groping you');"
        );

        // Categories
        this.update(
                "INSERT INTO `categories` (`name`, `description`) VALUES \n"    +
                "('mentions', 'Messages to send when the bot is mentioned'),\n" +
                "('bully', 'Bully messages'),\n"                                +
                "('pet', 'Pet images'),\n"                                      +
                "('self_pet', 'Self petting images'),\n"                        +
                "('pet_rejected', 'Pet rejected images'),\n"                    +
                "('spank', 'Spank images'),\n"                                  +
                "('self_spank', 'Self spanking images'),\n"                     +
                "('spank_rejected', 'Spank rejected images'),\n"                +
                "('grope', 'Grope images'),\n"                                  +
                "('self_grope', 'Self groping images'),\n"                      +
                "('grope_rejected', 'Grope rejected images');"
        );

        // Stats
        this.update(
                "INSERT INTO `stats` (`id`, `text`) VALUES \n" +
                "('scrapped_images', '0'),\n"                  +
                "('received_messages', '0'),\n"                +
                "('executed_commands', '0');"
        );
    }

    /**
     * Creates and executes a query.
     *
     * @param sql Query to execute.
     *
     * @return Query result.
     */
    public ResultSet query(String sql)
    {
        ResultSet result = null;

        try {
            Statement stm = this._connection.createStatement();
            result = stm.executeQuery(sql);
            stm.close();
        } catch(Exception e) {
            Console.println("Couldn't execute query `"+ sql +"`");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Creates and executes a query.
     *
     * @param sql  Query to execute.
     * @param args Statement parameters.
     *
     * @return Query result.
     */
    public ResultSet query(String sql, Object... args)
    {
        ResultSet result = null;

        try {
            PreparedStatement stmt = this.prepare(sql, args);
            result = stmt.executeQuery();
        } catch(Exception e) {
            Console.println("Couldn't execute query `"+ sql +"`");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Creates and executes an update query.
     *
     * @param sql Query to execute.
     *
     * @return Affected rows.
     */
    public int update(String sql)
    {
        int result = 0;

        try {
            Statement stm = this._connection.createStatement();
            result = stm.executeUpdate(sql);
            stm.close();
        } catch(Exception e) {
            Console.println("Couldn't execute query `"+ sql +"`");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Creates and executes an update query.
     *
     * @param sql  Query to execute.
     * @param args Statement parameters.
     *
     * @return Affected rows.
     */
    public int update(String sql, Object... args)
    {
        int result = 0;

        try {
            PreparedStatement stmt = this.prepare(sql, args);
            result = stmt.executeUpdate();
        } catch(Exception e) {
            Console.println("Couldn't execute query `"+ sql +"`");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Creates and executes an insert query.
     *
     * @param sql Query to execute.
     *
     * @return Last insert id.
     */
    public int insert(String sql)
    {
        int result = 0;

        try {
            Statement stm = this._connection.createStatement();
            stm.executeUpdate(sql);

            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                result = rs.getInt(1);
            }

            stm.close();
        } catch(Exception e) {
            Console.println("Couldn't execute query `"+ sql +"`");
            Console.println(e.getMessage());

            e.printStackTrace();

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Creates and executes an update query.
     *
     * @param sql  Query to execute.
     * @param args Statement parameters.
     *
     * @return Affected rows.
     */
    public int insert(String sql, Object... args)
    {
        int result = 0;

        try {
            PreparedStatement stmt = this.prepare(true, sql, args);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                result = rs.getInt(1);
            }

            stmt.close();
        } catch(Exception e) {
            Console.println("Couldn't execute query `"+ sql +"`");
            Console.println(e.getMessage());

            if(Console.debug) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Executes a prepared statement.
     *
     * @param sql  Query to execute.
     * @param args Statement parameters.
     *
     * @return Query result.
     */
    public PreparedStatement prepare(String sql, Object... args)
    {
        return this.prepare(false, sql, args);
    }

    /**
     * Executes a prepared statement.
     *
     * @param isInsert Whether the prepared statement is an INSERT query or not.
     * @param sql      Query to execute.
     * @param args     Statement parameters.
     *
     * @return Query result.
     */
    public PreparedStatement prepare(boolean isInsert, String sql, Object... args)
    {
        PreparedStatement stmt = null;

        try {
            stmt = this._connection.prepareStatement(sql);
            if(isInsert) {
                stmt = this._connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }

            for(int i = 0; i < args.length; i++) {
                int index = i + 1;
                Object p  = args[i];

                if(p instanceof Boolean) {
                    stmt.setBoolean(index, (Boolean)p);
                } else if(p instanceof Integer) {
                    stmt.setInt(index, (Integer)p);
                } else if(p instanceof Double) {
                    stmt.setDouble(index, (Double)p);
                } else if(p instanceof Float) {
                    stmt.setDouble(index, (Float)p);
                } else if(p instanceof Byte) {
                    stmt.setByte(index, (Byte)p);
                } else if(p instanceof Long) {
                    stmt.setLong(index, (Long)p);
                }  else if(p instanceof Array) {
                    stmt.setArray(index, (Array)p);
                } else if(p == null) {
                    stmt.setNull(index, 0);
                } else {
                    stmt.setString(index, p.toString());
                }
            }
        } catch(Exception e) {
            Console.println("Couldn't set parameter!");
            Console.println(e.getMessage());

            e.printStackTrace();
        }

        return stmt;
    }
}
