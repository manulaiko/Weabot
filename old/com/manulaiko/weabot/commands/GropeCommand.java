package com.manulaiko.weabot.commands;

import java.util.concurrent.ThreadLocalRandom;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Grope command.
 *
 * Gropes another user.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class GropeCommand extends Command
{
    /**
     * Available pats.
     */
    public String[] pats = new String[] {
        "https://i.imgur.com/NiYA2oa.gif",
        "https://i.imgur.com/gvQgnDs.gif",
        "https://i.imgur.com/4uiPJGe.gif",
        "https://i.imgur.com/WFOQbgX.gif",
        "https://i.imgur.com/rpyErVM.gif"
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
             .sendMessage(petted +"was groped by "+ event.getAuthorName() +"\n"+ this.pats[random]);
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "grope";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        return "Gropes another user.\n" +
               "\n" +
               "Usage:\n" +
               "    "+ this.getFullName() +" [user]\n";
    }
}
