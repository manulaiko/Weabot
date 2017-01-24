package com.manulaiko.weabot.listerners;

import java.util.List;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.dao.messages.Factory;
import com.manulaiko.weabot.dao.messages.Message;
import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Mention listener.
 *
 * Listens for mentions to Weabot and answers.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class MentionListener extends ListenerAdapter
{
    /**
     * On message received.
     *
     * Processes the message and checks if they mention Weabot.
     *
     * @param e Event fired.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        List<User> mentions = e.getMessage().getMentionedUsers();

        if(mentions.size() == 0) {
            return;
        }

        for(User u : mentions) {
            if(u.getId().equals(Main.weabot.jda.getSelfUser().getId())) {
                this.sendMessage(e.getTextChannel());

                return;
            }
        }
    }

    /**
     * Sends a random message.
     *
     * @param channel Channel to send the message.
     */
    public void sendMessage(TextChannel channel)
    {
        Message m = Factory.getRandomMessage("mention");

        channel.sendMessage(m.text).queue();
    }
}
