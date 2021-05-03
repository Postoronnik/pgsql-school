package ua.com.foxminded.data.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
/**
 * Class provides connection to database
 * @author Ind_us
 * @version 1.0
 * */
public class DaoFactory {
    /**
     * Instance of this class. Singleton pattern
     * */
    private static DaoFactory instance = null;
    /**
     * Private default constructor
     * */
    private DaoFactory(){ }
    /**
     * URL of connector
     * */
    private final static String URL;
    /**
     * DB username
     * */
    private final static String USERNAME;
    /**
     * DB password
     * */
    private final static String PASSWORD;

    static {
        Properties properties = new Properties();
        String propertiesFileName = "src/main/resources/config.properties";

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
    /**
     * Create if instance is null and returns or just returns
     * @return instance of class
     * */
    public static DaoFactory getInstance(){
        if(instance == null){
            instance = new DaoFactory();
        }
        return instance;
    }
    /**
     * Creates connection.
     * @throws DaoException exception from dao layer
     * @return new Connection
     * */
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
