package com.manulaiko.weabot.launcher;

import com.manulaiko.tabitha.Configuration;
import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.configuration.IConfiguration;
import com.manulaiko.tabitha.exceptions.NotFound;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

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
     *
     * @var Version.
     */
    private static final String version = "0.0.0";

    /**
     * Configuration object.
     *
     * Instance of `com.manulaiko.tabitha.configuration.IConfiguration` with the
     * configuration file.
     *
     * @var Configuration object.
     */
    public static IConfiguration configuration;

    /**
     * Configuration file location.
     *
     * The relative path of the configuration file being the location
     * where the application was launched being the starting point.
     *
     * By default it's `config.ini`.
     *
     * @var Configuration file location.
     */
    public static final String configurationFileLocation = "config.ini";

    /**
     * JDA instance.
     *
     * The instance of the JDA library used for interacting with Discord API.
     *
     * @var JDA Instance.
     */
    public static JDA jda;

    /**
     * Message listener instance.
     *
     * Contains available commands and processes received messages.
     *
     * @var MessageListener Instance.
     */
    public static MessageListener messageListener;

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
        Main.loadConfiguration();

        // 2nd Stage: Initialize JDA
        Main.initializeJDA();
    }

    /**
     * Reads the configuration file and sets the
     * `Main.configuration` object.
     */
    public static void loadConfiguration()
    {
        try {
            Console.println("Stage 1: Reading configuration file...");

            Main.configuration = Configuration.load(Main.configurationFileLocation);
        } catch(NotFound e) {
            Main.exit("Be sure that the configuration file is located in `"+ Main.configurationFileLocation +"`");
        } catch(Exception e) {
            Console.println("There was a problem reading configuration file.");
            Main.exit(e.getMessage());
        }
    }

    /**
     * Initializes the JDA library.
     */
    public static void initializeJDA()
    {
        try {
            Console.println("Initializing Weabot...");

            Main.messageListener = new MessageListener();
            JDABuilder builder   = new JDABuilder();

            if(Main.configuration.getBoolean("proxy.enabled")) {
                builder.setProxy(
                        Main.configuration.getString("proxy.host"),
                        Main.configuration.getInt("proxy.port")
                );
            }

            String token = Main.configuration.getString("core.token");
            if(token.isEmpty()) {
                Console.println("Bot token wasn't set!");
                Console.print("Enter bot token: ");
                token = Console.readLine();
            }
            builder.setBotToken(token);

            Main.jda = builder.buildBlocking();
            Main.jda.addEventListener(Main.messageListener);
        } catch(Exception e) {
            Console.println("Couldn't initialize Weabot!");
            Main.exit(e.getMessage());
        }
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
