package ua.bieliaiev.kyrylo.math_assistant;

import ua.bieliaiev.kyrylo.math_assistant.controller.EquationsController;
import ua.bieliaiev.kyrylo.math_assistant.model.JDBCConnection;
import ua.bieliaiev.kyrylo.math_assistant.view.EquationView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		// Get properties for database connection.
		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(Path.of("src/main/resources/database.properties"),
					StandardOpenOption.READ));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Create tables if they are not exists
		try {
			Connection connection = JDBCConnection.getConnection(properties);
			String tables = """
					CREATE TABLE IF NOT EXISTS equations(
					    equation_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START 1 INCREMENT 1 ),
					    equation_string character varying(255) NOT NULL UNIQUE,
					    rev_polish_notation character varying(255) NOT NULL UNIQUE,
					    CONSTRAINT equation_pkey PRIMARY KEY (equation_id)
					);
					     
					CREATE TABLE IF NOT EXISTS roots(
					    root_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START 1 INCREMENT 1 ),
					    root_value character varying(255) NOT NULL,
					    equation_id integer NOT NULL,
					    CONSTRAINT root_pkey PRIMARY KEY (root_id),
					    FOREIGN KEY (equation_id)
					        REFERENCES equations(equation_id)
					);""";

			try (Statement s = connection.createStatement()) {
				s.executeUpdate(tables);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// Run view with controller
		new EquationView(new EquationsController(properties));
	}
}
