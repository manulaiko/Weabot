package com.manulaiko.weabot.launcher;

import java.io.FileNotFoundException;

import com.manulaiko.tabitha.Configuration;
import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.configuration.IConfiguration;
import com.manulaiko.tabitha.utils.CommandPrompt;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

/**
 * Weabot main class
 *
 * Contains main method and bootstraps the application.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Main
{
    /**
     * Application version.
     */
    private static final String version = "1.2.1";

    /**
     * Configuration object.
     *
     * Instance of `com.manulaiko.tabitha.configuration.IConfiguration` with the
     * configuration file.
     */
    public static IConfiguration configuration;

    /**
     * Command prompt.
     */
    public static CommandPrompt commandPrompt;

    /**
     * Database object.
     *
     * Used to interact with the SQLite database.
     */
    public static Database database;

    /**
     * JDA instance.
     *
     * The instance of the JDA library used for interacting with Discord API.
     */
    public static JDA jda;

    /**
     * Configuration file location.
     *
     * The relative path of the configuration file being the location
     * where the application was launched being the starting point.
     *
     * By default it's `config.ini`.
     */
    public static final String configurationFileLocation = "config.ini";

    /**
     * Database file location.
     *
     * The relative path of the database file being the location
     * where the application was launched being the starting point.
     *
     * By default it's `weabot.db`.
     */
    public static final String databaseLocation = "weabot.db";

    /**
     * Start time.
     */
    public static final long start = System.currentTimeMillis();

    /**
     * Main method.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args)
    {
        Console.println("Weabot "+ Main.version +" by Manulaiko");
        Console.println(Console.LINE_EQ);

        // 1st Stage: Load configuration file.
        Main.loadConfiguration(true);

        // 2nd Stage: Load the SQLite Database.
        Main.loadDatabase(true);

        // 3rd Stage: Initialize JDA.
        Main.loadJDA(true);

        // 3rd Stage: Initialize command prompt
        Main.commandPrompt = new CommandPrompt();
        Main.commandPrompt.start();
    }

    /**
     * Reads the configuration file and sets the
     * `Main.configuration` object.
     *
     * @param exit Whether to exit or not in a failed attempt to read the file
     */
    public static void loadConfiguration(boolean exit)
    {
        try {
            Console.println("Stage 1: Reading configuration file...");

            Main.configuration = Configuration.load(Main.configurationFileLocation);

            Console.debug = Main.configuration.getBoolean("core.debug");
        } catch(FileNotFoundException e) {
            Console.println("Be sure that the configuration file is located in `" + Main.configurationFileLocation + "`");

            if(exit) {
                Main.exit("");
            }
        } catch(Exception e) {
            Console.println("There was a problem reading configuration file.");
            Console.println(e.getMessage());

            if(exit) {
                Main.exit("");
            }
        }
    }

    /**
     * Reads the configuration file and sets the
     * `Main.configuration` object.
     */
    public static void loadConfiguration()
    {
        Main.loadConfiguration(false);
    }

    /**
     * Loads the SQLite database and sets the
     * `Main.database` object.
     *
     * @param exit Whether to exit or not in a failed attempt to load the database
     */
    public static void loadDatabase(boolean exit)
    {
        Console.println("Stage 2: Loading the SQLite Database");
        try {
            Main.database = new Database(Main.databaseLocation);
        } catch (Exception e) {
            Console.println("Couldn't connect to the database!");
            Console.println(e.getMessage());

            if(exit) {
                Main.exit("");
            }
        }
    }

    /**
     * Loads the SQLite database and sets the
     * `Main.database` object.
     */
    public static void loadDatabase()
    {
        Main.loadDatabase(false);
    }

    /**
     * Initializes JDA.
     *
     * @param exit Whether to exit or not in a failed attempt to initialize JDA
     */
    public static void loadJDA(boolean exit)
    {
        JDA    jda         = null;
        String accountType = Main.configuration.getString("core.account_type");

        try {
            if(accountType.equalsIgnoreCase("bot")) {
                jda = Main._loadBotAccount();
            }

            if(accountType.equalsIgnoreCase("user")) {
                jda = Main._loadUserAccount();
            }
        } catch(Exception e) {
            Console.println("Couldn't initialize JDA!");

            if(exit) {
                Main.exit(e.getMessage());
            }

            Console.println(e.getMessage());
        }

        Main.jda = jda;
    }

    /**
     * Initializes JDA.
     */
    public static void loadJDA()
    {
        Main.loadJDA(false);
    }

    /**
     * Loads a bot account.
     *
     * @return JDA instance.
     *
     * @throws Exception If something goes wrong.
     */
    private static JDA _loadBotAccount() throws Exception
    {
        String token = Main.configuration.getString("bot.token");

        JDABuilder builder = new JDABuilder(AccountType.BOT);

        builder.setToken(token);

        return builder.buildBlocking();
    }

    /**
     * Loads an user account.
     *
     * @return JDA instance.
     *
     * @throws Exception If something goes wrong.
     */
    private static JDA _loadUserAccount() throws Exception
    {
        String username = Main.configuration.getString("user.name");
        String password = Main.configuration.getString("user.password");

        JDABuilder builder = new JDABuilder(AccountType.CLIENT);

        throw new Exception("User account login isn't supported yet!");
    }

    /**
     * Terminates remaining actions and finishes execution.
     *
     * @param message Message to print before exit.
     * @param level   Exit level.
     */
    public static void exit(String message, int level)
    {
        Console.println(message);
        Console.readLine();
        System.exit(level);
    }

    /**
     * Shortcut of `exit`.
     *
     * @param message Message to print before exit.
     */
    public static void exit(String message)
    {
        Main.exit(message, 0);
    }
}
