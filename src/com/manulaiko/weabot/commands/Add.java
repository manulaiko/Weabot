package com.manulaiko.weabot.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulaiko.weabot.dao.images.Image;
import com.manulaiko.weabot.dao.permissions.Factory;
import com.manulaiko.weabot.dao.permissions.Permission;
import com.manulaiko.weabot.dao.scrappers.Scrapper;
import com.manulaiko.weabot.dao.users.User;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Add command.
 *
 * Used to add entries to the database.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Add extends Command
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    @Override
    public String getName()
    {
        return "add";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return "Adds entries to the database";
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
        if(author.rank < 2) {
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

        commands.put("permission", new Option() {
            /**
             * Returns option description.
             *
             * @return Option description.
             */
            @Override
            public String getDescription()
            {
                return "Adds a new permission to the database.";
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
                if(args.length < 4) {
                    this.printUsage(channel);

                    return;
                }

                String name;
                String desc;
                int    rank;

                try {
                    name = args[2];
                    rank = Integer.parseInt(args[3]);
                    desc = "";
                } catch(Exception e) {
                    this.printUsage(channel);

                    return;
                }

                for(int i = 4; i < args.length; i++) {
                    if(!desc.isEmpty()) {
                        desc += " ";
                    }

                    desc += args[i];
                }

                Permission p = Factory.create(name, rank, desc);

                if(p.id == 0) {
                    channel.sendMessage(
                            "Couldn't insert permission!\n" +
                            "Name: `"+ name +"`\n"          +
                            "Rank: "+ rank +"\n"            +
                            "Description: `"+ desc +"`"
                    ).queue();

                    return;
                }

                channel.sendMessage("Permission `"+ p.name +"` created!\nID: "+ p.id).queue();
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
                        "permission [name] [rank] [description]\n" +
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
                return "Adds a new image to the database.";
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
                if(args.length < 4) {
                    this.printUsage(channel);

                    return;
                }

                String link = args[2];
                String cat  = args[3];

                Image i = com.manulaiko.weabot.dao.images.Factory.create(link, cat);

                if(i.id == 0) {
                    channel.sendMessage("Couldn't insert image!").queue();

                    return;
                }

                channel.sendMessage("Image `"+ i.link +"` created!\nID: "+ i.id).queue();
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
                        "image [link] [category]\n" +
                        "```\n" +
                        "Neither `link` nor `category` can't contain whitespaces!";

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
                return "Adds a new image scrapper to the database.";
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

                TextChannel c = null;
                String      p = args[2];

                if(args.length == 3) {
                    c = channel;
                } else if(args.length == 4) {
                    List<TextChannel> channels = channel.getGuild().getTextChannels();

                    for(TextChannel tc : channels) {
                        if(tc.getName().equals(args[3])) {
                            c = tc;

                            break;
                        }
                    }
                }

                if(c == null) {
                    this.printUsage(channel);

                    return;
                }

                File f = new File(p);
                if(f.exists() && f.isFile()) {
                    channel.sendMessage("Path already exists and is a file!").queue();

                    return;
                }

                if(!f.mkdirs()) {
                    channel.sendMessage("Couldn't make directories!").queue();

                    return;
                }

                p = this.getPath(f.getAbsolutePath());

                Scrapper scrapper = com.manulaiko.weabot.dao.scrappers.Factory.create(c, p);

                if(scrapper.id == 0) {
                    channel.sendMessage("Couldn't create image scrapper!").queue();

                    return;
                }

                channel.sendMessage(
                        "Image scrapper created with ID "+ scrapper.id +"!\n" +
                        "From now over all images posted in channel with ID `"+ scrapper.channelID +"` " +
                        "will be saved on `"+ scrapper.path +"`!"
                ).queue();
            }

            /**
             * Returns prepared path.
             *
             * @param path Base path.
             *
             * @return Prepared `path`.
             */
            public String getPath(String path)
            {
                String separator = "/";

                if(System.getProperty("os.name").contains("Windows")) {
                    separator = "\\";

                    // Well, I won't think too much with this because I don't use
                    // winshit so I don't give a fuck if they're escaped correctly.
                    // If you have problems with paths while scrapping images, fix this.
                    path = path.replace("\\", "\\\\");
                }

                if(!path.endsWith(separator)) {
                    path += separator;
                }

                return path;
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
                        "scrapper [path] ([channel])\n" +
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
