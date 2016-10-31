package com.manulaiko.tabitha.exceptions.configuration;

/**
 * Unsupported type exception
 *
 * Occurs when you try to parse a configuration file which has
 * an invalid/unsupported extension.
 *
 * Example:
 *     try {
 *         IConfiguration cfg = Configuration.load("configuration.asodnaornvoenr");
 *     } catch(UnsupportedType e) {
 *         Console.println("The configuration file "+ e.path +" has an unsupported extension!");
 *     }
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class UnsupportedType extends Exception
{
    /**
     * Configuration file path
     */
    public String path = "";

    /**
     * Constructor
     *
     * @param path Path to the configuration file
     */
    public UnsupportedType(String path)
    {
        super("The configuration file "+ path +" has an unsupported extension!");

        this.path = path;
    }
}
