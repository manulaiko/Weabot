package com.manulaiko.weabot.commands;

import java.io.File;
import java.util.ArrayList;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.launcher.Settings;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Add image save channel command.
 *
 * Adds a channel to the list.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class AddImageSaveChannelCommand extends Command
{
    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        if(args.length != 3) {
            event.getChannel()
                 .sendMessage(this.getDescription());

            return;
        }

        if(!Settings.isAdminOrMod(event.getAuthor())) {
            event.getChannel()
                 .sendMessage("This command can only be used by moderators or administrators.");

            return;
        }

        File f = new File(args[2]);
        if(!f.exists()) {
            Console.debug("Directories for `"+ f.getAbsolutePath() +"` created!");

            f.mkdirs();
        }
        if(f.isFile()) {
            event.getChannel()
                 .sendMessage("`"+ args[2] +"` is a file!");

            return;
        }

        TextChannel channel = null;
        for(TextChannel c : event.getGuild().getTextChannels()) {
            if(c.getName().equalsIgnoreCase(args[1])) {
                channel = c;
            }
        }
        if(channel == null) {
            event.getChannel()
                 .sendMessage("Channel `"+ args[1] +"` does not exist!");

            return;
        }

        for(String c : Settings.saveImagesChannels.keySet()) {
            if(c.equalsIgnoreCase(channel.getName())) {
                Settings.saveImagesChannels.get(c).add(f);
                event.getChannel()
                     .sendMessage("From now over, all images posted in `"+ channel.getName() +"` will also be saved in `"+ f.getAbsolutePath() +"`");

                return;
            }
        }

        ArrayList<File> array = new ArrayList<>();
        array.add(f);

        Settings.saveImagesChannels.put(channel.getName(), array);

        event.getChannel()
             .sendMessage("From now over, all images posted in `"+ channel.getName() +"` will be saved in `"+ f.getAbsolutePath() +"`");
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "add_save_images_from_channel";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        return "Saves the images posted in a channel.\n"+
               "\n"+
               "Usage:\n"+
               "    "+ this.getFullName() +" (channel_name) (path)";
    }
}
