package ua.bieliaiev.kyrylo.math_assistant.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.bieliaiev.kyrylo.math_assistant.model.JDBCConnection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EquationsControllerTest {

	private static Connection connection;
	private static EquationsController controller;

	@BeforeAll
	static void createTables() {

		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(Path.of("src/test/resources/database.properties"),
					StandardOpenOption.READ));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			controller = new EquationsController(properties);
			connection = JDBCConnection.getConnection(properties);
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
	}

	@Test
	void saveEquation() {
		String equation = "12*x=12";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Saved successfully");
	}

	@Test
	void saveTwoEquations() {
		String equation = "12*x=12";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Saved successfully");

		equation = "2*x*x=5";
		result = controller.saveEquation(equation);
		assertEquals(result, "Saved successfully");
	}
	@Test
	void saveTwoSameEquations() {
		String equation = "12*x=12";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Saved successfully");

		result = controller.saveEquation(equation);
		assertEquals(result, "Saved successfully");
		assertEquals(1, controller.getAllEquations().size());
	}
	@Test
	void saveWrongEquation() {
		String equation = "12*x12";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Problems with equation sign");
	}
	@Test
	void saveWrongEquation2() {
		String equation = "=12*x=";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Problems with equation sign");
	}
	@Test
	void saveWrongEquation3() {
		String equation = "12x=12";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Wrong number: 12x");
	}
	@Test
	void saveWrongEquation4() {
		String equation = "12.0++x=12.1";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Extra symbols");
	}
	@Test
	void saveWrongEquation5() {
		String equation = "12.0+a+x=12.1";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Incorrect symbols: a");
	}
	@Test
	void saveWrongEquation6() {
		String equation = "12.1=12.1";
		String result = controller.saveEquation(equation);
		assertEquals(result, "No x variable");
	}
	@Test
	void saveWrongEquation7() {
		String equation = "12.1+x/=12.1";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Extra operation sign");
	}
	@Test
	void saveWrongEquation8() {
		String equation = "(12+x=1)";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Problem with parentheses");
	}
	@Test
	void saveWrongEquation9() {
		String equation = "((12+x=1)";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Problem with parentheses");
	}
	@Test
	void saveWrongEquation10() {
		String equation = "12+x=1)";
		String result = controller.saveEquation(equation);
		assertEquals(result, "Problem with parentheses");
	}

	@Test
	void saveRootOfEquation() {
		String equation = "12*x=12";
		String result = controller.saveEquationWithRoot(equation, "1");
		assertEquals(result, "Saved successfully");
	}
	@Test
	void saveWrongRootOfEquation() {
		String equation = "12*x=12";
		String result = controller.saveEquationWithRoot(equation, "2");
		assertEquals(result, "2 is not root of this equation, the difference is 12.0");
	}
	@Test
	void saveWrongRootOfEquation2() {
		String equation = "12*x=12";
		String result = controller.saveEquationWithRoot(equation, "x");
		assertEquals(result, "Character x is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.");
	}

	@Test
	void saveTwoRootsOfEquation() {
		String equation = "x*x=4";
		String result = controller.saveEquationWithRoot(equation, "2");
		assertEquals(result, "Saved successfully");
		result = controller.saveEquationWithRoot(equation, "-2");

		assertEquals(result, "Saved successfully");
	}

	@Test
	void getAllEquationsWhenOne() {
		String equation = "x*x=4";
		controller.saveEquationWithRoot(equation, "2");
		controller.saveEquationWithRoot(equation, "-2");
		List<String> result = controller.getAllEquations();
		assertThat(result, Matchers.containsInAnyOrder(List.of(equation).toArray()));
	}

	@Test
	void getAllEquationsWhenTwo() {
		String equation = "x*x=4";
		controller.saveEquationWithRoot(equation, "2");
		controller.saveEquationWithRoot(equation, "-2");

		String secondEquation = "12+x=1";
		controller.saveEquation(secondEquation);
		List<String> result = controller.getAllEquations();
		assertThat(result, Matchers.containsInAnyOrder(List.of(equation, secondEquation).toArray()));
	}

	@Test
	void getAllEquationsWhenThree() {
		String equation = "x*x=4";
		controller.saveEquationWithRoot(equation, "2");
		controller.saveEquationWithRoot(equation, "-2");

		String secondEquation = "12+x=1";
		controller.saveEquation(secondEquation);
		String thirdEquation = "12*x=12";
		controller.saveEquationWithRoot(thirdEquation, "1");
		List<String> result = controller.getAllEquations();
		assertThat(result, Matchers.containsInAnyOrder(List.of(equation, secondEquation, thirdEquation).toArray()));
	}

	@Test
	void getAllEquationsByRoot() {
		String equation = "x*x=4";
		controller.saveEquationWithRoot(equation, "2");
		controller.saveEquationWithRoot(equation, "-2");
		List<String> result = controller.getAllEquationsByRoot("2");
		assertThat(result, Matchers.containsInAnyOrder(List.of(equation).toArray()));
	}
	@Test
	void getOneEquationByRootWithOthers() {
		String equation = "x*x=4";
		controller.saveEquationWithRoot(equation, "2");
		controller.saveEquationWithRoot(equation, "-2");

		String secondEquation = "12+x=1";
		controller.saveEquation(secondEquation);
		String thirdEquation = "12*x=12";
		controller.saveEquationWithRoot(thirdEquation, "1");
		List<String> result = controller.getAllEquationsByRoot("2");
		assertThat(result, Matchers.containsInAnyOrder(List.of(equation).toArray()));
	}
	@Test
	void getTwoEquationsByRootWithOthers() {
		String equation = "x*x=4";
		controller.saveEquationWithRoot(equation, "2");
		controller.saveEquationWithRoot(equation, "-2");

		String secondEquation = "12+x=1";
		controller.saveEquation(secondEquation);
		String thirdEquation = "12*x=12";
		controller.saveEquationWithRoot(thirdEquation, "1");
		String fourthEquation = "10+x=12";
		controller.saveEquationWithRoot(fourthEquation, "2");
		List<String> result = controller.getAllEquationsByRoot("2");
		assertThat(result, Matchers.containsInAnyOrder(List.of(equation, fourthEquation).toArray()));
	}

	@Test
	void getZeroRootsOfEquation() {
		String equation = "12*x=12";
		controller.saveEquation(equation);

		List<String> result = controller.getAllRootsOfEquation(equation);
		assertThat(result, Matchers.containsInAnyOrder(List.of().toArray()));
	}
	@Test
	void getOneRootOfEquation() {
		String equation = "x*x=4";
		controller.saveEquationWithRoot(equation, "2");

		List<String> result = controller.getAllRootsOfEquation(equation);
		assertThat(result, Matchers.containsInAnyOrder(List.of("2").toArray()));
	}

	@Test
	void getTwoRootsOfEquation() {
		String equation = "x*x=4";
		controller.saveEquationWithRoot(equation, "2");
		controller.saveEquationWithRoot(equation, "-2");

		List<String> result = controller.getAllRootsOfEquation(equation);
		assertThat(result, Matchers.containsInAnyOrder(List.of("2", "-2").toArray()));
	}

	@AfterEach
	void clearRows() {
		try {
			String tables = """
					TRUNCATE roots, equations""";

			try (Statement s = connection.createStatement()) {
				s.executeUpdate(tables);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	@AfterAll
	static void closeConnection() {
		try {
			String tables = """
					DROP TABLE roots;
					DROP TABLE equations;""";

			try (Statement s = connection.createStatement()) {
				s.executeUpdate(tables);
			}
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}