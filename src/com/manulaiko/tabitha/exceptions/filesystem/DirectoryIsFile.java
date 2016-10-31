package com.manulaiko.tabitha.exceptions.filesystem;

/**
 * Directory is a file exception.
 *
 * This exception is thrown when you try to use a file
 * as if it was a directory
 *
 * The constructor accepts 1 parameter:
 *  * name: The name of the directory that is a file
 *
 * Example:
 *
 *     try {
 *         Directory d = new Directory("text");
 *     } catch(DirectoryIsFile e) {
 *         Console.println("The directory "+ e.name +" is a file!");
 *     }
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class DirectoryIsFile extends Exception
{
    /**
     * Directory's name
     */
    public String name = "";

    /**
     * Constructor
     *
     * @param name Directory's name
     */
    public DirectoryIsFile(String name)
    {
        super("The directory "+ name +" is a file!");

        this.name = name;
    }
}