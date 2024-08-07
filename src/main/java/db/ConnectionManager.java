package db;

import java.sql.*;

public interface ConnectionManager {
    Connection getConnection() throws SQLException;
}