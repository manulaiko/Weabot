package com.manulaiko.tabitha.filesystem;

import com.manulaiko.tabitha.Console;
import com.manulaiko.tabitha.exceptions.NotFound;
import com.manulaiko.tabitha.exceptions.filesystem.DirectoryAlreadyExists;
import com.manulaiko.tabitha.exceptions.filesystem.DirectoryIsFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Directory object
 *
 * This object can be used for directory related operations like
 * listing directories, creating directories, deleting directories...
 *
 * The constructor accepts as parameter a string that is the name of the
 * directory to open.
 *
 * Example:
 *
 *     Directory directory;
 *     try {
 *         directory = Directory.make("/test");
 *     } catch(DirectoryAlreadyExists e) {
 *         directory = new Directory("/test");
 *     }
 *
 *     for(File f : directory.listFiles()) {
 *         Console.println("File: "+ f.name +"."+ f.extension);
 *     }
 *     for(Directory d : directory.listDirectories()) {
 *         Console.println("Subdirectories: "+ d.name);
 *     }
 *
 *
 * @author Manulaiko <manulaiko@gmail.com>
 */
public class Directory
{
    ///////////////////////////////////
    // Static methods and properties //
    ///////////////////////////////////
    /**
     * Makes a directory on the filesystem
     *
     * @param path Path to directory to create
     *
     * @return Created directory object
     *
     * @throws DirectoryAlreadyExists If path already exists
     * @throws DirectoryIsFile       If path is an existing file
     * @throws IOException
     */
    public static Directory make(String path) throws DirectoryAlreadyExists, DirectoryIsFile, IOException
    {
        path = Paths.get(path).toAbsolutePath().normalize().toString();

        java.io.File f = new java.io.File(path);

        if(f.exists()) {
            throw new DirectoryAlreadyExists(path);
        }

        if(f.isFile()) {
            throw new DirectoryIsFile(path);
        }

        if(f.mkdir()) {
            try {
                return new Directory(path);
            } catch(NotFound e) {
                Console.println(e.getMessage());
            }
        }

        return null;
    }

    /**
     * Checks whether a directory exists or not
     *
     * @param path Path to the directory
     *
     * @return Whether path exists and is a directory
     */
    public static boolean exists(String path)
    {
        path = Paths.get(path).toAbsolutePath().normalize().toString();

        java.io.File f = new java.io.File(path);

        if(f.exists() && f.isDirectory())  {
            return true;
        }

        return false;
    }

    ///////////////////////////////////////
    // Non static methods and properties //
    ///////////////////////////////////////

    /**
     * Directory path
     */
    public String path = "";

    /**
     * Directory name
     */
    public String name = "";

    /**
     * File object
     */
    private java.io.File _file;

    /**
     * Constructor
     *
     * @param path Path to file
     *
     * @throws NotFound If the directory doesn't exist
     */
    public Directory(String path) throws NotFound, DirectoryIsFile
    {
        path = Paths.get(path).toAbsolutePath().normalize().toString();

        java.io.File file = new java.io.File(path);

        if(!file.exists()) {
            throw new NotFound("directory", path);
        } else if(file.isFile()) {
            throw new DirectoryIsFile(path);
        }

        this.path  = path;
        this._file = file;


        int idx = path.replaceAll("\\\\", "/")
                      .lastIndexOf("/");
        if(idx >= 0) {
            this.name = path.substring(idx + 1);
        } else {
            this.name = path;
        }
    }

