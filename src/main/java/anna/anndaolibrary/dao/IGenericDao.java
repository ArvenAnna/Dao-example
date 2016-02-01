/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anna.anndaolibrary.dao;

/**
 *
 * @author ArvenAnna
 */
import java.io.Serializable;
import java.util.List;

/**
 * Persistant interface
 *
 * @param <T> type of persistant object
 * @author Alex
 */
public interface IGenericDao<T extends Serializable> {

    /**
     * Create new record corresponding to object
     */
    public void add(T object);
    
    /**
     * Save new object State
     */
    public void update(T object);
    
    /**
     * Delete record about object
     */
    public void delete(T object);

    /**
     * Return list of objects corresponding to all records
     */
    public List<T> findAll();

    /**
     * Return new objects from record corresponding to another with same primary key
     */
    public T findByPK(T object);
    
    /**
     * Return connection to the pool
     */
    public void closeConnection();
    
} 

