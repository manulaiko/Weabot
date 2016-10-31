package com.manulaiko.tabitha;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.manulaiko.tabitha.utils.Tools;

/**
 * Console class
 * 
 * This class is used to interact with the console.
 * It has methods for printing strings and reading input.
 * 
 * @author Manulaiko
 */
public class Console
{    
    /* Start constant definition */
    public static final String LINE_EQ         = "======================================================";
    public static final String LINE_MINUS      = "------------------------------------------------------";
    public static final String ANSI_RESET      = "\u001B[0m";
    public static final String ANSI_BLACK      = "\u001B[30m";
    public static final String ANSI_RED        = "\u001B[31m";
    public static final String ANSI_GREEN      = "\u001B[32m";
    public static final String ANSI_YELLOW     = "\u001B[33m";
    public static final String ANSI_BLUE       = "\u001B[34m";
    public static final String ANSI_PURPLE     = "\u001B[35m";
    public static final String ANSI_CYAN       = "\u001B[36m";
    public static final String ANSI_WHITE      = "\u001B[37m";
    public static final String ANSI_BOLD       = "\u001B[1m";
    public static final String ANSI_BOLD_RESET = "\u001B[21m";
    public static final String ANSI_BLINK      = "\u001B[5m";
    /* End constant definition */
    
    /**
     * Short method for {@see com.manulaiko.tabitha.Console.print}
     * 
     * @param strings String(s) to print
     */
    public static void print(String... strings)
    {
        String str = "";
        for(String s : strings) {
            str += s;
        }
        
        print(true, str);
    }
    
    /**
     * Short method for {@see com.manulaiko.tabitha.Console.println}
     * 
     * @param strings String(s) to print
     */
    public static void println(String... strings)
    {
        String str = "";
        for(String s : strings) {
            str += s;
        }
        
        println(true, str);
    }
    
    /**
     * Prints a(some) string(s) to the console.
     * 
     * Usage is fairly simple:
     *     
     *     Console.print("Hello world!");
     * 
     * You can also concatenate various string by sending them
     * as different parameters:
     * 
     *     Console.print("Hello", " ", "world!");
     * 
     * @param strings String(s) to print
     */
    public static void print(boolean showInfo, String... strings)
    {
        String str = "";
        
        for(String s : strings) {
            str += s;
        }
        
        _print(str, showInfo);
    }
    
    /**
     * Prints a(some) string(s) to the console and sets cursor in new line.
     * 
     * Usage is fairly simple:
     *     
     *     Console.print("Hello world!");
     * 
     * You can also concatenate various string by sending them
     * as different parameters:
     * 
     *     Console.print("Hello", " ", "world!");
     * 
     * @param strings String(s) to print
     */
    public static void println(boolean showInfo, String... strings)
    {
        String str = "";
        
        for(String s : strings) {
            str += s;
        }
        
        _print(str+"\n", showInfo);
    }
    
    /**
     * Actually prints the string to the console
     * 
     * @param string   String to print
     * @param showInfo Whether to show or not calling method info
     */
    private static void _print(String string, boolean showInfo)
    {
        String caller = "";

        if(showInfo) {
            //Get caller method
            StackTraceElement[] st = Thread.currentThread()
                                           .getStackTrace();
            for(StackTraceElement aSt : st) {
                String cls = aSt.getClassName();

                if(!cls.equals("com.manulaiko.tabitha.Console") && !cls.equals("java.lang.Thread")) {
                    caller = aSt.getClassName() + "::" + aSt.getMethodName() + " (" + aSt.getLineNumber() + ")";

                    break;
                }
            }

            //Build date
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            System.out.print(ANSI_BLACK+"["+dateFormat.format(date)+"]"+ANSI_YELLOW+" (" + caller + ")"+ANSI_BLACK+": "+ANSI_RESET);
        }

        System.out.print(string);
    }
    
    /**
     * Returns a string from input
     * 
     * @return Input result
     */
    public static String readLine()
    {
        return Tools.in.nextLine();
    }
    
    /**
     * Returns an integer from input
     * 
     * @return Input result
     */
    public static int readInt()
    {
        return Tools.in.nextInt();
    }
}
