package com.manulaiko.tabitha.configuration;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import com.manulaiko.tabitha.exceptions.NotFound;
import com.manulaiko.tabitha.exceptions.filesystem.FileIsDirectory;
import com.manulaiko.tabitha.filesystem.File;

/**
 * Ini Configuration class
 *
 * This class is used to parse `*.ini` configuration files
 *
 * For accessing section members use a dot. Example:
 * Given the configuration file:
 *
 *     [core]
 *     verbose=true
 *
 *     [database]
 *     verbose=false
 *
 * The code to access the `verbose` members of each section would look like this:
 *
 *     try {
 *         IniConfiguration cfg = Configuration.loadIni("config.ini");
 *
 *         if(cfg.getBoolean("core.verbose")) {
 *             Console.println("true");
 *         } else {
 *             Console.println("false");
 *         }
 *
 *         if(cfg.getBoolean("database.verbose")) {
 *             Console.println("true");
 *         } else {
 *             Console.println("false");
 *         }
 *     } catch(Exception e) {
 *
 *     }
 *
 * This outputs the following:
 *
 *     true
 *     false
 *
 * @author Manulaiko <manulaiko@gmail.com>
 *
 * @see http://stackoverflow.com/a/15638381
 */
public class PropertiesConfiguration implements IConfiguration
{
    /**
     * Path to the configuration file
     */
    public String path = "";

    /**
     * File handler
     */
    private File _handler;

    /**
     * Patter used to identify members
     */
    private Properties _props;

    /**
     * Entries from the configuration file
     */
    private Map<String, Map<String, String>> _entries = new HashMap<String, java.util.Map<String, String>>();

    /**
     * Parses the configuration file
     *
     * @param path Path to the configuration file
     *
     * @throws NotFound        If configuration file does not exists
     * @throws FileIsDirectory If `path` is a directory
     * @throws IOException     If couldn't read configuration file
     */
    public void parse(String path) throws NotFound, FileIsDirectory, IOException
    {
        this._handler = new File(path);
        this.path     = this._handler.path;
        this._props   = new Properties();

        this._props.load(new BufferedReader(new FileReader(this.path)));
    }

    /**
     * Returns given configuration parameter as a short
     *
     * @param name Configuration parameter
     *
     * @return Given configuration parameter value
     */
    public short getShort(String name)
    {
        return Short.parseShort(this.getString(name));
    }

    /**
     * Returns given configuration parameter as an int
     *
     * @param name Configuration parameter
     *
     * @return Given configuration parameter value
     */
    public int getInt(String name)
    {
        return Integer.parseInt(this.getString(name));
    }

    /**
     * Returns given configuration parameter as a long
     *
     * @param name Configuration parameter
     *
     * @return Given configuration parameter value
     */
    public long getLong(String name)
    {
        return Long.parseLong(this.getString(name));
    }

    /**
     * Returns given configuration parameter as a string
     *
     * @param name Configuration parameter
     *
     * @return Given configuration parameter value
     */
    public String getString(String name)
    {
        if(this._props.getProperty(name) != null) {
            return this._props.getProperty(name);
        }

        return "";
    }

    /**
     * Returns given configuration parameter as a boolean
     *
     * @param name Configuration parameter
     *
     * @return Given configuration parameter value
     */
    public boolean getBoolean(String name)
    {
        return Boolean.getBoolean(this.getString(name));
    }

    /**
     * Returns given configuration parameter as a float
     *
     * @param name Configuration parameter
     *
     * @return Given configuration parameter value
     */
    public float getFloat(String name)
    {
        return Float.parseFloat(this.getString(name));
    }

    /**
     * Returns given configuration parameter as a double
     *
     * @param name Configuration parameter
     *
     * @return Given configuration parameter value
     */
    public double getDouble(String name)
    {
        return Double.parseDouble(this.getString(name));
    }

    /**
     * Returns given configuration parameter as a byte
     *
     * @param name Configuration parameter
     *
     * @return Given configuration parameter value
     */
    public byte getByte(String name)
    {
        return Byte.parseByte(this.getString(name));
    }
}
