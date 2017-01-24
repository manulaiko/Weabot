package com.manulaiko.weabot.listerners;

import java.io.File;
import java.util.List;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.dao.scrappers.Factory;
import com.manulaiko.weabot.dao.scrappers.Scrapper;
import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Image scrapper.
 *
 * Downloads attached images.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class ImageScrapper extends ListenerAdapter
{
    /**
     * On message received.
     *
     * Processes the message.
     *
     * @param e Event fired.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        Message m = e.getMessage();

        m.getAttachments().forEach((a)->{
            Thread t = new Thread() {
                public void run()
                {
                    download(a, m.getTextChannel());
                }
            };

            try {
                t.start();
            } catch(Exception ex) {
                // Ignore
            }
        });
    }

    /**
     * Downloads an attachment.
     *
     * @param attachment Attachment to download.
     * @param channel    Channel where the attachment was posted.
     */
    public void download(Message.Attachment attachment, TextChannel channel)
    {
        if(!attachment.isImage()) {
            return;
        }

        List<Scrapper> scrappers = Factory.byChannel(channel);

        scrappers.forEach((s)->{
            File f = new File(s.path + attachment.getFileName());

            if(f.exists()) {
                f = new File(s.path + System.currentTimeMillis() +"-"+ attachment.getFileName());
            }

            if(
                !f.getParentFile().exists() &&
                f.getParentFile().mkdirs()
            ) {
                Console.println("Couldn't make directories for "+ s.path +"!");

                return;
            }

            attachment.download(f);

            Main.weabot.scrappedImages++;
        });
    }
}