    /**
     * Returns parent directory object
     *
     * @return Parent directory
     */
    public Directory getParentDirectory() throws NotFound
    {
        int i = this.path.lastIndexOf(this.name);

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
     * Returns an array of files from the directory
     *
     * @return Array list containing directory's files
     */
    public ArrayList<File> listFiles()
    {
        ArrayList<File> files = new ArrayList<File>();

        for(java.io.File f : this._file.listFiles()) {
            if(f == null) {
                continue;
            }

            if(f.isDirectory()) {
                continue;
            }

            try {
                files.add(new File(f.getAbsolutePath()));
            } catch(Exception e) {
                Console.println(e.getMessage());
            }
        }

        return files;
    }

    /**
     * Returns an array of subdirectories from the directory
     *
     * @return Array list containing directory's subdirectories
     */
    public ArrayList<Directory> listDirectories()
    {
        ArrayList<Directory> directories = new ArrayList<Directory>();

        for(java.io.File f : this._file.listFiles()) {
            if(f == null) {
                continue;
            }

            if(f.isFile()) {
                continue;
            }

            if(f.getName().equals(".") || f.getName().equals("..")) {
                continue;
            }

            try {
                directories.add(new Directory(f.getAbsolutePath()));
            } catch(Exception e) {
                Console.println(e.getMessage());
            }
        }

        return directories;
    }

    /**
     * Checks if 'file' exists in this directory
     *
     * @param file     File to check
     * @param isRegexp Whether 'file' is a valid regular expression or not
     *
     * @return Whether 'file' exists in this directory
     */
    public boolean fileExists(String file, boolean isRegexp)
    {
        for(File f : this.listFiles()) {
            if(isRegexp) {
                Pattern p = Pattern.compile(file);
                Matcher m = p.matcher(f.name);

                if(m.find()) {
                    return true;
                }
            } else {
                if(file.equals(f.name)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if 'file' exists in this directory
     *
     * @param file File to check
     *
     * @return Whether 'file' exists in this directory
     */
    public boolean fileExists(String file)
    {
        return this.fileExists(file, false);
    }

    /**
     * Returns given file
     *
     * @param file File name
     *
     * @trhows NotFound If file does not exist
     */
    public File getFile(String file) throws NotFound
    {
        for(File f : this.listFiles()) {
            if(file.equals(f.name)) {
                return f;
            }
        }

        throw new NotFound("file", file);
    }

    /**
     * Returns a file based on given regular expression
     *
     * @param regexp File name regular expression
     *
     * @trhows NotFound If no files matches 'regexp'
     */
    public File getFileByRegexp(String regexp) throws NotFound
    {
        for(File f : this.listFiles()) {
            Pattern p = Pattern.compile(regexp);
            Matcher m = p.matcher(f.name);

            if(m.find()) {
                return f;
            }
        }

        throw new NotFound("file", regexp);
    }

    /**
     * Returns given directory
     *
     * @param directory Directory name
     *
     * @trhows NotFound If directory does not exist
     */
    public Directory getDirectory(String directory) throws NotFound
    {
        for(Directory d : this.listDirectories()) {
            if(directory.equals(d.name)) {
                return d;
            }
        }

        throw new NotFound("directory", directory);
    }

    /**
     * Returns a directory based on given regular expression
     *
     * @param regexp Directory name regular expression
     *
     * @trhows NotFound If no directories matches 'regexp'
     */
    public Directory getDirectoryByRegexp(String regexp) throws NotFound
    {
        for(Directory d : this.listDirectories()) {
            Pattern p = Pattern.compile(regexp);
            Matcher m = p.matcher(d.name);

            if(m.find()) {
                return d;
            }
        }

        throw new NotFound("directory", regexp);
    }

    /**
     * Checks if 'directory' exists in this directory
     *
     * @param directory Directory to check
     * @param isRegexp  Whether 'directory' is a valid regular expression or not
     *
     * @return Whether 'directory' exists in this directory
     */
    public boolean directoryExists(String directory, boolean isRegexp)
    {
        for(Directory d : this.listDirectories()) {
            if(isRegexp) {
                Pattern p = Pattern.compile(directory);
                Matcher m = p.matcher(d.name);

                if(m.find()) {
                    return true;
                }
            } else {
                if(directory.equals(d.name)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if 'directory' exists in this directory
     *
     * @param directory Directory to check
     *
     * @return Whether 'directory' exists in this directory
     */
    public boolean directoryExists(String directory)
    {
        return this.directoryExists(directory, false);
    }
}
