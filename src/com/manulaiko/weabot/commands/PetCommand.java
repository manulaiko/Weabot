package com.manulaiko.weabot.commands;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Pet command.
 *
 * Pets another user.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class PetCommand extends Command
{
    /**
     * Available pats.
     */
    public String[] pats = new String[] {
        "https://i.imgur.com/9zQT0m9.gif",
        "https://i.imgur.com/5t6zdMg.gif",
        "https://i.imgur.com/e9Br8sp.gif",
        "https://i.imgur.com/jvApXrs.gif",
        "https://i.imgur.com/449566w.gif",
        "https://i.imgur.com/aNLFqm7.gif",
        "https://i.imgur.com/DfIjLvm.gif",
        "https://i.imgur.com/hWXAacQ.gif"
    };

    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        if(args.length < 2) {
            return;
        }

        String petted = "";
        for(int i = 1; i < args.length; i++) {
            petted += args[i] +" ";
        }

        if(petted.startsWith("@")) {
            petted = petted.substring(1, petted.length());
        }

        int random = ThreadLocalRandom.current()
                                      .nextInt(0, this.pats.length);

        event.getChannel()
             .sendMessage(petted +"was petted by "+ event.getAuthorName() +"\n"+ this.pats[random]);
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "pet";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        return "Pets another user.\n" +
                "\n" +
                "Usage:\n" +
                "    "+ this.getFullName() +" [user]\n";
    }
}
