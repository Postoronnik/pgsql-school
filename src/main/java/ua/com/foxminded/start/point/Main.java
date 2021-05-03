package ua.com.foxminded.start.point;

import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.ui.UserOptions;

/**
 * @author Ind_us
 * @version 1.0
 * Entry point of program
 * */
public class Main {

    /**
     * Main method
     * @param args doing nothing here
     * @throws DaoException exception from dao layer
     * */
    public static void main(String[] args) throws DaoException {
        RunSqlScript.initDatabase("schema.sql");
        UserOptions userOptions = new UserOptions();
        userOptions.start();
    }
}
