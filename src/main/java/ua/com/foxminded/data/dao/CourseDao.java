package ua.com.foxminded.data.dao;

import ua.com.foxminded.data.domain.Course;

import java.util.Optional;

/**
 * @author Ind_us
 * @version 1.0
 * @see Dao
 * @see ua.com.foxminded.data.domain.Course
 * Interface provides special methods for course data
 * */
public interface CourseDao extends Dao<Course>{
    /**
     * @param courseId id of course
     * @param studentId id of student
     * @throws DaoException exception from dao layer
     *                  Removes`s student to course by id`s
     * */
    void removeStudent(long courseId, long studentId) throws DaoException;
    /**
     * @param courseId id of course
     * @param studentId id of student
     * @throws DaoException exception from dao layer
     *                  Add`s student to course by id`s
     * */
    void addStudent(long courseId, long studentId) throws DaoException;
    /**
     * @param courseName name of course
     * @return Optional of course
     * @throws DaoException exception from dao layer
     *                  Find`s course by specified name
     * */
    Optional<Course> getByName(String courseName) throws DaoException;
}
