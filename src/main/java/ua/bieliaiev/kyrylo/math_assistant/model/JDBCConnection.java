package ua.bieliaiev.kyrylo.math_assistant.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnection {

	private static Connection connection = null;

	public static synchronized Connection getConnection(Properties properties) throws SQLException {
		if (connection == null || connection.isClosed()) {

			String url = properties.getProperty("url");
			String user = properties.getProperty("user");
			String password = properties.getProperty("password");

			connection = DriverManager.getConnection(url, user, password);
		}

		return connection;
	}
}
