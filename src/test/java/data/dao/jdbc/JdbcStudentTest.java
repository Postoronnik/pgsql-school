package data.dao.jdbc;

import ua.com.foxminded.data.dao.CourseDao;
import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.data.dao.DaoFactory;
import ua.com.foxminded.data.dao.StudentDao;
import ua.com.foxminded.data.dao.jdbc.JdbcCourseDao;
import ua.com.foxminded.data.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.data.domain.Course;
import ua.com.foxminded.data.domain.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcStudentTest {
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
    void shouldNotThrowExceptionForCreatingStudent(){
        assertDoesNotThrow(() -> new JdbcStudentDao().create(new Student("Name","Surname")));
    }

    @Test
    void shouldThrowDaoExceptionForCreatingEmptyStudent() {
        assertThrows(DaoException.class, () -> new JdbcStudentDao().create(new Student()));
    }

    @Test
    void shouldNotThrowDaoExceptionForDeletingStudent() {
        assertDoesNotThrow(() -> new JdbcStudentDao().delete(1));
    }

    @Test
    void shouldShowAddedStudents() throws DaoException {
        StudentDao studentDao = new JdbcStudentDao();
        List<Student> expected = new ArrayList<>();
        int studentsNum = 3;
        for(int i = 1; i <= studentsNum; i++){
            studentDao.create(new Student("Some name","Some surname"));
            expected.add(new Student(i,0,"Some name","Some surname"));
        }
        assertEquals(expected,studentDao.getAll().get());
    }

    @Test
    void shouldShowStudentsRelatedToCourse() throws DaoException {
        CourseDao courseDao = new JdbcCourseDao();
        initCourses(3);
        initStudents(6);
        courseDao.addStudent(1,1);
        courseDao.addStudent(1,2);
        courseDao.addStudent(1,4);
        courseDao.addStudent(2,1);

        List<Student> expected = new ArrayList<>();

        expected.add(new Student(1,0,"Some name","Some surname"));
        expected.add(new Student(2,0,"Some name","Some surname"));
        expected.add(new Student(4,0,"Some name","Some surname"));

        assertEquals(expected,new JdbcStudentDao().getAllByCourseName(new Course("Course name 1","Course description")).get());
    }

    private void initCourses(int coursesNum) throws DaoException {
        CourseDao courseDao = new JdbcCourseDao();
        for(int i = 1; i <= coursesNum; i++){
            courseDao.create(new Course("Course name " + i,"Course description"));
        }
    }

    private void initStudents(int studentNum) throws DaoException {
        StudentDao studentDao = new JdbcStudentDao();
        for(int i = 1; i <= studentNum; i++){
            studentDao.create(new Student("Some name","Some surname"));
        }
    }


}
