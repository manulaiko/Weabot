package com.manulaiko.weabot.launcher;

import java.io.File;
import java.util.ArrayList;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.commands.*;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
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
    public ArrayList<Command> commands = new ArrayList<>();

    /**
     * Constructor.
     */
    public MessageListener()
    {
        super();

        this.commands.add(new HelpCommand());
        this.commands.add(new WhatToDoCommand());
        this.commands.add(new SearchAnimeCommand());
        this.commands.add(new AddImageSaveChannelCommand());
        this.commands.add(new RemoveImageSaveChannelCommand());
        this.commands.add(new ExecuteCommand());
        this.commands.add(new PetCommand());
    }

    /**
     * Listens and process messages.
     *
     * @param event Fired event.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(
            Main.configuration.getBoolean("core.printMessages") &&
            !event.getMessage().getContent().isEmpty()
        ) {
            Console.println("["+ event.getTextChannel().getName() +"] ("+ event.getAuthorName() +"): "+ event.getMessage().getContent());
        }

        String[] command = event.getMessage()
                                .getContent()
                                .split(" ");

        this.commands.forEach((c)->{
            if(c.canExecute(command[0])) {
                c.execute(event, command);
            }
        });

        for(Message.Attachment a : event.getMessage().getAttachments()) {
            if(!a.isImage()) {
                continue;
            }

            this._saveImage(a, event.getTextChannel());
        }
    }

    /**
     * Saves an image.
     *
     * @param attachment Image attachment.
     * @param channel    Channel where the image was posted.
     */
    private void _saveImage(Message.Attachment attachment, TextChannel channel)
    {
        Settings.saveImagesChannels.forEach((c, f)->{
            Thread t = new Thread()
            {
                public void run()
                {
                    if(!c.getId().equals(channel.getId())) {
                        return;
                    }

                    f.forEach((path)->{
                        File image = new File(path.getAbsolutePath() + File.separator + attachment.getFileName());
                        if(image.exists()) {
                            image = new File(path.getAbsolutePath() + File.separator + System.currentTimeMillis() + "-" + attachment.getFileName());
                        }
                        attachment.download(image);
                    });
                }
            };
            t.start();
        });
    }
}
