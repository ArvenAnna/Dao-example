/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anna.anndaolibrary.specificdao.foreignkeydao;

import anna.anndaolibrary.dao.AbstractDao;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Skeletal implementation of IForeignKey interface
 * @author Alex
 * @param <T> entity, which has foreign key of type E
 */
public abstract class AbstractForeignKeyDao <T extends Serializable> extends AbstractDao<T> 
        implements IForeignKeyDao<T>{
    
    private static final Logger log = Logger.getLogger(AbstractForeignKeyDao.class);
    
    public AbstractForeignKeyDao(Connection connection) {
        super(connection);
    }
    
    /**
     * Adds to sql query foreign key condition like "where foreign_key_name = ?"
     * @param sql select query like "select all from table_name"
     * @return query for select using foreign key
     */
    protected abstract String addForeignKeyQuery(String sql);
    
    /**
     * Fills foreign key condition with concrete foreign key value
     * @param statement prepare statement for filling
     * @param object foreign key entity, from which extracting index
     */
    protected abstract <E extends Serializable> void prepareStatementForForeignKey(PreparedStatement statement, E object);
    
    @Override
    public <E extends Serializable> List<T> findByForeignKey(E object){
        List<T> list = new LinkedList<T>();  
        String sql = addForeignKeyQuery(getSelectQuery());
        log.info("sql findByForeignKey");
        log.info(sql);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForForeignKey(statement, object);
            ResultSet rs = statement.executeQuery(); 
            list = parseResultSet(rs);
            rs.close();
        } catch (SQLException e) {
            log.error("DB error in getbyForeignKey");
        } 
        return list; 
    }
    
    @Override
    public <E extends Serializable> void replaceForeignKeyInformation(List<T> objects, E foreignKey){
        
        List<T> newInfo = new ArrayList<T>(objects);
       
        List<T> oldInfo = findByForeignKey(foreignKey);
  
        Iterator newIterator = newInfo.iterator();

        log.info(oldInfo);
        log.info(newInfo);
        
        while (newIterator.hasNext()) {
            T newObject = (T)newIterator.next();
            log.info(newObject);
            Iterator oldIterator = oldInfo.iterator();
            while (oldIterator.hasNext()) {
                T oldObject = (T)oldIterator.next();
                log.info(oldObject);
                if(isInformationWas(newObject, oldObject)){
                    log.info("lock");
                    newIterator.remove();
                    oldIterator.remove();
                    break;
                }
            }
        }
        
        log.info(oldInfo);
        log.info(newInfo);
        for(T entity:oldInfo){
            delete(entity);
        }
        for(T entity:newInfo){
            add(entity);
        }       
    }
    
    /**
     * Checks equality of two objects using user's defined set of fields
     * This method used for replaceForeignKeyInformation() method
     * for checking if new object was in db
     * @param newObject - object, prepared to be inserted in db
     * @param oldObject - object, extracted from db
     * @return 
     */
    protected abstract boolean isInformationWas(T newObject, T oldObject);
}

