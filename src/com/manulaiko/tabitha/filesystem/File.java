package com.manulaiko.tabitha.filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Vector;
import java.util.function.Consumer;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.exceptions.NotFound;
import com.manulaiko.tabitha.exceptions.filesystem.FileAlreadyExists;
import com.manulaiko.tabitha.exceptions.filesystem.FileIsDirectory;
import com.manulaiko.tabitha.exceptions.filesystem.DirectoryIsFile;

/**
 * File object
 *
 * This object can be used to read/write easily to a file.
 *
 * The constructor accepts as parameter a string that is the name of the
 * file to open.
 *
 * Example:
 *
 *     File file;
 *     try {
 *         file = File.make("/test");
 *     } catch(FileAlreadyExists e) {
 *         file = new File("/test");
 *     }
 *     
 *     Console.println(file.getContent());
 *     file.append("Hello ");
 *     file.appendLine("world!");
 *     Console.println(file.getContent());
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class File
{
    ///////////////////////////////////
    // Static methods and properties //
    ///////////////////////////////////
    /**
     * Makes a file on the filesystem
     *
     * @param path Path to file to create
     *
     * @return Created file object
     *
     * @throws com.manulaiko.tabitha.exceptions.filesystem.FileAlreadyExists If path already exists
     * @throws com.manulaiko.tabitha.exceptions.filesystem.FileIsDirectory  If path is an existing directory
     * @throws IOException
     */
    public static File make(String path) throws FileAlreadyExists, FileIsDirectory, IOException
    {
        path = Paths.get(path).toAbsolutePath().normalize().toString();

        java.io.File f = new java.io.File(path);
        
        if(f.exists()) {
            throw new FileAlreadyExists(path);
        }

        if(f.isDirectory()) {
            throw new FileIsDirectory(path);
        }
        
        if(f.createNewFile()) {
            try {
                return new File(path);
            } catch(NotFound e) {
                Console.println(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Checks wether a file exists or not
     *
     * @param path Path to the file
     *
     * @return Whether path exists and is a file
     */
    public static boolean exists(String path)
    {
        path = Paths.get(path).toAbsolutePath().normalize().toString();

        java.io.File f = new java.io.File(path);

        if(f.exists() && f.isFile())  {
            return true;
        }

        return false;
    }

    ///////////////////////////////////////
    // Non static methods and properties //
    ///////////////////////////////////////

    /**
     * File path
     */
    public String path = "";

    /**
     * File name
     */
    public String name = "";

    /**
     * File extension
     */
    public String extension = "";

    /**
     * File lines
     */
    public Vector<String> lines = null;

    /**
     * Reader object
     */
    private BufferedReader _reader = null;

    /**
     * Writer object
     */
    private BufferedWriter _writer = null;

    /**
     * File object
     */
    private java.io.File _file = null;

    /**
     * Constructor
     *
     * @param path Path to file
     * @throws IOException 
     */
    public File(String path) throws FileIsDirectory, NotFound, IOException
    {
        path = Paths.get(path).toAbsolutePath().normalize().toString();

        java.io.File f = new java.io.File(path);

        if(!f.exists()) {
            throw new NotFound("file", path);
        } else if(f.isDirectory()) {
            throw new FileIsDirectory(path);
        }

        int    idx      = path.replaceAll("\\\\", "/")
                              .lastIndexOf("/");
        String fullName = "";

        if(idx >= 0) {
            fullName = path.substring(idx + 1);
        } else {
            fullName = path;
        }

        int i = fullName.lastIndexOf('.');
        int p = Math.max(fullName.lastIndexOf('/'), fullName.lastIndexOf('\\'));

        if(i > p) {
            this.extension = fullName.substring(i + 1);
            this.name = fullName.substring(0, i);
        } else {
            this.name = fullName;
        }

        this.path  = path;
        this._file = f;
    }

    /**
     * Returns file's directory object
     *
     * @return Directory on which file is located
     */
    public Directory getDirectory() throws NotFound
    {
        int i = this.path.lastIndexOf(this.name +"."+ this.extension);

        if(i <= 0) {
            try {
                return new Directory(Paths.get(".").toAbsolutePath().normalize().toString());
            } catch(DirectoryIsFile e) {
                Console.println(e.getMessage());
            }
        }

        String dir = this.path.substring(i);

        try {
            return new Directory(dir);
        } catch(DirectoryIsFile e) {
            Console.println(e.getMessage());
        }

        return null;
    }

    /**
     * Returns a single line of the file
     *
     * @param line Line number
     *
     * @return Line content
     * @throws IOException 
     */
    public String getLine(int line) throws IOException
    {
        String l = "";
        int    i = 0;
        
        while((l = this._reader.readLine()) != null) {
            if(i == line) {
                break;
            } else {
                i++;
            }
        }
        
        return l;
    }
    
    /**
     * Returns all lines of the file
     * 
     * @return File content
     * @throws IOException 
     */
    public ArrayList<String> getAllLines() throws IOException
    {
        ArrayList<String> lines = new ArrayList();

        Files.lines(Paths.get(this._file.getAbsolutePath()))
             .forEach(s-> {
                 lines.add(s);
             });
        
        return lines;
    }
}
