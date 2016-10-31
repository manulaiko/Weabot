package com.manulaiko.tabitha.exceptions.filesystem;

/**
 * File is a directory exception.
 *
 * This exception is thrown when you try to use a directory
 * as if it was a file
 *
 * The constructor accepts 1 parameter:
 *  * name: The name of the file that is a directory
 *
 * Example:
 *
 *     try {
 *         File f = new File("text");
 *     } catch(FileIsDirectory e) {
 *         Console.println("The file "+ e.name +" is a directory!");
 *     }
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class FileIsDirectory extends Exception
{
    /**
     * File's name
     */
    public String name = "";

    /**
     * Constructor
     *
     * @param name File's name
     */
    public FileIsDirectory(String name)
    {
        super("The file "+ name +" is a directory!");

        this.name = name;
    }
}