package com.manulaiko.weabot.commands;

import java.util.List;

import com.manulaiko.weabot.dao.messages.Factory;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import com.manulaiko.weabot.dao.messages.Message;

/**
 * Bully command.
 *
 * Bullies someone.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Bully extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "bully";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Bullies someone";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    @Override
    public String getUsage()
    {
        return super.getFullName() +" [user]";
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

        List<User> mentions = e.getMessage().getMentionedUsers();

        if(mentions.size() == 0) {
            e.getTextChannel().sendMessage("No users to bully").queue();

            return;
        }

        Message m = Factory.getRandomMessage("bully");

        if(m == null) {
            e.getTextChannel().sendMessage("No message found under `bully` category!").queue();

            return;
        }

        e.getTextChannel().sendMessage(mentions.get(0).getName() +", "+ m.text).queue();
    }
}
