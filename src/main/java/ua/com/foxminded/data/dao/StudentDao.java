package ua.com.foxminded.data.dao;

import ua.com.foxminded.data.domain.Course;
import ua.com.foxminded.data.domain.Student;

import java.util.List;
import java.util.Optional;

/**
 * @author Ind_us
 * @version 1.0
 * Interface provides special methods for student
 * @see Dao
 * */
public interface StudentDao extends Dao<Student>{
    /**
     * Executes query of selecting all students related to course
     * @throws DaoException exception from dao layer
     * @param course course data
     * @return Optional list of students
     * */
    Optional<List<Student>> getAllByCourseName(Course course) throws DaoException;
}
