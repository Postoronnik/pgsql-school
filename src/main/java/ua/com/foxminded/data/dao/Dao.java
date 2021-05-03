package ua.com.foxminded.data.dao;

import java.util.List;
import java.util.Optional;
/**
 * @author Ind_us
 * @version 1.0
 * Dao interface that provides simple CRUD operations
 * */
public interface Dao<T> {
    /**
     * Create some T object
     * @param t object that will communicate with database
     * @throws DaoException throw exception from dao layer
     * */
    void create(T t) throws DaoException;
    /**
     * Deletes some T object
     * @param id id of object
     * @throws DaoException throw exception from dao layer
     * */
    void delete(long id) throws DaoException;
    /**
     * Find`s all T objects
     * @return Optional list of T data
     * @throws DaoException exception from dao layer
     * */
    Optional<List<T>> getAll() throws DaoException;

}
