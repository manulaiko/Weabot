package com.manulaiko.weabot.launcher;

import java.util.ArrayList;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.utils.ICommand;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * Message listener class.
 *
 * Listens for messages and process them.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class MessageListener extends ListenerAdapter
{
    /**
     * Available commands array.
     *
     * @var Available commands.
     */
    public ArrayList<ICommand> commands = new ArrayList();

    /**
     * Constructor.
     */
    public MessageListener()
    {
        super();
    }

    /**
     * Listens and process messages.
     *
     * @param event Fired event.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(Main.configuration.getBoolean("core.printMessages")) {
            Console.println("["+ event.getTextChannel().getName() +"] ("+ event.getAuthorName() +"): "+ event.getMessage());
        }

        String[] command = event.getMessage().getContent().split(" ");

        this.commands.forEach((c)->{
            if(c.canExecute(command[0])) {
                c.execute(command);
            }
        });
    }
}
