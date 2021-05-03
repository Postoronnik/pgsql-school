package data.dao;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.data.dao.DaoException;
import ua.com.foxminded.data.dao.DaoFactory;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DaoFactoryTest{

    @Test
    void shouldReturnDifferentConnections() throws DaoException {
        ua.com.foxminded.data.dao.DaoFactory daoFactory = DaoFactory.getInstance();
        assertNotEquals(daoFactory.getConnection(),daoFactory.getConnection());
    }
}
