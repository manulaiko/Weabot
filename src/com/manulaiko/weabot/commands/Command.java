package com.manulaiko.weabot.commands;

import com.manulaiko.tabitha.utils.ICommand;
import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.events.Event;

/**
 * Base class for all commands.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public abstract class Command
{
    /**
     * Checks whether this command can execute `name` command.
     *
     * @param name Command name to check.
     *
     * @return Whether this command can execute `name`.
     */
    public boolean canExecute(String name)
    {
        String cName = Main.configuration.getString("bot.prefix") + this.getName();

        if(Main.configuration.getBoolean("bot.ignoreCase")) {
            return cName.equalsIgnoreCase(name);
        }

        return cName.equals(name);
    }

    /**
     * Returns the message for the `help` command.
     *
     * @return `help` command message.
     */
    public String getHelp()
    {
        String help = Main.configuration.getString("bot.prefix") + this.getName() +":\n"+
                      "```\n"+
                      this.getDescription() +"\n"+
                      "```";

        return help;
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
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public abstract void execute(Event event, String[] args);
}
