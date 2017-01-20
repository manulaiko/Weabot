package com.manulaiko.weabot.commands;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
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
        return this.getFullName() +" ( ([key] [value]) | ([user] [key] [value]) | help)";
    }

    /**
     * Executes the command.
     *
     * @param e    Event that fired the command.
     * @param args Command arguments.
     */
    @Override
    public void execute(Event e, String[] args)
    {
        MessageReceivedEvent event = (MessageReceivedEvent)e;
        if(args.length <= 1) {
            super.printUsage(event.getTextChannel());

            return;
        }

        if(args[1].equalsIgnoreCase("help")) {
            this.printHelp(event.getTextChannel());

            return;
        }

        String                             key   = args[1];
        String                             value = args[2];
        net.dv8tion.jda.core.entities.User user  = event.getAuthor();

        if(key.startsWith("@")) {
            if(args.length <= 3) {
                super.printUsage(event.getTextChannel());

                return;
            }

            user = event.getMessage().getMentionedUsers().get(0);

            key   = value;
            value = args[3];
        }

        this._handle(event.getTextChannel(), user, key, value);
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

        channel.sendMessage(message);
    }

    /**
     * Handles request.
     *
     * @param channel Channel to print the result.
     * @param user    Mentioned user.
     * @param key     Requested configuration.
     * @param value   New value of `key`.
     */
    private void _handle(TextChannel channel, net.dv8tion.jda.core.entities.User user, String key, String value)
    {
        User u = Factory.find(user);

        if(!u.canChangeOthersConfig()) {
            channel.sendMessage("You can't change other's configuration!");

            return;
        }

        Permission permission = com.manulaiko.weabot.dao.permissions.Factory.find(key);

        if(permission == null) {
            this.printHelp(channel);

            return;
        }

        if(u.rank < permission.rank) {
            channel.sendMessage("You don't have enough privileges to change this permission!");

            return;
        }

        if(u.permissions.contains(permission)) {
            if(!this._asBool(value)) {
                this._deletePermission(u, permission);

                channel.sendMessage("Permission deleted!");

                return;
            }

            return;
        }

        if(this._asBool(value)) {
            this._addPermission(u, permission);

            channel.sendMessage("Permission added!");

            return;
        }

        channel.sendMessage("You can't remove a permission you don't have!");
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
