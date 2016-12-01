package com.manulaiko.weabot.launcher.commands;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.utils.ICommand;
import com.manulaiko.weabot.launcher.Main;
import com.manulaiko.weabot.launcher.Settings;

/**
 * Add administrator command.
 *
 * Adds an user as administrator.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class AddAdminCommand implements ICommand
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
                Settings.administrators.add(u);

                Console.println(u.getUsername() +" is now an administrator!");
            }
        });
    }

    /**
     * Checks whether the command can be executed.
     *
     * @param name Command name to check.
     *
     * @return `true` if `name` is `add_admin`, `false` if not.
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
        return "add_admin";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return this.getName() +" [user]: Adds `user` to the administrators list.";
    }
}
