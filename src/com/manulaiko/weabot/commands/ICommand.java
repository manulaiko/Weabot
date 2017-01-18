package com.manulaiko.weabot.commands;

/**
 * Command interface.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public interface ICommand
{
    /**
     * Returns command name.
     *
     * @return Command name.
     */
    String getName();

    /**
     * Returns command description.
     *
     * @return Command description.
     */
    String getDescription();

    /**
     * Executes the command.
     *
     * @param args Command arguments.
     */
    void execute(String[] args);
}
