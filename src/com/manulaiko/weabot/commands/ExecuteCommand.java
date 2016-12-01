package com.manulaiko.weabot.commands;

import com.manulaiko.weabot.launcher.Main;
import com.manulaiko.weabot.launcher.Settings;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Execute command.
 *
 * Executes any of the commands from command prompt.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class ExecuteCommand extends Command
{
    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        if(args.length <= 1) {
            event.getChannel()
                 .sendMessage(this.getDescription());

            return;
        }

        if(!Settings.isAdminOrMod(event.getAuthor())) {
            event.getChannel()
                 .sendMessage("This command can only be used by administrators or moderators!");

            return;
        }

        String[] command = new String[args.length - 1];
        for(int i = 1; i < args.length; i++) {
            command[(i - 1)] = args[i];
        }

        Main.commandPrompt.execute(command);

        event.getChannel()
             .sendMessage("Command executed!");

        return;
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "execute";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        return "Executes any of the commands from command prompt.\n"+
               "\n"+
               "Usage:\n"+
               "    "+ this.getFullName() +" ([command name])";
    }
}
