package com.manulaiko.weabot.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.manulaiko.weabot.launcher.Settings;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Remove image save channel command.
 *
 * Removes a channel from the list.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class RemoveImageSaveChannelCommand extends Command
{
    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        if(args.length != 2) {
            event.getChannel()
                 .sendMessage(this.getDescription());

            return;
        }

        if(!Settings.isAdminOrMod(event.getAuthor())) {
            event.getChannel()
                 .sendMessage("This command can only be used by moderators or administrators.");

            return;
        }

        for(TextChannel c : Settings.saveImagesChannels.keySet()) {
            if(c.getName().equalsIgnoreCase(args[1])) {
                Settings.saveImagesChannels.remove(c);
                event.getChannel()
                     .sendMessage("From now over, the images posted on `"+ args[1] +"` won't be saved!");

                return;
            }
        }
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "remove_save_images_from_channel";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        return "Stops saving the images posted in a channel.\n"+
               "\n"+
               "Usage:\n"+
               "    "+ this.getFullName() +" (channel_name)";
    }
}
