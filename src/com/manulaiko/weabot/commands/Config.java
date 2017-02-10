package com.manulaiko.weabot.commands;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import com.manulaiko.weabot.dao.permissions.Permission;
import com.manulaiko.weabot.dao.users.Factory;
import com.manulaiko.weabot.dao.users.User;

/**
 * Config command.
 *
 * Updates user's configuration.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Config extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "config";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Updates user's configuration.";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    @Override
    public String getUsage()
    {
        return this.getFullName() +" ( ([key] [value]) | ([user] ( [key] [value] | list ) ) | help )";
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
        if(args.length < 1) {
            super.printUsage(e.getTextChannel());

            return;
        }

        if(args[1].equalsIgnoreCase("help")) {
            this.printHelp(e.getTextChannel());

            return;
        }

        String key    = args[1];
        String value  = args[2];
        User   user   = Factory.find(e.getAuthor());
        User   author = Factory.find(e.getAuthor());

        if(key.startsWith("@")) {
            if(args.length <= 3) {
                super.printUsage(e.getTextChannel());

                return;
            }

            user = Factory.find(e.getMessage().getMentionedUsers().get(0));

            key   = value;
            value = args[3];
        }

        if(value.equalsIgnoreCase("list")) {
            this._printPermissions(e.getTextChannel(), user);

            return;
        }

        this._handle(e.getTextChannel(), user, author, key, value);
    }

    /**
     * Prints available configuration options.
     *
     * @param channel Channel where the message should be sent.
     */
    public void printHelp(TextChannel channel)
    {
        String message = "Available permissions:\n" +
                         "```\n";

        for(Permission p : com.manulaiko.weabot.dao.permissions.Factory.all()) {
            message += " - "+ p.name +": "+ p.description +" (required rank: "+ p.rank +")\n";
        }

        message += "```";

        channel.sendMessage(message).queue();
    }

    /**
     * Prints user's permission.
     *
     * @param channel Channel to print the result.
     * @param user    User to print the permissions.
     */
    private void _printPermissions(TextChannel channel, User user)
    {
        String message = "Permissions of *"+ user.name +"*:\n" +
                         "```";

        for(Permission p : user.permissions) {
            message += " - "+ p.name +" ("+ p.id +": "+ p.description +"\n";
        }

        message += "```";

        channel.sendMessage(message).queue();
    }

    /**
     * Handles request.
     *
     * @param channel Channel to print the result.
     * @param user    Mentioned user.
     * @param author  Message author.
     * @param key     Requested configuration.
     * @param value   New value of `key`.
     */
    private void _handle(TextChannel channel, User user, User author, String key, String value)
    {
        if(
            !author.discordID.equalsIgnoreCase(user.discordID) &&
            ( !author.canChangeOthersConfig() || author.rank < 3 )
        ) {
            channel.sendMessage("You can't change other's configuration!").queue();

            return;
        }

        Permission permission = com.manulaiko.weabot.dao.permissions.Factory.find(key);

        if(permission == null) {
            this.printHelp(channel);

            return;
        }

        if(author.rank < permission.rank) {
            channel.sendMessage("You don't have enough privileges to change this permission!").queue();

            return;
        }

        if(user.permissions.contains(permission)) {
            if(!this._asBool(value)) {
                this._deletePermission(user, permission);

                channel.sendMessage("Permission deleted!").queue();

                return;
            }

            return;
        }

        if(this._asBool(value)) {
            this._addPermission(user, permission);

            channel.sendMessage("Permission added!").queue();

            return;
        }

        channel.sendMessage("You can't remove a permission you don't have!").queue();
    }

    /**
     * Removes a permission from an user.
     *
     * @param user       User to remove permission.
     * @param permission Permission to remove.
     */
    private void _deletePermission(User user, Permission permission)
    {
        com.manulaiko.weabot.dao.permissions.Factory.remove(user.id, permission.id);

        user.permissions.remove(permission);
    }

    /**
     * Adds a permission from an user.
     *
     * @param user       User to add permission.
     * @param permission Permission to add.
     */
    private void _addPermission(User user, Permission permission)
    {
        com.manulaiko.weabot.dao.permissions.Factory.add(user.id, permission.id);

        user.permissions.add(permission);
    }

    /**
     * Returns the input of the user as a boolean.
     *
     * @param input User's input.
     *
     * @return `input` as boolean.
     */
    private boolean _asBool(String input)
    {
        return (
                input.equalsIgnoreCase("activate")  ||
                input.equalsIgnoreCase("activated") ||
                input.equalsIgnoreCase("enable")    ||
                input.equalsIgnoreCase("enabled")   ||
                input.equalsIgnoreCase("true")      ||
                input.equalsIgnoreCase("1")
        );
    }
}
