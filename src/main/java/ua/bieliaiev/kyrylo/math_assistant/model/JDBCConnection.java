package ua.bieliaiev.kyrylo.math_assistant.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

	private static Connection connection = null;

	public static synchronized Connection getConnection() throws SQLException {
		if (connection == null) {
			String url = "jdbc:postgresql://localhost:5432/math_assistant";
			String user = "math_assistant";
			String password = "1234";

			connection = DriverManager.getConnection(url, user, password);
		}

		return connection;
	}
}
