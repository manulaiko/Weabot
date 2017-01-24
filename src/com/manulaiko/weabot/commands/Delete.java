package com.manulaiko.weabot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulaiko.weabot.dao.messages.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import com.manulaiko.weabot.dao.images.Image;
import com.manulaiko.weabot.dao.permissions.Factory;
import com.manulaiko.weabot.dao.permissions.Permission;
import com.manulaiko.weabot.dao.scrappers.Scrapper;
import com.manulaiko.weabot.dao.users.User;

/**
 * Delete command.
 *
 * Used to delete entries to from database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Delete extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "delete";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Deletes entries from the database";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    @Override
    public String getUsage()
    {
        return this.getFullName() +" [type] ([args]...)";
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

        User author = com.manulaiko.weabot.dao.users.Factory.find(e.getAuthor());
        if(author.rank < 3) {
            e.getTextChannel().sendMessage("You can't use this command!").queue();

            return;
        }

        HashMap<String, Option> commands = this.getOptions();

        if(!commands.containsKey(args[1])) {
            this.printHelp(e.getTextChannel());

            return;
        }

        commands.get(args[1]).handle(args, e.getTextChannel());
    }

    /**
     * Returns available options.
     *
     * @return Available options.
     */
    public HashMap<String, Option> getOptions()
    {
        HashMap<String, Option> commands = new HashMap<>();

        commands.put("message", new Option() {
            /**
             * Returns option description.
             *
             * @return Option description.
             */
            @Override
            public String getDescription()
            {
                return "Deletes a message from the database.";
            }

            /**
             * Handles the option.
             *
             * @param args    Option arguments.
             * @param channel Channel
             */
            @Override
            public void handle(String[] args, TextChannel channel)
            {
                if(args.length < 3) {
                    this.printUsage(channel);

                    return;
                }

                int id = 0;
                try {
                    id = Integer.parseInt(args[2]);
                } catch(Exception e) {
                    this.printUsage(channel);

                    return;
                }

                Message m = com.manulaiko.weabot.dao.messages.Factory.find(id);
                if(m == null) {
                    channel.sendMessage("Message with ID "+ id +" does not exist!").queue();

                    return;
                }

                if(com.manulaiko.weabot.dao.messages.Factory.delete(m)) {
                    channel.sendMessage("Message deleted!").queue();

                    return;
                }

                channel.sendMessage("Couldn't delete message!").queue();
            }

            /**
             * Prints option usage.
             *
             * @param channel Channel to send the message.
             */
            public void printUsage(TextChannel channel)
            {
                String message = "Options:\n" +
                        "```\n" +
                        "message [id]\n" +
                        "```\n";

                channel.sendMessage(message).queue();
            }
        });
        commands.put("permission", new Option() {
            /**
             * Returns option description.
             *
             * @return Option description.
             */
            @Override
            public String getDescription()
            {
                return "Deletes a permission from the database.";
            }

            /**
             * Handles the option.
             *
             * @param args    Option arguments.
             * @param channel Channel
             */
            @Override
            public void handle(String[] args, TextChannel channel)
            {
                if(args.length < 3) {
                    this.printUsage(channel);

                    return;
                }

                try {
                    int id = Integer.parseInt(args[2]);

                    Permission p = Factory.find(id);
                    if(p == null) {
                        channel.sendMessage("Permission with ID "+ id +" does not exist!").queue();

                        return;
                    }

                    if(Factory.delete(p)) {
                        channel.sendMessage("Permission deleted!");

                        return;
                    }

                    channel.sendMessage("Couldn't delete permission!");

                    return;
                } catch(Exception e) {
                    // Ignore
                }

                Permission p = Factory.find(args[2]);
                if(p == null) {
                    channel.sendMessage("Permission with name `"+ args[2] +"` does not exist!").queue();

                    return;
                }

                if(Factory.delete(p)) {
                    channel.sendMessage("Permission deleted!");

                    return;
                }

                channel.sendMessage("Couldn't delete permission!");
            }

            /**
             * Prints option usage.
             *
             * @param channel Channel to send the message.
             */
            public void printUsage(TextChannel channel)
            {
                String message = "Options:\n" +
                        "```\n" +
                        "permission ([id]|[name])\n" +
                        "```\n" +
                        "`name` can't contain whitespaces!";

                channel.sendMessage(message).queue();
            }
        });
        commands.put("image", new Option() {
            /**
             * Returns option description.
             *
             * @return Option description.
             */
            @Override
            public String getDescription()
            {
                return "Deletes an image from the database.";
            }

            /**
             * Handles the option.
             *
             * @param args    Option arguments.
             * @param channel Channel
             */
            @Override
            public void handle(String[] args, TextChannel channel)
            {
                if(args.length < 3) {
                    this.printUsage(channel);

                    return;
                }

                try {
                    int id = Integer.parseInt(args[2]);

                    Image i = com.manulaiko.weabot.dao.images.Factory.find(id);
                    if(i == null) {
                        channel.sendMessage("Image with ID "+ id +" does not exist!").queue();

                        return;
                    }

                    if(com.manulaiko.weabot.dao.images.Factory.delete(i)) {
                        channel.sendMessage("Image deleted!");

                        return;
                    }

                    channel.sendMessage("Couldn't delete image!");
                } catch(Exception e) {
                    this.printUsage(channel);
                }
            }

            /**
             * Prints option usage.
             *
             * @param channel Channel to send the message.
             */
            public void printUsage(TextChannel channel)
            {
                String message = "Options:\n" +
                        "```\n" +
                        "image [id]\n" +
                        "```";

                channel.sendMessage(message).queue();
            }
        });
        commands.put("scrapper", new Option() {
            /**
             * Returns option description.
             *
             * @return Option description.
             */
            @Override
            public String getDescription()
            {
                return "Deletes an image scrapper from the database.";
            }

            /**
             * Handles the option.
             *
             * @param args    Option arguments.
             * @param channel Channel
             */
            @Override
            public void handle(String[] args, TextChannel channel)
            {
                if(args.length < 3) {
                    this.printUsage(channel);

                    return;
                }

                try {
                    int id = Integer.parseInt(args[2]);

                    Scrapper s = com.manulaiko.weabot.dao.scrappers.Factory.find(id);
                    if(s == null) {
                        channel.sendMessage("Scrapper with ID "+ id +" does not exist!").queue();

                        return;
                    }

                    if(com.manulaiko.weabot.dao.scrappers.Factory.delete(s)) {
                        channel.sendMessage("Scrapper deleted!").queue();

                        return;
                    }

                    channel.sendMessage("Couldn't delete scrapper!");

                    return;
                } catch(Exception e) {
                    // Ignore
                }

                if(args.length != 4) {
                    this.printUsage(channel);

                    return;
                }

                String option = args[2];
                String value  = args[3];

                List<Scrapper> scrappers = new ArrayList<>();

                if(option.equalsIgnoreCase("channel")) {
                    scrappers = com.manulaiko.weabot.dao.scrappers.Factory.byChannel(value);
                } else if(option.equalsIgnoreCase("path")) {
                    scrappers = com.manulaiko.weabot.dao.scrappers.Factory.byPath(value);
                }

                if(scrappers.size() <= 0) {
                    channel.sendMessage("No scrapper found for `"+ option +" = "+ value +"`").queue();

                    return;
                }

                String result = "Result:\n" +
                                "```\n";

                for(Scrapper s : scrappers) {
                    if(com.manulaiko.weabot.dao.scrappers.Factory.delete(s)) {
                        result += " - Deleted scrapper with ID "+ s.id +": "+ s.channelID +" -> "+ s.path +"\n";
                    } else {
                        result += " - Couldn't delete scrapper with ID "+ s.id +": "+ s.channelID +" -> "+ s.path +"\n";
                    }
                }

                result += "```";

                channel.sendMessage(result).queue();
            }

            /**
             * Prints option usage.
             *
             * @param channel Channel to send the message.
             */
            public void printUsage(TextChannel channel)
            {
                String message = "Options:\n" +
                        "```\n" +
                        "scrapper ( ([id] | ( (channel [channel_id]) | (path [path]) ) )\n" +
                        "```\n" +
                        "`path` can't contain whitespaces!";

                channel.sendMessage(message).queue();
            }
        });

        return commands;
    }

    /**
     * Prints help message.
     *
     * @param channel Channel to send the message.
     */
    public void printHelp(TextChannel channel)
    {
        String message = "Available commands:\n" +
                         "```\n";

        for(Map.Entry<String, Option> command : this.getOptions().entrySet()) {
            message += " - '"+ command.getKey() +"': "+ command.getValue().getDescription() +"\n";
        }

        message += "```";

        channel.sendMessage(message).queue();
    }

    public abstract class Option
    {
        /**
         * Returns option description.
         *
         * @return Option description.
         */
        public abstract String getDescription();

        /**
         * Handles the option.
         *
         * @param args    Option arguments.
         * @param channel Channel
         */
        public abstract void handle(String[] args, TextChannel channel);
    }
}
