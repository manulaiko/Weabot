package com.manulaiko.weabot.commands;

import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;

/**
 * Command interface.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public abstract class Command
{
    /**
     * Returns full command name.
     *
     * @return Full command name.
     */
    public String getFullName()
    {
        return Main.configuration.getString("core.prefix") + this.getName();
    }

    /**
     * Prints command usage.
     *
     * @param channel Channel to send the message.
     */
    public void printUsage(TextChannel channel)
    {
        String message = this.getDescription() +"\n" +
                         "Usage: " +
                         "`"+ this.getUsage() +"`";

        channel.sendMessage(message);
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public abstract String getName();

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public abstract String getDescription();

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    public abstract String getUsage();

    /**
     * Executes the command.
     *
     * @param e    Event that fired the command.
     * @param args Command arguments.
     */
    public abstract void execute(Event e, String[] args);
}
