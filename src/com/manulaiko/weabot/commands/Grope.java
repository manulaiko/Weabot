package com.manulaiko.weabot.commands;

import java.util.List;

import com.manulaiko.weabot.dao.users.Factory;
import com.manulaiko.weabot.dao.users.User;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Grope command.
 *
 * Gropes another user.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Grope extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "grope";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Gropes another user";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    @Override
    public String getUsage()
    {
        return super.getFullName() + " [username]";
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

        List<net.dv8tion.jda.core.entities.User> mentions = e.getMessage().getMentionedUsers();

        if(mentions.size() != 1) {
            super.printUsage(e.getTextChannel());

            return;
        }

        User user = Factory.find(mentions.get(0));

        if(user.noneCanGropeMe()) {
            e.getTextChannel().sendMessage(
                    com.manulaiko.weabot.dao.images.Factory.getRandomImage("grope_rejected").link +"\n"+
                    "One does not simply grope "+ user.name +"!"
            ).queue();

            return;
        }

        if(user.discordID.equalsIgnoreCase(e.getAuthor().getId())) {
            this.selfPet(e.getTextChannel(), user);

            return;
        }

        e.getTextChannel().sendMessage(
                com.manulaiko.weabot.dao.images.Factory.getRandomImage("grope").link +"\n"+
                user.name +" was groped by "+ e.getAuthor().getName()
        ).queue();
    }

    /**
     * Self gropes the user.
     *
     * @param channel Channel to send the message.
     * @param user    User that doesn't have anyone to grope him.
     */
    public void selfPet(TextChannel channel, User user)
    {
        channel.sendMessage(
                com.manulaiko.weabot.dao.images.Factory.getRandomImage("self_grope").link +"\n"+
                user.name +" was groped by himself"
        ).queue();
    }
}
