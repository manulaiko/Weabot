package com.manulaiko.weabot.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.manulaiko.tabitha.Configuration;
import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.configuration.IConfiguration;
import com.manulaiko.tabitha.utils.CommandPrompt;
import com.manulaiko.weabot.launcher.commands.*;
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
    private static final String version = "1.2.0";

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
     * Command prompt.
     */
    public static CommandPrompt commandPrompt;

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
        Main.loadConfiguration();
        Console.debug = Main.configuration.getBoolean("core.debug");

        // 2nd Stage: Initialize JDA
        Main.initializeJDA();

        // 3rd Stage: Initialize command prompt
        Main.commandPrompt = new CommandPrompt();
        Main.commandPrompt.addCommand(new AddModCommand());
        Main.commandPrompt.addCommand(new RemoveModCommand());
        Main.commandPrompt.addCommand(new AddAdminCommand());
        Main.commandPrompt.addCommand(new RemoveAdminCommand());
        Main.commandPrompt.addCommand(new ReloadConfiguration());
        Main.commandPrompt.start();
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

            String saveImages = Main.configuration.getString("core.saveImages");
            for(String s : saveImages.split("\\|")) {
                if(s.isEmpty()) {
                    continue;
                }

                String i[] = s.split(";");
                String channels[] = i[0].split(",");
                String paths[]    = i[1].split(",");

                Main._addSaveImageFromChannels(channels, paths);
            }
        } catch(FileNotFoundException e) {
            Main.exit("Be sure that the configuration file is located in `"+ Main.configurationFileLocation +"`");
        } catch(Exception e) {
            Console.println("There was a problem reading configuration file.");
            Main.exit(e.getMessage());
        }
    }

    /**
     * Adds various channels to the save image list.
     *
     * @param channels Channels to add.
     * @param paths    Paths where the images will be saved.
     */
    private static void _addSaveImageFromChannels(String[] channels, String[] paths)
    {
        for(String channel : channels) {
            for(String path : paths) {
                File f = new File(path);
                if(!f.exists()) {
                    Console.debug("Directories for `"+ f.getAbsolutePath() +"` created!");

                    f.mkdirs();
                }
                if(f.isFile()) {
                    Console.println(path +" is a file!");

                    continue;
                }

                if(!Settings.saveImagesChannels.containsKey(channel)) {
                    Settings.saveImagesChannels.put(channel, new ArrayList<>());
                }

                Settings.saveImagesChannels.get(channel).add(f);

                Console.debug("Images from `"+ channel +"` will be saved on `"+ f.getAbsolutePath() +"`");
            }
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

    /**
     * Winshit's path parser.
     *
     * @return Path separator.
     */
    public static String separator()
    {
        String separator = "/";
        if(System.getProperty("os.name").contains("Windows")) {
            separator = "\\";
        }

        return separator;
    }

    /**
     * Returns run time.
     *
     * @return Run time.
     */
    public static String getRuntime()
    {
        long millis = System.currentTimeMillis() - Main.start;

        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour   = (millis / (1000 * 60 * 60)) % 24;

        String runtime = String.format("%02d minutes and %02d seconds", minute, second);
        if(hour > 0) {
            runtime = String.format("%02d hours %02d minutes and %02d seconds", hour, minute, second);
        }

        return runtime;
    }
}
