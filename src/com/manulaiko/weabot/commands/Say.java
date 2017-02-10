package com.manulaiko.weabot.commands;

import java.util.List;

import com.manulaiko.weabot.dao.messages.Factory;
import com.manulaiko.weabot.dao.messages.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Say command.
 *
 * Says something.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Say extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "say";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Says something";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    @Override
    public String getUsage()
    {
        return super.getFullName() +" [message]";
    }

    /**
     * Executes the command.
     *
     * @param e    Event that fired the command.
     * @param args Command arguments.
     */
    @Override
    public void execute(MessageReceivedEvent e, String[] args)
    {
        if(args.length < 2) {
            super.printUsage(e.getTextChannel());

            return;
        }

        String message = "";
        for(int i = 1; i < args.length; i++) {
            if(!message.isEmpty()) {
                message += " ";
            }

            message += args[i];
        }

        e.getTextChannel().sendMessage(message).queue();
    }
}
