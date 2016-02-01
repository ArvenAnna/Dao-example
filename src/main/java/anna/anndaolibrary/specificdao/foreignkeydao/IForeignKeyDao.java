/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anna.anndaolibrary.specificdao.foreignkeydao;

import java.io.Serializable;
import java.util.List;

/**
 * Dao interface for entities, which have foreign key to another entity 
 * @author Alex
 * @param <T> entity, which has foreign key of type E
 */
public interface IForeignKeyDao <T extends Serializable>{
    
    /**
     * Finds all entities, which have given foreign key
     * @param <E> - type of foreign key
     * @param foreignKey - given foreign key
     * @return list of found entities
     */
    public <E extends Serializable> List<T> findByForeignKey(E foreignKey);
    
    /**
     * Replace list of entities for given foreign key with new entity list,
     * if new entity from list already contains in db, it remains
     * @param <E> - type of foreign key
     * @param objects new entity list for replacement
     * @param foreignKey foreign key for wich replacement makes.
     */
    public <E extends Serializable> void replaceForeignKeyInformation(List<T> objects, E foreignKey);
}
