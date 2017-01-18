package com.manulaiko.weabot.launcher;

import java.util.HashMap;

import com.manulaiko.weabot.commands.ICommand;
import com.manulaiko.weabot.listerners.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Weabot class.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Weabot
{
    /**
     * JDA instance.
     *
     * The instance of the JDA library used for interacting with Discord API.
     */
    public JDA jda;

    /**
     * Bot commands.
     *
     * Commands that the bot can execute.
     */
    public HashMap<String, ICommand> commands = new HashMap<>();

    /**
     * Constructor.
     *
     * @param token Bot token.
     */
    public Weabot(String token) throws Exception
    {
        this.jda = this._loadBotAccount(token);

        this.init();
    }

    /**
     * Constructor.
     *
     * @param username User name.
     * @param password User password.
     */
    public Weabot(String username, String password) throws Exception
    {
        this.jda = this._loadUserAccount(username, password);

        this.init();
    }

    /**
     * Loads a bot account.
     *
     * @param token Bot token.
     *
     * @return JDA instance.
     *
     * @throws Exception If something goes wrong.
     */
    private JDA _loadBotAccount(String token) throws Exception
    {
        JDABuilder builder = new JDABuilder(AccountType.BOT);

        builder.setToken(token);

        return builder.buildBlocking();
    }

    /**
     * Loads an user account.
     *
     * @param username User name.
     * @param password Password
     *
     * @return JDA instance.
     *
     * @throws Exception If something goes wrong.
     */
    private JDA _loadUserAccount(String username, String password) throws Exception
    {
        JDABuilder builder = new JDABuilder(AccountType.CLIENT);

        throw new Exception("User account login isn't supported yet!");
    }

    /**
     * Initializes the bot.
     */
    private void init()
    {
        this._addListeners();
        this._addCommands();
    }

    /**
     * Adds the listeners to the JDA instance.
     */
    private void _addListeners()
    {
        ListenerAdapter[] listeners = new ListenerAdapter[]{
                new MessageListener()
        };

        for(ListenerAdapter l : listeners) {
            this.jda.addEventListener(l);
        }
    }

    /**
     * Adds the commands to the array.
     */
    private void _addCommands()
    {
    }
}
