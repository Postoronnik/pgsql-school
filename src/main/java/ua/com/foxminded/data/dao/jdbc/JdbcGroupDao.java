package ua.com.foxminded.data.dao.jdbc;

import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.data.dao.DaoFactory;
import ua.com.foxminded.data.domain.Group;
import ua.com.foxminded.data.dao.GroupDao;
import ua.com.foxminded.data.dao.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Class implements GroupDao interface methods and realize executing queries for group
 * @author Ind_us
 * @version 1.0
 * @see GroupDao
 * @see Dao
 * */
public class JdbcGroupDao implements GroupDao {
    /**
     * SQL query for creating group
     * */
    private static final String CREATE_GROUP = "select university.create_group(?)";
    /**
     * SQL query for deleting group
     * */
    private static final String DELETE_GROUP = "delete from university.groups where groups.group_id = ?";
    /**
     * SQL query for finding all groups
     * */
    private static final String FIND_ALL = "select * from university.groups";
    /**
     * SQL query for finding groups with less or equal students amount
     * */
    private static final String FIND_ALL_LESS_OR_EQUALS = "" +
            "SELECT university.groups.group_name,university.students.group_id, " +
            "COUNT(students.group_id)\n" +
            "    FROM university.students\n" +
            "             INNER JOIN university.groups ON students.group_id = groups.group_id\n" +
            "    GROUP BY students.group_id, group_name\n" +
            "    HAVING COUNT(students.group_id)<= ?;";
    /**
     * Instance of connection factory
     * */
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    @Override
    public Optional<List<Group>> getAll() throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Group> groupList;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(FIND_ALL);
            groupList = readDataFromResultSet(statement.executeQuery());
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
        return Optional.of(groupList);
    }

    @Override
    public void create(Group group) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(CREATE_GROUP);
            statement.setString(1,group.getGroupName());
            if(!statement.execute()){
                throw new DaoException("Group was not created");
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
    public void delete(long groupId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(DELETE_GROUP);
            statement.setLong(1,groupId);
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
    public Optional<List<Group>> findAllLessOrEquals(long studentsAmount) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Group> groupList;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(FIND_ALL_LESS_OR_EQUALS);
            statement.setLong(1,studentsAmount);
            groupList = readDataFromResultSet(statement.executeQuery());
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
        return Optional.of(groupList);
    }
    /**
     * Converts result set of query to list of courses
     * @throws DaoException exception from dao layer
     * @param resultSet result of select query
     * @return Group list
     * */
    private List<Group> readDataFromResultSet(ResultSet resultSet) throws DaoException {
        List<Group> groupList = new ArrayList<>();
        try {
            while(resultSet.next()){
                groupList.add(new Group(
                        resultSet.getInt("group_id"),
                        resultSet.getString("group_name")
                ));
            }
        } catch (SQLException e) {
            throw new DaoException("Reading from result set is failed",e);
        }
        return groupList;
    }
}
