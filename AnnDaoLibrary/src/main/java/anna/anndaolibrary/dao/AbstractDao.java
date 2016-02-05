/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anna.anndaolibrary.dao;

/**
 *
 * @author Alex
 */
import anna.anndaolibrary.dbconnection.ConnectionPool;
import java.io.Serializable; 
import java.sql.Connection;
import java.sql.SQLException; 
import java.util.List; 
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 * Abstract class with basic realization CRUD JDBC operations
 * @param <T> type of persistant object 
 * @author Alex
 */
public abstract class AbstractDao<T extends Serializable> implements IGenericDao<T>{
    
    protected Connection connection;
    private static Logger log = Logger.getLogger(AbstractDao.class);
    
    /** 
     * Make SQL query for select all records in table 
     * SELECT * FROM [Table] 
     */
    protected abstract String getSelectQuery(); 
    
    /** 
     * Make SQL query for update record in table 
     * UPDATE [Table] SET
     */
    protected abstract String getUpdateQuery();
    
    /** 
     * Make SQL query for delete record from table 
     * DELETE FROM [Table]
     */
    protected abstract String getDeleteQuery();
    
    /** 
     * Make SQL query for insert record to the table 
     * INSERT INTO [Table]
     */
    protected abstract String getInsertQuery();
    
    /** 
     * Continiue SQL query by setting primary key 
     * WHERE [PK] =
     * @param sql main SQL query
     */
    protected abstract String addPrimaryKeyQuery(String sql);
    
    /** 
     * Fill prepare statement for updating record
     * @param statement prepare statement for filling
     * @param object will be future updated record
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object);
    
    /** 
     * Fill prepare statement for delete record
     * @param statement prepare statement for filling
     * @param object will be future deleted record
     */
    protected abstract void prepareStatementForDelete(PreparedStatement statement, T object);
    
    /** 
     * Fill prepare statement for insert record
     * @param statement prepare statement for filling
     * @param object will be inserted record
     */
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object);
    
    /** 
     * Fill prepare statement for primary key
     * @param statement prepare statement for filling
     * @param object wich contains primary key
     */
    protected abstract void prepareStatementForPrimaryKey(PreparedStatement statement, T object);

     /** 
      * Parse ResultSet object rs 
      * @param rs
      * @return list of objects corresponding to inner of ResultSet
      */ 
    protected abstract List<T> parseResultSet(ResultSet rs); 

    /**
     * Find object by primary key
     * @param object contained target primary key
     * @return object got from database by primary key
     */
    @Override
    public T findByPK(T object){ 
        List<T> list = null;  
        String sql = addPrimaryKeyQuery(getSelectQuery());
        log.info("sql findByPK");
        log.info(sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForPrimaryKey(statement, object);
            ResultSet rs = statement.executeQuery(); 
            list = parseResultSet(rs);
            rs.close();
        } catch (SQLException e) {
            log.error("DB error in getbyPK");
        } 
        if (list == null || list.size() == 0) { 
            return null; 
        } 
        return list.iterator().next(); 
    } 
    
    @Override 
    public List<T> findAll(){ 
        List<T> list = new LinkedList<T>(); 
        String sql = getSelectQuery(); 
        log.info("sql findAll");
        log.info(sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) { 
            ResultSet rs = statement.executeQuery();    
            list = parseResultSet(rs); 
            rs.close();
        } catch (SQLException e) { 
            log.error("DB error in readAll()"); 
        } 
        return list; 
    } 
    
    /**
     * Return connection to the pool
     */
    public void closeConnection(){ 
        ConnectionPool.returnConnection(connection);
    }
    
    @Override 
    public void update(T object) { 
        String sql = getUpdateQuery();
        log.info("sql update");
        log.info(sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object); 
            int count = statement.executeUpdate(); 
            if (count != 1) { 
                log.error("update" + object + "failed");
            } 
        } catch (SQLException e) {
            log.error("DB error in update");
        } 
    } 

    @Override 
    public void delete(T object){ 
        String sql = getDeleteQuery(); 
        log.info("sql delete");
        log.info(sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {  
            prepareStatementForDelete(statement, object); 
            int count = statement.executeUpdate(); 
            if (count != 1) { 
                log.error("delete" + object + "failed");
            }  
        } catch (SQLException e) { 
            log.error("DB error in delete"); 
        } 
    }
    
    @Override 
    public void add(T object){  
        String sql = getInsertQuery(); 
        log.info("sql insert");
        log.info(sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) { 
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) { 
                log.error("insert" + object + "failed");
            } 
        } catch (SQLException e) { 
            log.error("DB error in insert");
        }
    }

    public AbstractDao(Connection connection) { 
        this.connection = connection; 
    } 
}
