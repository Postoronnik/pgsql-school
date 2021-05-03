package ua.com.foxminded.data.dao;

import ua.com.foxminded.data.domain.Group;

import java.util.List;
import java.util.Optional;

/**
 * @author Ind_us
 * @version 1.0
 * @see Dao
 * Interface provides special methods for group data
 * */
public interface GroupDao extends Dao<Group>{
    /**
     * Provides query for finding all groups with less or equal student number
     * @throws DaoException exception from dao layer
     * @param studentsAmount compare number of students
     * @return Optional list of group
     * */
    Optional<List<Group>> findAllLessOrEquals(long studentsAmount) throws DaoException;
}
