package com.manulaiko.weabot.commands;

import java.util.List;

import com.manulaiko.weabot.dao.scrappers.Factory;
import com.manulaiko.weabot.dao.scrappers.Scrapper;
import com.manulaiko.weabot.launcher.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Stats command.
 *
 * Prints bot's stats.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Stats extends Command
{
    /**
     * Executes the command.
     *
     * @param event Event that fired the command.
     * @param args  Command arguments.
     */
    public void execute(MessageReceivedEvent event, String[] args)
    {
        EmbedBuilder builder = new EmbedBuilder();

        builder.addField("Runtime", this.runtime(), true);
        builder.addField("Version", Main.version, true);

        builder.addBlankField(false);

        builder.addField("Received messages", this.receivedMessages(), true);
        builder.addField("Executed commands", this.executedCommands(), true);

        builder.addBlankField(false);

        builder.addField("Scrappers", this.scrappers(), false);

        builder.addField("Scrapped images", this.scrappedImages(), true);
        builder.addField("Users", this.users(event.getGuild()), true);

        event.getTextChannel().sendMessage(builder.build()).queue();
    }

    /**
     * Returns runtime.
     *
     * @return Runtime.
     */
    public String runtime()
    {
        long millis = System.currentTimeMillis() - Main.start;

        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour   = (millis / (1000 * 60 * 60)) % 24;

        String runtime = String.format("%02d minutes and %02d seconds", minute, second);
        if(hour > 0) {
            runtime = String.format("%02d hours %02d minutes and %02d seconds", hour, minute, second);
        }

        return runtime;
    }

    /**
     * Returns received messages.
     *
     * @return Received messages.
     */
    public String receivedMessages()
    {
        return Main.weabot.receivedMessages +"";
    }

    /**
     * Returns executed commands.
     *
     * @return Executed commands.
     */
    public String executedCommands()
    {
        return Main.weabot.executedCommands +"";
    }

    /**
     * Returns scrappers list.
     *
     * @return Scrappers list.
     */
    public String scrappers()
    {
        List<Scrapper> scrappers = Factory.all();
        String         message   = "```\n";

        for(Scrapper s : scrappers) {
            message += " - "+ s.id +": "+ s.channelID +" -> "+ s.path +"\n";
        }

        return message + "```";
    }

    /**
     * Returns scrapped images.
     *
     * @return Scrapped images.
     */
    public String scrappedImages()
    {
        return Main.weabot.scrappedImages +"";
    }

    /**
     * Returns users.
     *
     * @param g Guild.
     *
     * @return Users.
     */
    public String users(Guild g)
    {
        List<Member> users   = g.getMembers();
        int          offline = 0;
        int          total   = users.size();
        int          bots    = 0;

        for(Member u : users) {
            if(u.getOnlineStatus().getKey().equals(OnlineStatus.ONLINE.getKey())) {
                offline++;
            }

            if(u.getUser().isBot()) {
                bots++;
            }
        }

        return (total - offline) +"/"+ total +" ("+ bots +" bots)";
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "stats";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    public String getDescription()
    {
        return "Prints bot's stats.";
    }

    /**
     * Returns command usage.
     *
     * @return Command usage.
     */
    public String getUsage()
    {
        return this.getFullName();
    }
}
