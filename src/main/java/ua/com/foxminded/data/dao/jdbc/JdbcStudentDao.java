package ua.com.foxminded.data.dao.jdbc;

import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.data.dao.DaoFactory;
import ua.com.foxminded.data.domain.Course;
import ua.com.foxminded.data.domain.Student;
import ua.com.foxminded.data.dao.StudentDao;
import ua.com.foxminded.data.dao.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Class implements StudentDao interface methods and realize executing queries for student
 * @author Ind_us
 * @version 1.0
 * @see StudentDao
 * @see Dao
 * */
public class JdbcStudentDao implements StudentDao {
    /**
     * SQL query for creating student with group dependency
     * */
    private static final String CREATE_STUDENT_WITH_GROUP = "select university.create_student(?,?,?)";
    /**
     * SQL query for creating student without group dependency
     * */
    private static final String CREATE_STUDENT_WITHOUT_GROUP = "select university.create_student(?,?)";
    /**
     * SQL query for deleting student
     * */
    private static final String DELETE_STUDENT = "delete from university.students where student_id = ?";
    /**
     * SQL query for finding students by course name
     * */
    private static final String FIND_BY_COURSE_NAME = "" +
            "select students.student_id, group_id, first_name, last_name " +
            "from university.students, university.courses, university.courses_students " +
            "where course_name = ? " +
            "and courses.course_id = courses_students.course_id " +
            "and students.student_id = courses_students.student_id";
    /**
     * SQL query for finding all students
     * */
    private static final String FIND_ALL = "select * from university.students";
    /**
     * Instance of connection factory
     * */
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    @Override
    public Optional<List<Student>> getAll() throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Student> studentList = new ArrayList<>();
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(FIND_ALL);
            studentList = readDataFromResultSet(statement.executeQuery());
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
                throw new DaoException("Cannot close connection. Cause it`s null");
            }
        }
        return Optional.of(studentList);
    }
    @Override
    public void create(Student student) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            int i = 1;
           connection = daoFactory.getConnection();
           if(student.getGroupId() != 0){
               statement = connection.prepareStatement(CREATE_STUDENT_WITH_GROUP);
               statement.setInt(i,student.getGroupId());
               i++;
           } else {
               statement = connection.prepareStatement(CREATE_STUDENT_WITHOUT_GROUP);
           }
           statement.setString(i,student.getFirstName());
           i++;
           statement.setString(i,student.getLastName());
           if(!statement.execute()){
               throw new DaoException("Student was not created");
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
    public void delete(long studentId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(DELETE_STUDENT);
            statement.setLong(1,studentId);
            statement.execute();
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
    public Optional<List<Student>> getAllByCourseName(Course course) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Student> studentList;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(FIND_BY_COURSE_NAME);
            statement.setString(1,course.getCourseName());
            studentList = readDataFromResultSet(statement.executeQuery());
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
        return Optional.of(studentList);
    }
    /**
     * Converts result set of query to list of courses
     * @throws DaoException exception from dao layer
     * @param resultSet result of select query
     * @return Student list
     * */
    private List<Student> readDataFromResultSet(ResultSet resultSet) throws DaoException {
        List<Student> studentList = new ArrayList<>();
        try {
            while(resultSet.next()){
                studentList.add(new Student(
                        resultSet.getInt("student_id"),
                        resultSet.getInt("group_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name")
                ));
            }
        } catch (SQLException e) {
            throw new DaoException("Reading from result set is failed",e);
        }
        return studentList;
    }
}
