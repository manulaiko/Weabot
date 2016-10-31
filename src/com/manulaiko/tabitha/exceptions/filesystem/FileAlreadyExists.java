package com.manulaiko.tabitha.exceptions.filesystem;

/**
 * File already exists exception.
 *
 * This exception is thrown when you try to create an
 * already existing file
 *
 * The constructor accepts 1 parameter:
 *  * name: The name of the file that already exists
 *
 * Example:
 *
 *     try {
 *         File f = new File("text");
 *     } catch(FileAlreadyExists e) {
 *         Console.println("The file "+ e.name +" already exists!");   
 *     }
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class FileAlreadyExists extends Exception
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
    public FileAlreadyExists(String name)
    {
        super("The file "+ name +" already exists!");
        
        this.name = name;
    }
}