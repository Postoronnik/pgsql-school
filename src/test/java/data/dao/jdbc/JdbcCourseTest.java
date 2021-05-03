package data.dao.jdbc;

import ua.com.foxminded.data.dao.CourseDao;
import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.data.dao.DaoFactory;
import ua.com.foxminded.data.domain.Course;
import ua.com.foxminded.data.domain.Group;
import ua.com.foxminded.data.domain.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.data.dao.jdbc.JdbcCourseDao;
import ua.com.foxminded.data.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.data.dao.jdbc.JdbcStudentDao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcCourseTest {
    private static final DaoFactory factory = DaoFactory.getInstance();

    @BeforeEach
    public  void initDatabase() throws DaoException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = factory.getConnection();
            String sql = getSqlFileQueries("src/test/resources/scripts/schema.sql");
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert statement != null;
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterEach
    public  void clearDataBase() throws DaoException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection  = factory.getConnection();
            String sql = getSqlFileQueries("src/test/resources/scripts/clearDB.sql");
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert statement != null;
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getSqlFileQueries(String fileRoute) throws IOException {
        StringBuilder queries = new StringBuilder();
        File file = new File(fileRoute);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String string;
        while ((string = br.readLine()) != null) {
            queries.append(string);
        }
        return queries.toString();
    }

    @Test
    void shouldNotThrowExceptionForCreatingCourse(){
        assertDoesNotThrow(() -> new JdbcCourseDao().create(new Course("Course name","Some description")));

    }

    @Test
    void shouldNotThrowExceptionForAddingStudentToCourse() {
        try {
            new JdbcCourseDao().create(new Course("Course","Description"));
            new JdbcStudentDao().create(new Student("Name","Surname"));
            new JdbcGroupDao().create(new Group("Group"));
        } catch (DaoException e) {
            e.printStackTrace();
        }
        assertDoesNotThrow(() -> new JdbcCourseDao().addStudent(1,1));
    }

    @Test
    void shouldThrowDaoExceptionForCreatingEmptyCourse() {
        assertThrows(DaoException.class, () -> new JdbcCourseDao().create(new Course()));
    }

    @Test
    void shouldNotThrowDaoExceptionForDeletingCourse() {
        assertDoesNotThrow(() -> new JdbcCourseDao().delete(1));
    }

    @Test
    void shouldNotThrowDaoExceptionForRemovingStudentFromTheCourse() {
        assertDoesNotThrow(() -> new JdbcCourseDao().removeStudent(1,1));
    }

    @Test
    void shouldShowAddedCourses() throws DaoException {
        CourseDao courseDao = new JdbcCourseDao();
        List<Course> expected = new ArrayList<>();
        int coursesNum = 3;
        for(int i = 1; i <= coursesNum; i++){
            courseDao.create(new Course("Course name" + i,"Course description"));
            expected.add(new Course(i,"Course name" + i,"Course description"));
        }
        assertEquals(expected,courseDao.getAll().get());
    }


}
