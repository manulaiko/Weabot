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
            this._rejectPet(e.getTextChannel(), user);

            return;
        }

        if(user.discordID.equalsIgnoreCase(e.getAuthor().getId())) {
            this._selfPet(e.getTextChannel(), user);

            return;
        }

        com.manulaiko.weabot.dao.images.Image i = com.manulaiko.weabot.dao.images.Factory.getRandomImage("pet");
        if(i == null) {
            e.getTextChannel().sendMessage("No image found in category `pet`!");

            return;
        }

        e.getTextChannel().sendMessage(
                i.link +"\n"+
                user.name +" was petted by "+ e.getAuthor().getName()
        ).queue();
    }

    /**
     * Rejects a pet.
     *
     * @param channel Channel to send the message.
     * @param user    User that rejected the pet.
     */
    private void _rejectPet(TextChannel channel, User user)
    {
        com.manulaiko.weabot.dao.images.Image i = com.manulaiko.weabot.dao.images.Factory.getRandomImage("pet_rejected");

        if(i == null) {
            channel.sendMessage("No image/message found in category `pet_rejected`!");

            return;
        }

        channel.sendMessage(
                i.link +"\n"+
                "You can't pet "+ user.name +"!"
        ).queue();
    }

    /**
     * Self pets the user.
     *
     * @param channel Channel to send the message.
     * @param user    User that doesn't have anyone to pet him.
     */
    private void _selfPet(TextChannel channel, User user)
    {
        com.manulaiko.weabot.dao.images.Image i = com.manulaiko.weabot.dao.images.Factory.getRandomImage("self_pet");

        if(i == null) {
            channel.sendMessage("No image/message found in category `self_pet`!");

            return;
        }

        channel.sendMessage(
                i.link +"\n"+
                user.name +" was petted by himself :/"
        ).queue();
    }
}
