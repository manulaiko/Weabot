package com.manulaiko.weabot.commands;

import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Help command.
 *
 * Prints available commands.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Help extends Command
{
    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        String name    = "";
        if(args.length > 1) {
            name = args[1];
        }

        if(name.isEmpty()) {
            this.printAllCommands(event.getTextChannel());

            return;
        }

        Command command = null;
        for(Command c : Main.weabot.commands.values()) {
            if(c.getName().equalsIgnoreCase(name)) {
                command = c;

                break;
            }
        }

        if(command == null) {
            event.getTextChannel().sendMessage("Command not found!");
            this.printAllCommands(event.getTextChannel());

            return;
        }

        String message = command.getName() +"\n" +
                         "*"+ command.getDescription() +"*\n" +
                         "Usage:\n" +
                         "```" +
                         command.getUsage() +
                         "```";

        event.getTextChannel().sendMessage(message).queue();
    }

    /**
     * Prints all commands.
     *
     * @param channel Channel to send the message.
     */
    public void printAllCommands(TextChannel channel)
    {
        String message = "Available commands:";

        for(Command c : Main.weabot.commands.values()) {
            message += "\n`"+ c.getFullName() +"`: "+ c.getDescription();
        }

        channel.sendMessage(message).queue();
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
        return "Prints available commands.";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    public String getUsage()
    {
        return this.getFullName() +" ([command name])";
    }
}
