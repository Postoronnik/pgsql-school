package ua.com.foxminded.data.dao;

import java.util.concurrent.ExecutionException;

/**
 * @author Ind_us
 * @version 1.0
 * @see java.util.concurrent.ExecutionException
 * @see java.io.Serializable
 * @see java.lang.Exception
 * @see java.lang.Throwable
 * @serial
 * Exception for dao layer
 * */
public class DaoException extends ExecutionException {
    /**
     * @param cause cause message
     * @param message message of error
     *                Exception constructor with cause data
     * */
    public DaoException(String message, Throwable cause){
        super(message, cause);
    }
    /**
     * @param message message of error
     *                Exception constructor without cause data
     * */
    public DaoException(String message){
        super(message);
    }
}
