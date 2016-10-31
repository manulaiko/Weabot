package com.manulaiko.tabitha.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.manulaiko.tabitha.exceptions.database.ConnectionFailed;

/**
 * Connection class
 * 
 * This class establish a connection to a MySQL server
 * and provides methods for executing queries.
 * 
 * The constructor accepts as parameters:
 *  * A string being the hostname/ip of the MySQL server
 *  * A short integer being the port on which the server is running
 *  * A string being the username
 *  * A string being the password
 *  * A string being the name of the database
 * 
 * Example:
 * 
 *     try {
 *         Connection connection = new Connection("localhost", (short)3306, "root", "", "tabitha");
 *     } catch(ConnectionFailed e) {
 *         Console.println("Couldn't connect to database server!");
 *     }
 * 
 * @author Manuliako <manulaiko@gmail.com>
 */
public class Connection
{
    /**
     * Hostname
     */
    private String _host;
    
    /**
     * Port
     */
    private short _port;
    
    /**
     * Username
     */
    private String _username;
    
    /**
     * Password
     */
    private String _password;
    
    /**
     * Database
     */
    private String _database;
    
    /**
     * Connection object
     */
    private java.sql.Connection _connection;
    
    /**
     * Constructor
     * 
     * @param host     Server host
     * @param port     Server port
     * @param username Authentication user name
     * @param password Authentication password
     * @param database Database name
     * 
     * @throws com.manulaiko.tabitha.exceptions.database.ConnectionFailed If couldn't connect to the server
     */
    public Connection(String host, short port, String username, String password, String database) throws ConnectionFailed
    {
        this._host     = host;
        this._port     = port;
        this._username = username;
        this._password = password;
        this._database = database;
        
        try {
            this._connection = DriverManager.getConnection("jdbc:mysql://"+ this._host +":"+ this._port +"/"+ this._database, this._username, this._password);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new ConnectionFailed();
        }
    }
    
    /**
     * Returns a statement object
     * 
     * @throws SQLException 
     */
    public ResultSet query(String query) throws SQLException
    {
        Statement st = this._connection.createStatement();
        
        return st.executeQuery(query);
    }
    
    /**
     * Executes an UPDATE query
     * 
     * @throws SQLException 
     */
    public int update(String query) throws SQLException
    {
        Statement st = this._connection.createStatement();
        
        return st.executeUpdate(query);
    }
    
    /**
     * Returns a prepared statement object
     * 
     * @throws SQLException 
     */
    public PreparedStatement prepare(String query) throws SQLException
    {
        PreparedStatement st = this._connection.prepareStatement(query);
        
        return st;
    }
}
