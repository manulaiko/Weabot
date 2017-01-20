package com.manulaiko.weabot.launcher;

import java.sql.*;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.filesystem.File;

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
        boolean isNewDatabase = File.exists(path);

        this._connection = DriverManager.getConnection("jdcb:sqlite:"+ path);

        if(isNewDatabase) {
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
                "    `join_date`  TEXT NOT NULL,\n" +
                "    `rank`       INTEGER NOT NULL DEFAULT '0',\n"+
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

        // Images table, contains the images used by different commands
        this.update(
                "CREATE TABLE `images` (\n" +
                "    `id`       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    `link`     TEXT NOT NULL,\n" +
                "    `category` TEXT NOT NULL DEFAULT ''\n" +
                ");"
        );
    }

    /**
     * Inserts the initial rows to the database.
     */
    private void _insertRows()
    {
        // Dump permissions
        // Permissions list:
        //  - `Allow Other Users To Command Me`: Allows other users to use commands on other users (e.g. pet another user)
        this.update(
                "INSERT INTO `permissions` (`id`, `name`, `rank`, `description`) VALUES" +
                "(1, 'change_rank', 3, 'Allows to change user rank');"
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
            result = stm.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
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
    public int insert(String sql, Object... args)
    {
        int result = 0;

        try {
            PreparedStatement stmt = this.prepare(true, sql, args);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();

            result = rs.getInt(1);

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
                Object p = args[i];

                if(p instanceof Boolean) {
                    stmt.setBoolean(i, (Boolean)p);
                } else if(p instanceof Integer) {
                    stmt.setInt(i, (Integer)p);
                } else if(p instanceof Double) {
                    stmt.setDouble(i, (Double)p);
                } else if(p instanceof Float) {
                    stmt.setDouble(i, (Float)p);
                } else if(p instanceof Byte) {
                    stmt.setByte(i, (Byte)p);
                } else if(p instanceof Long) {
                    stmt.setLong(i, (Long)p);
                }  else if(p instanceof Array) {
                    stmt.setArray(i, (Array)p);
                } else if(p == null) {
                    stmt.setNull(i, 0);
                } else {
                    stmt.setString(i, p.toString());
                }
            }
        } catch(Exception e) {
            Console.println("Couldn't set parameter!");
            Console.println(e.getMessage());
        }

        return stmt;
    }
}
