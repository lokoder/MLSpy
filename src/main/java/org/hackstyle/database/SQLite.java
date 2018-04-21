package org.hackstyle.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLite {
    
    private static SQLite instance;
    private Connection connection;
    private String url = "jdbc:sqlite:/home/erick/database.sqlite";
    
    
    private SQLite() throws SQLException, ClassNotFoundException {

        Class.forName("org.sqlite.JDBC");            
        connection = DriverManager.getConnection(url);
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public static SQLite getInstance() throws SQLException, ClassNotFoundException {
        
        if (instance == null) {
            instance = new SQLite();
        } else if (instance.getConnection().isClosed()) {
            instance = new SQLite();
        }
        return instance;
    }
    
    
}
