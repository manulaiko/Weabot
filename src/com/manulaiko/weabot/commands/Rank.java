package com.manulaiko.weabot.commands;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import com.manulaiko.weabot.dao.users.Factory;
import com.manulaiko.weabot.dao.users.User;

/**
 * Rank command.
 *
 * Changes user's rank.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Rank extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "change_rank";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Changes user's rank.";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    @Override
    public String getUsage()
    {
        return this.getFullName() +" ([user]) [rank]";
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

        String key    = args[1];
        User   author = Factory.find(e.getAuthor());
        User   user   = Factory.find(e.getAuthor());

        if(!author.canChangeRank()) {
            e.getTextChannel().sendMessage("You can't use this command!").queue();

            return;
        }

        if(key.startsWith("@")) {
            if(args.length < 3) {
                super.printUsage(e.getTextChannel());

                return;
            }

            user = Factory.find(e.getMessage().getMentionedUsers().get(0));
            key  = args[(args.length - 1)];
        }

        int rank;
        try {
            rank = Integer.parseInt(key);
        } catch(Exception ex) {
            super.printUsage(e.getTextChannel());

            return;
        }

        this._handle(e.getTextChannel(), user, author, rank);
    }

    /**
     * Handles request.
     *
     * @param channel Channel to print the result.
     * @param user    Mentioned user.
     * @param author  Message author.
     * @param value   New rank.
     */
    private void _handle(TextChannel channel, User user, User author, int value)
    {
        if(
            !author.discordID.equalsIgnoreCase(user.discordID) &&
            ( !author.canChangeOthersConfig() || author.rank < 3 )
        ) {
            channel.sendMessage("You can't change other's rank!").queue();

            return;
        }

        user.rank = value;
        user.save();

        channel.sendMessage(user.name +"'s rank is now `"+ value +"`").queue();
    }
}
