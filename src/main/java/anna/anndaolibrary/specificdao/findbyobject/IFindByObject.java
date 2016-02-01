/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anna.anndaolibrary.specificdao.findbyobject;

import java.io.Serializable;
import java.util.List;

/**
 * Interface declarates finding operation by given parameter
 * @author Alex
 * @param <T> type of entity
 */
public interface IFindByObject<T extends Serializable>{
    
    /**
     * Finds entities of type T using parameter
     * @param object given parameter
     * @return list of entities
     */
    List<T> findByObject(Object object);
}

