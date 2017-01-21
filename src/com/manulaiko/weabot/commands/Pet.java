package com.manulaiko.weabot.commands;

import java.util.List;

import com.manulaiko.weabot.dao.users.Factory;
import com.manulaiko.weabot.dao.users.User;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Pet command.
 *
 * Pets another user.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Pet extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "pet";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Pets another user";
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

        if(user.noneCanPetMe()) {
            e.getTextChannel().sendMessage("You can't pet "+ user.name +"!").queue();

            return;
        }

        if(user.discordID.equalsIgnoreCase(e.getAuthor().getId())) {
            this.selfPet(e.getTextChannel(), user);

            return;
        }

        e.getTextChannel().sendMessage(
                com.manulaiko.weabot.dao.images.Factory.getRandomImage("pet").link +"\n"+
                user.name +" was petted by "+ e.getAuthor().getName()
        ).queue();
    }

    /**
     * Self pets the user.
     *
     * @param channel Channel to send the message.
     * @param user    User that doesn't have anyone to pet him.
     */
    public void selfPet(TextChannel channel, User user)
    {
        channel.sendMessage(
                com.manulaiko.weabot.dao.images.Factory.getRandomImage("self_pet").link +"\n"+
                user.name +" was petted by himself"
        ).queue();
    }
}
