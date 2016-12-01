package com.manulaiko.weabot.launcher.commands;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.utils.ICommand;
import com.manulaiko.weabot.launcher.Main;
import com.manulaiko.weabot.launcher.Settings;

/**
 * Add moderator command.
 *
 * Adds an user as moderator.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class AddModCommand implements ICommand
{
    /**
     * Executes the command
     *
     * @param command Command arguments.
     */
    @Override
    public void execute(String[] command)
    {
        if(command.length != 2) {
            Console.println(this.getDescription());

            return;
        }

        Main.jda.getUsers().forEach((u)->{
            if(
                u.getUsername().equalsIgnoreCase(command[1]) ||
                u.getId().equalsIgnoreCase(command[1])
            ) {
                Settings.moderators.add(u);

                Console.println(u.getUsername() +" is now a moderator!");
            }
        });
    }

    /**
     * Checks whether the command can be executed.
     *
     * @param name Command name to check.
     *
     * @return `true` if `name` is `add_mod`, `false` if not.
     */
    @Override
    public boolean canExecute(String name)
    {
        return name.equalsIgnoreCase(this.getName());
    }

    /**
     * Returns command name.
     *
     * @return Command name.
     */
    public String getName()
    {
        return "add_mod";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return this.getName() +" [user]: Adds `user` to the moderator list.";
    }
}
