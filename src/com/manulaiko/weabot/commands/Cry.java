package com.manulaiko.weabot.commands;

import java.util.List;

import com.manulaiko.weabot.dao.images.*;
import com.manulaiko.weabot.dao.messages.Factory;
import com.manulaiko.weabot.dao.messages.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Cry command.
 *
 * An user crying, so fun.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Cry extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "cry";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "An user crying, so fun";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    @Override
    public String getUsage()
    {
        return super.getFullName();
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
        com.manulaiko.weabot.dao.images.Image i = com.manulaiko.weabot.dao.images.Factory.getRandomImage("cry");
        if(i == null) {
            e.getTextChannel().sendMessage("No images found under `cry` category!").queue();

            return;
        }

        e.getTextChannel().sendMessage(i.link +"\nPoor lil "+ e.getAuthor().getName() +" is crying :/").queue();
    }
}
