package database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class PostgreSQLConnectionTest {

    @Test
    void testDatabaseConnection() {
        PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();

        try (Connection connection = postgreSQLConnection.connect()) {
            // Assert that the connection is not null
            assertNotNull(connection);

            // Assert that the connection is open
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            fail("Failed to connect to the database: " + e.getMessage());
        }
    }
}
