package com.manulaiko.weabot.commands;

import com.manulaiko.weabot.dao.images.Factory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Image command.
 *
 * Sends an image from the database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Image extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "image";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Sends an image from the database";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    @Override
    public String getUsage()
    {
        return super.getFullName() +" [category]";
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

        com.manulaiko.weabot.dao.images.Image i = Factory.getRandomImage(args[1]);

        e.getTextChannel().sendMessage(i.link).queue();
    }
}
