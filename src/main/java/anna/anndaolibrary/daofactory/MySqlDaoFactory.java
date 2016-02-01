/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anna.anndaolibrary.daofactory;

import anna.anndaolibrary.dbconnection.ConnectionPool;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * Factory for dao's
 *
 * @author ArvenAnna
 */
public class MySqlDaoFactory implements IDaoFactory {

    private static MySqlDaoFactory uniqueInstance;
    private static Logger log = Logger.getLogger(MySqlDaoFactory.class);
    private static Set<Class> daoSet;
    private static ResourceBundle rb;

    private MySqlDaoFactory(Set<Class> daoClasses, ResourceBundle rb) {
        daoSet = daoClasses;
        this.rb = rb;
    }

    public static MySqlDaoFactory instance(Set<Class> daoClasses, ResourceBundle rb) {
        if (uniqueInstance == null) {
            uniqueInstance = new MySqlDaoFactory(daoClasses, rb);
        }
        return uniqueInstance;
    }

    private Connection makeConnection() {
        ConnectionPool.setConnectionParameters(rb);
        return ConnectionPool.getConnection();
    }

    public <T> T getDao(Class daoClass) {
        log.info("we are in getDa0");
        T dao = null;
        if (daoClass != null) {
            if (daoSet.contains(daoClass)) {
  
                try {
                    dao = (T) daoClass.getConstructor(Connection.class).newInstance(makeConnection());
                } catch (InstantiationException ex) {
                    java.util.logging.Logger.getLogger(MySqlDaoFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    java.util.logging.Logger.getLogger(MySqlDaoFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    java.util.logging.Logger.getLogger(MySqlDaoFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    java.util.logging.Logger.getLogger(MySqlDaoFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchMethodException ex) {
                    java.util.logging.Logger.getLogger(MySqlDaoFactory.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    java.util.logging.Logger.getLogger(MySqlDaoFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
 
            }
        } else {
            log.fatal("there is no such class at all");
        }
        log.info(dao.getClass().getName());
        return dao;
    }
}
