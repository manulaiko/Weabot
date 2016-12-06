package com.manulaiko.weabot.launcher.commands;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.utils.ICommand;
import com.manulaiko.weabot.launcher.Main;
import com.manulaiko.weabot.launcher.Settings;

/**
 * Reload configuration command.
 *
 * Reloads configuration file.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class ReloadConfiguration implements ICommand
{
    /**
     * Executes the command
     *
     * @param command Command arguments.
     */
    @Override
    public void execute(String[] command)
    {
        Main.loadConfiguration();
    }

    /**
     * Checks whether the command can be executed.
     *
     * @param name Command name to check.
     *
     * @return `true` if `name` is `reloadConfiguration`, `false` if not.
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
        return "reloadConfiguration";
    }

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    @Override
    public String getDescription()
    {
        return this.getName() +" : Reloads configuration file.";
    }
}
