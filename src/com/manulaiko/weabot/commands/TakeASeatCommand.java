package com.manulaiko.weabot.commands;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

import com.manulaiko.tabitha.Console;
import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Take a seat command.
 *
 * Ask an user to take a seat over there.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class TakeASeatCommand extends Command
{
    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        try {
            if(args.length < 2) {
                return;
            }

            String user = "";
            for(int i = 1; i < args.length; i++) {
                user += args[i] + " ";
            }

            if(user.startsWith("@")) {
                user = user.substring(1, user.length());
            }

            BufferedImage image = ImageIO.read(
                    new File(Main.configuration.getString("take_a_seat.location"))
            );

            Graphics g = image.getGraphics();
            g.setFont(g.getFont().deriveFont(
                    Main.configuration.getFloat("take_a_seat.size")
            ));
            g.setColor(new Color(
                    Main.configuration.getInt("take_a_seat.r"),
                    Main.configuration.getInt("take_a_seat.g"),
                    Main.configuration.getInt("take_a_seat.b")
            ));
            g.drawString(
                    "Why don't you take a seat over there, "+ user +"?",
                    Main.configuration.getInt("take_a_seat.x"),
                    Main.configuration.getInt("take_a_seat.y")
            );

            File f = File.createTempFile("take-a-sit"+ user, ".png");
            ImageIO.write(image, "png", f);

            event.getTextChannel().sendFile(f, null);
        } catch(Exception e) {
            // Ignore
        }
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "seat";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        return "Ask an user to take a seat over there\n" +
                "\n" +
                "Usage:\n" +
                "    "+ this.getFullName() +" [user]\n";
    }
}
