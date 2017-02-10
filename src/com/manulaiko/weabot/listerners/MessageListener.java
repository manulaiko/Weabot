package com.manulaiko.weabot.listerners;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.dao.stats.Factory;
import com.manulaiko.weabot.dao.stats.Stat;
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
        Main.weabot.receivedMessages++;
        Stat stat = Factory.find("received_messages");
        try {
            long val  = Long.parseLong(stat.text) + 1;
            stat.text = val +"";

            stat.save();
        } catch(Exception ex) {
            Console.println("Couldn't update stats!");
            Console.println(ex.getMessage());
        }

        if(Main.configuration.getBoolean("core.print_messages")) {
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
        String   prefix  = Main.configuration.getString("core.prefix");

        if(!message.startsWith(prefix)) {
            return;
        }

        String command = args[0].substring(prefix.length(), args[0].length());

        Main.weabot.commands.forEach((i, c) -> {
            if(command.equalsIgnoreCase(i)) {
                Main.weabot.executedCommands++;
                Stat s = com.manulaiko.weabot.dao.stats.Factory.find("scrapped_images");
                try {
                    long val = Long.parseLong(s.text) + 1;
                    s.text   = val +"";

                    s.save();
                } catch(Exception ex) {
                    Console.println("Couldn't update stats!");
                    Console.println(ex.getMessage());
                }

                c.execute(e, args);
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
        try {
            String format = Main.configuration.getString("messages.format");

            return format.replaceAll("\\{MESSAGE\\}", message)
                         .replaceAll("\\{AUTHOR\\}", author)
                         .replaceAll("\\{CHANNEL\\}", channel)
                         .replaceAll("\\{GUILD\\}", guild);
        } catch(Exception e) {
            Console.println("Couldn't format message!");
        }

        return message;
    }
}
