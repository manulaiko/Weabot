package com.manulaiko.weabot.commands;

import java.util.List;

import com.manulaiko.weabot.dao.images.*;
import com.manulaiko.weabot.dao.messages.Message;
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
            this._rejectGrope(e.getTextChannel(), user);

            return;
        }

        if(user.discordID.equalsIgnoreCase(e.getAuthor().getId())) {
            this.selfGrope(e.getTextChannel(), user);

            return;
        }

        com.manulaiko.weabot.dao.images.Image i = com.manulaiko.weabot.dao.images.Factory.getRandomImage("grope");
        if(i == null) {
            e.getTextChannel().sendMessage("No image found in category `grope`!");

            return;
        }

        e.getTextChannel().sendMessage(
                i.link +"\n"+
                user.name +" was groped by "+ e.getAuthor().getName()
        ).queue();
    }

    /**
     * Rejects a grope.
     *
     * @param channel Channel to send the message.
     * @param user    User that rejected the grope.
     */
    private void _rejectGrope(TextChannel channel, User user)
    {
        com.manulaiko.weabot.dao.images.Image i = com.manulaiko.weabot.dao.images.Factory.getRandomImage("grope_rejected");

        if(i == null) {
            channel.sendMessage("No image/message found in category `grope_rejected`!");

            return;
        }

        channel.sendMessage(
                i.link +"\n"+
                "You can't grope "+ user.name +"!"
        ).queue();
    }

    /**
     * Self gropes the user.
     *
     * @param channel Channel to send the message.
     * @param user    User that doesn't have anyone to grope him.
     */
    public void selfGrope(TextChannel channel, User user)
    {
        com.manulaiko.weabot.dao.images.Image i = com.manulaiko.weabot.dao.images.Factory.getRandomImage("self_grope");

        if(i == null) {
            channel.sendMessage("No image found in category `self_grope`!");

            return;
        }

        channel.sendMessage(
                i.link +"\n"+
                user.name +" was groped by himself"
        ).queue();
    }
}
