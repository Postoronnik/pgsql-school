package ua.com.foxminded.start.point;

import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.data.dao.DaoFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Ind_us
 * @version 1.0
 * Class provides creating database schema and functions
 * */
public class RunSqlScript {
    /**
     * Instance of connection factory
     * */
    private static DaoFactory factory = DaoFactory.getInstance();
    /**
     * Provides executing sql query from file
     * @param sqlFileRoute file route/name
     * @throws DaoException exception from dao layer
     * */
    public static void initDatabase(String sqlFileRoute) throws DaoException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = factory.getConnection();
            String sql = getSqlFileQueries(sqlFileRoute);
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
    /**
     * Provides getting sql file content
     * @param fileName file route/name
     * @return String of file
     * @throws IOException trouble with file directory or name
     * */
    private static String getSqlFileQueries(String fileName) throws IOException {
        InputStream inputStream = RunSqlScript.class.getClassLoader().getResourceAsStream(fileName);
        assert inputStream != null;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString(String.valueOf(StandardCharsets.UTF_8));
    }
}
