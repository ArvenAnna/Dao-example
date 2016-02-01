/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anna.anndaolibrary.specificdao.findbyobject;

import anna.anndaolibrary.dbconnection.ConnectionPool;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Skeletal implementation of IFindByObject interface
 *
 * @author Alex
 * @param <T> type of entity
 */
public abstract class AbstractFindByObject<T extends Serializable> implements IFindByObject<T> {

    private Connection connection;
    private static final Logger log = Logger.getLogger(AbstractFindByObject.class);
    
    public AbstractFindByObject(Connection connection) { 
        this.connection = connection; 
    } 

    protected abstract void prepareStatementForSelectByObject(PreparedStatement statement, Object object);
    
    protected abstract String getSelectObjectQuery();
    
    protected abstract List<T> parseObjectResultSet(ResultSet rs);

    @Override
    public List<T> findByObject(Object object) {
        List<T> list = new LinkedList<T>();
        String sql = getSelectObjectQuery();
        log.info("sql findByObject");
        log.info(sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForSelectByObject(statement, object);
            try (ResultSet rs = statement.executeQuery()) {
                list = parseObjectResultSet(rs);
            }
        } catch (SQLException e) {
            log.error("DB error in findByObject");
        }
        return list;
    }

    public void closeConnection() {
        ConnectionPool.returnConnection(connection);
    }
}

