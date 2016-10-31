package com.manulaiko.tabitha;

import java.io.IOException;

import com.manulaiko.tabitha.exceptions.NotFound;
import com.manulaiko.tabitha.exceptions.filesystem.FileIsDirectory;
import com.manulaiko.tabitha.exceptions.configuration.UnsupportedType;
import com.manulaiko.tabitha.filesystem.File;
import com.manulaiko.tabitha.configuration.IConfiguration;
import com.manulaiko.tabitha.configuration.IniConfiguration;

/**
 * Configuration class
 *
 * This class offers helpers to read/write configuration files.
 *
 * Depending on the type of configuration file you'll need to call
 * a different load method:
 *  * .ini files: `loadIni(path)` returns {@see com.manulaiko.tabitha.configuration.IniConfiguration}
 *  * .properties files: `loadProperties(path)` returns {@see com.manulaiko.tabitha.configuration.PropertiesConfiguration}
 *  * .xml files: `loadXml(path)` returns {@see com.manulaiko.tabitha.configuration.XMLConfiguration}
 *  * .json files: `loadJson(path)` returns {@see com.manulaiko.tabitha.configuration.JSONConfiguration}
 *
 * Alternatively you can use the method {@see com.manulaiko.tabitha.Configuration#load} which accepts as
 * parameter the name of the configuration file, the returning object is an instance of {@see com.manulaiko.tabitha.configuration.IConfiguration}
 * and is instanced depending on file extension.
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Configuration
{
    /**
     * Loads a configuration file
     *
     * @param path Path to the file
     *
     * @return Configuration object instance
     *
     * @throws NotFound        If configuration file does not exists
     * @throws FileIsDirectory If `path` is a directory
     * @throws UnsupportedType If the configuration file has an unsupported extension
     * @throws IOException     If couldn't read configuration file
     */
    public static IConfiguration load(String path) throws NotFound, FileIsDirectory, UnsupportedType, IOException
    {
        File f = new File(path);

        IConfiguration cfg;

        if(f.extension.equals("ini")) {
            cfg = new IniConfiguration();
        /*} else if(f.extension.equals("properties")) {
            cfg = new PropertiesConfiguration();
        } else if(f.extension.equals("xml")) {
            cfg = new XMLConfiguration();
        } else if(f.extension.equals("json")) {
            cfg = new JSONConfiguration();*/
        } else {
            throw new UnsupportedType(path);
        }

        cfg.parse(path);

        return cfg;
    }

    /**
     * Loads a ini configuration file
     *
     * @return Configuration object instance
     *
     * @throws NotFound        If configuration file does not exists
     * @throws FileIsDirectory If `path` is a directory
     * @throws UnsupportedType If the configuration file has an unsupported extension
     * @throws IOException     If couldn't read configuration file
     */
    public static IniConfiguration loadIni(String path) throws NotFound, FileIsDirectory, UnsupportedType, IOException
    {
        return (IniConfiguration) Configuration.load(path);
    }
}
