package com.manulaiko.weabot.commands;

import java.util.List;

import com.manulaiko.weabot.dao.users.Factory;
import com.manulaiko.weabot.dao.users.User;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Spank command.
 *
 * Spanks another user.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Spank extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "spank";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Spanks another user";
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

        if(user.noneCanSpankMe()) {
            this._rejectSpank(e.getTextChannel(), user);

            return;
        }

        if(user.discordID.equalsIgnoreCase(e.getAuthor().getId())) {
            this._selfSpank(e.getTextChannel(), user);

            return;
        }

        com.manulaiko.weabot.dao.images.Image i = com.manulaiko.weabot.dao.images.Factory.getRandomImage("spank");
        if(i == null) {
            e.getTextChannel().sendMessage("No image found in category `spank`!");

            return;
        }

        e.getTextChannel().sendMessage(
                i.link +"\n"+
                user.name +" was spanked by "+ e.getAuthor().getName()
        ).queue();
    }

    /**
     * Rejects a spank.
     *
     * @param channel Channel to send the message.
     * @param user    User that rejected the spank.
     */
    private void _rejectSpank(TextChannel channel, User user)
    {
        com.manulaiko.weabot.dao.images.Image i = com.manulaiko.weabot.dao.images.Factory.getRandomImage("spank_rejected");

        if(i == null) {
            channel.sendMessage("No image/message found in category `spank_rejected`!");

            return;
        }

        channel.sendMessage(
                i.link +"\n"+
                "You can't spank "+ user.name +"!"
        ).queue();
    }

    /**
     * Self spanks the user.
     *
     * @param channel Channel to send the message.
     * @param user    User that doesn't have anyone to spank him.
     */
    private void _selfSpank(TextChannel channel, User user)
    {
        com.manulaiko.weabot.dao.images.Image i = com.manulaiko.weabot.dao.images.Factory.getRandomImage("self_spank");

        if(i == null) {
            channel.sendMessage("No image/message found in category `self_spank`!");

            return;
        }

        channel.sendMessage(
                i.link +"\n"+
                user.name +" was spanked by himself :/"
        ).queue();
    }
}
