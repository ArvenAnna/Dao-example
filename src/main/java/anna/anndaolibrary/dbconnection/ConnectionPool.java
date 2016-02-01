/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anna.anndaolibrary.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 *
 * @author Alex
 */
public class ConnectionPool {
    
    private static Logger log = Logger.getLogger(ConnectionPool.class);
    //private static ResourceBundle rb = ResourceBundle.getBundle("com/epam/hotel/resources/dbconnection");
    private static String user;                                 
    private static String password;                        
    private static String url;
    private static String driver;               
    private static int maxSize;
    private static List<Connection> pool = null;
    private static int available = 0;
    private static int allCreated = 0;
    private static ConnectionPool uniqueInstance;
    
    private ConnectionPool() {
    }

    private static Connection riseConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
            log.error("driver JDBC not found");
        } catch (SQLException ex) {
            log.error("SQL exception - can't make connection with given parameters");
            return null;
        }
        return connection;
    }
    
    public static ConnectionPool instance(){
        if (uniqueInstance == null) {
            uniqueInstance = new ConnectionPool();
        }
        return uniqueInstance;
    }
    
    public static void setConnectionParameters(ResourceBundle rb){
        user = rb.getString("user");
        password = rb.getString("password");
        url = rb.getString("url");
        driver = rb.getString("driver");
        maxSize = Integer.parseInt(rb.getString("maxSize"));   
    }

    public static Connection getConnection() {
        if (pool == null) {
            pool = new ArrayList<Connection>();
            pool.add(riseConnection());
            available++;
            log.info("First connection was created in the pool");
            allCreated++;
        }
        if (pool.size() > 0) {
            available--;
            log.info("Connection was given from the pool, available = " + available);
        } else if (allCreated < maxSize) {
            pool.add(riseConnection());
            allCreated++;
            log.info("connection was created in the pool, available = " + (available+1));
            log.info("Created total" + allCreated);
            log.info("Connection was given from the pool, available = " + available);
            
        } else {
            log.info("There are no available connections in the pool, please return old");
            return null;
        }
        return pool.remove(available);
    }

    public static void returnConnection(Connection c) {
        pool.add(c);
        available++;
        log.info("Connection was returned to the pool, available = " + available);
        
    }
}
