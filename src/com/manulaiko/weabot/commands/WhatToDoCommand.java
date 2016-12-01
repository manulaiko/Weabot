package com.manulaiko.weabot.commands;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * What to do command.
 *
 * Offers help to poor individuals that doesn't know what
 * to do in life.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class WhatToDoCommand extends Command
{
    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        ArrayList<String> options = (args.length > 1) ? this._getOptions(args) : this._getDefaultOptions();

        int random = ThreadLocalRandom.current()
                                      .nextInt(0, options.size());

        event.getChannel()
             .sendMessage("It seems you're lost in life, why don't you just "+ options.get(random) +"?");
    }

    /**
     * Returns an array with possible options.
     *
     * @param args Command arguments.
     *
     * @return Array with possible options.
     */
    private ArrayList<String> _getOptions(String[] args)
    {
        ArrayList<String> options = new ArrayList<>();

        for(String arg : args) {
            if(arg.equalsIgnoreCase("+")) {
                options.addAll(this._getDefaultOptions());
            } else {
                options.add(arg);
            }
        }

        return options;
    }

    /**
     * Returns default options.
     *
     * @return Array with default options.
     */
    private ArrayList<String> _getDefaultOptions()
    {
        ArrayList<String> options = new ArrayList<>();

        for(String option : Main.configuration.getString("commands.what_to_do").split(", ")) {
            options.add(option);
        }

        return options;
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "what_to_do";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        String options = "";

        for(String s : this._getDefaultOptions()) {
            options += "     -"+ s +"\n";
        }

        return "Offers random things to do when you don't know what to do in life.\n" +
                "\n" +
                "Usage:\n" +
                "    "+ this.getFullName() +" ([+]) ([options])\n" +
                "Example:\n" +
                "    Get random default option: "+ this.getFullName() +"\n" +
                "    Get random custom option: "+ this.getFullName() +" ListenToMusic WatchAnime LearnSomething\n" +
                "    Get random custom or default option: "+ this.getFullName() +" + ListenToMusic WatchAnime LearnSomething\n" +
                "Default options:\n" +
                options;
    }
}
