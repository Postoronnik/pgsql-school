package ui;

import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.data.dao.DaoFactory;
import ua.com.foxminded.data.dao.jdbc.JdbcCourseDao;
import ua.com.foxminded.data.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.data.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.data.domain.Course;
import ua.com.foxminded.data.domain.Group;
import ua.com.foxminded.data.domain.Student;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.ui.UserOptions;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class UserOptionsTest {

    private static final UserOptions userOptionsClass = new UserOptions();
    private static final DaoFactory factory = DaoFactory.getInstance();
    private static final JdbcCourseDao courseDao = new JdbcCourseDao();

    @Test
    void shouldReturnTrueForExistingCourse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, DaoException {
        initDatabase();

        try {
            String existingCourseName = "Existing course name";
            String notExistingCourseName = "Not existing course name";
            courseDao.create(new Course(existingCourseName,"Some description"));
            Method method = userOptionsClass.getClass().getDeclaredMethod("isNoSuchCourseName", String.class);
            method.setAccessible(true);
            assertTrue((Boolean) method.invoke(userOptionsClass,notExistingCourseName));
        } finally {
            clearDataBase();
        }
    }

    @Test
    void shouldReturnFalseForNotExistingCourse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, DaoException {
        initDatabase();

        try {
            String existingCourseName = "Existing course name";
            courseDao.create(new Course(existingCourseName,"Some description"));
            Method method = userOptionsClass.getClass().getDeclaredMethod("isNoSuchCourseName", String.class);
            method.setAccessible(true);
            assertFalse((Boolean) method.invoke(userOptionsClass,existingCourseName));
        } finally {
            clearDataBase();
        }
    }

    @Test
    void shouldReturnTrueForExistingGroup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = userOptionsClass.getClass().getDeclaredMethod("isSuchGroupExist", int.class, int[].class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(userOptionsClass,1,new int[]{1,2,3,4}));
    }

    @Test
    void shouldReturnTrueForNotExistingGroup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = userOptionsClass.getClass().getDeclaredMethod("isSuchGroupExist", int.class, int[].class);
        method.setAccessible(true);
        assertFalse((Boolean) method.invoke(userOptionsClass,5,new int[]{1,2,3,4}));
    }

    @Test
    void shouldShowAllStudents() throws DaoException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        initDatabase();

        new JdbcGroupDao().create(new Group(""));
        new JdbcStudentDao().create(new Student(1,1,"Name1","Surname"));
        new JdbcStudentDao().create(new Student(2,1,"Name1","Surname"));

        String expected = new Student(1,1,"Name1","Surname") + "\n" +
                new Student(2,1,"Name1","Surname").toString().trim();

        Method method = userOptionsClass.getClass().getDeclaredMethod("showStudents");
        method.setAccessible(true);
        try {
            ByteArrayOutputStream outputCaptor = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputCaptor));
            method.invoke(userOptionsClass);
            assertEquals(expected, outputCaptor.toString().trim());

        } finally {
            System.setOut(System.out);
        }

        clearDataBase();
    }

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

}
