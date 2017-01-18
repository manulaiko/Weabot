package com.manulaiko.weabot.listerners;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Message listener.
 *
 * Listens for new messages and processes commands
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class MessageListener extends ListenerAdapter
{
    /**
     * On message received.
     *
     * Processes the message and executes the available commands.
     *
     * @param e Event fired.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(Main.configuration.getBoolean("messages.print")) {
            String message = this.formatMessage(
                    e.getMessage().getContent(),
                    e.getAuthor().getName(),
                    e.getChannel().getName(),
                    e.getGuild().getName()
            );

            Console.println(message);
        }

        String   message = e.getMessage().getContent();
        String[] args    = message.split(" ");
        String   preffix = Main.configuration.getString("core.preffix");

        if(!message.startsWith(preffix)) {
            return;
        }

        String command = args[0].substring(preffix.length(), (args[0].length() - 1));

        Main.weabot.commands.forEach((i, c) -> {
            if(command == i) {
                c.execute(args);
            }
        });
    }

    /**
     * Formats and returns a message.
     *
     * @param message Message content.
     * @param author  Author name.
     * @param channel Guild channel.
     * @param guild   Guild name.
     *
     * @return Formatted message.
     */
    public String formatMessage(String message, String author, String channel, String guild)
    {
        String format = Main.configuration.getString("messages.format");

        return format.replaceAll("\\{MESSAGE\\}", message)
                     .replaceAll("\\{AUTHOR\\}", author)
                     .replaceAll("\\{CHANNEL\\}", channel)
                     .replaceAll("\\{GUILD\\}", guild);
    }
}
