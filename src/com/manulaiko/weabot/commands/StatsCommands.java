package com.manulaiko.weabot.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import com.manulaiko.weabot.launcher.Main;
import com.manulaiko.weabot.launcher.Settings;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Prints bot's stats.
 *
 * Prints bot's stats.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class StatsCommands extends Command
{
    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        if(!Settings.isAdminOrMod(event.getAuthor())) {
            event.getTextChannel()
                 .sendMessage("This command can only be used by administrators or moderators!");

            return;
        }
        String message = "";

        message += "Running for: "+ Main.getRuntime() +"\n";
        message += "\n";
        message += "Images from channels saved:\n";
        message += "```\n";

        for(Map.Entry<String, ArrayList<File>> entry : Settings.saveImagesChannels.entrySet()) {
            String          channel = entry.getKey();
            ArrayList<File> paths   = entry.getValue();

            message += channel +":\n";
            for(File path : paths) {
                message += "    " + path.getAbsolutePath();
            }

            message += "\n";
        }

        message += "```";

        event.getTextChannel().sendMessage(message);
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "stats";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        return "Prints bot's stats.";
    }
}
