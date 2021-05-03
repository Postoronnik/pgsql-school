package data.dao.jdbc;

import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.data.dao.DaoFactory;
import ua.com.foxminded.data.dao.GroupDao;
import ua.com.foxminded.data.dao.StudentDao;
import ua.com.foxminded.data.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.data.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.data.domain.Group;
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

public class JdbcGroupTest {
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
    void shouldNotThrowExceptionForCreatingGroup(){
        assertDoesNotThrow(() -> new JdbcGroupDao().create(new Group("Group name")));
    }

    @Test
    void shouldThrowDaoExceptionForCreatingEmptyGroup() {
        assertThrows(DaoException.class, () -> new JdbcGroupDao().create(new Group()));
    }

    @Test
    void shouldNotThrowDaoExceptionForDeletingGroup() {
        assertDoesNotThrow(() -> new JdbcGroupDao().delete(1));
    }

    @Test
    void shouldShowAddedGroups() throws DaoException {
        GroupDao groupDao = new JdbcGroupDao();
        List<Group> expected = new ArrayList<>();
        int groupsNum = 3;
        for(int i = 1; i <= groupsNum; i++){
            groupDao.create(new Group("Group-" + i));
            expected.add(new Group(i,"Group-" + i));
        }
        assertEquals(expected,groupDao.getAll().get());
    }

    @Test
    void shouldShowGroupsWithLessOrEqualsStudentsNumber() throws DaoException {
        GroupDao groupDao = new JdbcGroupDao();
        List<Group> expected = new ArrayList<>();
        int groupsNum = 3;
        for(int i = 1; i <= groupsNum; i++){
            groupDao.create(new Group("Group-" + i));
        }
        addStudentsToGroup(3,1);

        expected.add(new Group(1,"Group-1"));
        assertEquals(expected,groupDao.findAllLessOrEquals(4).get());
    }

    private void addStudentsToGroup(int studentsNum, int groupId) throws DaoException {
        StudentDao studentDao = new JdbcStudentDao();
        for(int i = 0; i < studentsNum; i++){
            studentDao.create(new Student(groupId,"Some name","Some surname"));
        }
    }


}
