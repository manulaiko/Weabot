package com.manulaiko.tabitha.exceptions.filesystem;

/**
 * Directory already exists exception.
 *
 * This exception is thrown when you try to create an
 * already existing directory
 *
 * The constructor accepts 1 parameter:
 *  * name: The name of the directory that already exists
 *
 * Example:
 *
 *     try {
 *         Directory d = new Directory("text");
 *     } catch(DirectoryAlreadyExists e) {
 *         Console.println("The directory "+ e.name +" already exists!");
 *     }
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class DirectoryAlreadyExists extends Exception
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
    public DirectoryAlreadyExists(String name)
    {
        super("The directory "+ name +" already exists!");
        
        this.name = name;
    }
}