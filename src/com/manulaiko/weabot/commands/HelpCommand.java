package com.manulaiko.weabot.commands;

import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Help command.
 *
 * Prints available commands.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class HelpCommand extends Command
{
    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        String message = "";

        for(Command command : Main.messageListener.commands) {
            message += command.getHelp() +"\n";
        }

        event.getChannel().sendMessage(message);
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "help";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        return "Prints available commands";
    }
}
