/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anna.anndaolibrary.daofactory;

/**
 * Interface for making dao Factory
 * @author ArvenAnna
 */
public interface IDaoFactory {

    /**
     * Return concrete dao for given entity's dao class
     */
    public <T> T getDao(Class daoClass);
}
