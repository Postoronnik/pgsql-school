package data.dao;

import ua.com.foxminded.data.dao.DaoException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DaoFactory {
    private static DaoFactory instance = null;

    private DaoFactory(){ }

    private final static String URL;
    private final static String USERNAME;
    private final static String PASSWORD;

    static {
        Properties properties = new Properties();
        String propertiesFileName = "src/test/resources/config.properties";

        try{
            properties.load(new FileInputStream(propertiesFileName));
        } catch (IOException exception) {
            exception.printStackTrace();
            try {
                throw new DaoException("File " + propertiesFileName + " not found",exception);
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }

        URL = properties.getProperty("url");
        USERNAME = properties.getProperty("userName");
        PASSWORD = properties.getProperty("password");
    }

    public static DaoFactory getInstance(){
        if(instance == null){
            instance = new DaoFactory();
        }
        return instance;
    }

    public Connection getConnection() throws DaoException {

        Connection connection;
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e){
            e.printStackTrace();
            throw new DaoException("Connection is failed", e);
        }

        return connection;
    }
}

