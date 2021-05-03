package ua.com.foxminded.data.dao.jdbc;

import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.data.dao.DaoFactory;
import ua.com.foxminded.data.domain.Course;
import ua.com.foxminded.data.dao.CourseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Class implements CourseDao interface methods and realize executing queries for course
 * @author Ind_us
 * @version 1.0
 * @see CourseDao
 * */
public class JdbcCourseDao implements CourseDao {
    /**
     * SQL query for creating course
     * */
    private static final String CREATE_COURSE = "select university.create_course(?,?)";
    /**
     * SQL query for deleting course
     * */
    private static final String DELETE_COURSE = "delete from university.courses where courses.course_id = ?";
    /**
     * SQL query for finding course by name
     * */
    private static final String FIND_BY_NAME = "select * from university.courses where courses.course_name = ?";
    /**
     * SQL query for finding all courses
     * */
    private static final String FIND_ALL = "select * from university.courses";
    /**
     * SQL query for adding student to course
     * */
    private static final String ADD_STUDENT_TO_COURSE = "select university.add_student_to_course(?,?)";
    /**
     * SQL query for removing student from course
     * */
    private static final String REMOVE_STUDENT_FROM_COURSE = "select university.remove_student_from_course(?,?)";
    /**
     * Instance of connection factory
     * */
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    @Override
    public Optional<List<Course>> getAll() throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Course> courseList;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(FIND_ALL);
            courseList = readDataFromResultSet(statement.executeQuery());
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
        return Optional.of(courseList);
    }
    @Override
    public void create(Course course) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(CREATE_COURSE);
            statement.setString(1,course.getCourseName());
            statement.setString(2,course.getCourseDescription());
            if(!statement.execute()){
                throw new DaoException("Course was not created");
            }
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
    }
    @Override
    public void delete(long course) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(DELETE_COURSE);
            statement.setLong(1,course);
            statement.execute();
        } catch (DaoException | SQLException e){
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                System.out.println("Cannot close connection");
                e.printStackTrace();
            }
        }
    }
    @Override
    public void removeStudent(long courseId, long studentId) throws DaoException {
        studentAndCourseConfigurations(courseId, studentId,REMOVE_STUDENT_FROM_COURSE);
    }
    @Override
    public void addStudent(long courseId, long studentId) throws DaoException {
        studentAndCourseConfigurations(courseId, studentId,ADD_STUDENT_TO_COURSE);
    }
    @Override
    public Optional<Course> getByName(String courseName) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        Course course = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(FIND_BY_NAME);
            statement.setString(1,courseName);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                course = new Course(
                        resultSet.getInt("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getString("course_description")
                );
            } else {
                System.out.println("No courses found by name = " + courseName);
            }
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
        if(course == null){
            return Optional.empty();
        }
        return Optional.of(course);
    }
    /**
     * Executes queries for adding and removing student from course
     * @param studentId id of student
     * @param courseId id of course
     * @param command query
     *                @throws DaoException exception from dao layer
     * */
    private void studentAndCourseConfigurations(long courseId, long studentId, String command) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(command);
            statement.setInt(1,(int)courseId);
            statement.setInt(2,(int)studentId);
            if(!statement.execute()){
                String action = getCommandAction(command);
                throw new DaoException("Student was not "+ action +"ed from course");
            }
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
    }
    /**
     * Splits message and point out operation of query
     * @param command name of query
     * @return command add/remove
     * */
    private String getCommandAction(String command){
        String[] splitByNamespace = command.split(" ");
        String[] splitBy_ = splitByNamespace[1].split("_");
        return splitBy_[0];
    }
    /**
     * Converts result set of query to list of courses
     * @throws DaoException exception from dao layer
     * @param resultSet result of select query
     * @return Course list
     * */
    private List<Course> readDataFromResultSet(ResultSet resultSet) throws DaoException {
        List<Course> courseList = new ArrayList<>();
        try {
            while(resultSet.next()){
                courseList.add(new Course(
                        resultSet.getInt("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getString("course_description")
                ));
            }
        } catch (SQLException e) {
            throw new DaoException("Reading from result set is failed",e);
        }
        return courseList;
    }
}
