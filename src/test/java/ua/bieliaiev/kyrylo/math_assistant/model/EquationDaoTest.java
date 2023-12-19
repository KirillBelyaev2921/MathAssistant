package ua.bieliaiev.kyrylo.math_assistant.model;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EquationDaoTest {

	private static Connection connection;
	private static EquationDao equationDao;

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
			connection = JDBCConnection.getConnection(properties);
			String tables = """
					CREATE TABLE IF NOT EXISTS equations(
					    equation_id integer NOT NULL GENERATED ALWAYS AS IDENTITY (START 1 INCREMENT 1 ),
					    equation_string character varying(255) NOT NULL UNIQUE,
					    rev_polish_notation character varying(255) NOT NULL,
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
			equationDao = new EquationDao(properties);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void saveEquation() {
		String equationString = "x*x=4";
		String reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		Equation equation = new Equation(0, equationString, reversePolishNotation,
				List.of(new BigDecimal(2), new BigDecimal(-2)));

		int id = equationDao.saveEquation(equation).orElse(0);

		assertNotEquals(id, 0);
	}

	@Test
	void saveEquationWithRoots() {
		String equationString = "x*x=4";
		String reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		Equation equation = new Equation(0, equationString, reversePolishNotation,
				List.of(new BigDecimal(2), new BigDecimal(-2)));

		equationDao.saveEquation(equation);
		equationDao.getEquationByEquationString(equationString).ifPresentOrElse(e -> assertEquals(e.roots(), equation.roots()),
				Assertions::fail);
	}

	@Test
	void getEquation() {
		String equationString = "x*x=4";
		String reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		Equation equation = new Equation(0, equationString, reversePolishNotation,
				List.of(new BigDecimal(2), new BigDecimal(-2)));

		int id = equationDao.saveEquation(equation).orElse(0);
		equationDao.getEquationByEquationString(equationString).ifPresentOrElse(e -> assertEquals(e, equation),
				Assertions::fail);

		assertNotEquals(id, 0);
	}

	@Test
	void getEquationWithDoubleRoot() {
		String equationString = "x*x=5";
		String reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		Equation equation = new Equation(0, equationString, reversePolishNotation,
				List.of(new BigDecimal("2.2360679774997896964091736687313"), new BigDecimal("-2.2360679774997896964091736687313")));

		int id = equationDao.saveEquation(equation).orElse(0);
		equationDao.getEquationByEquationString(equationString).ifPresentOrElse(e -> assertEquals(e, equation),
				Assertions::fail);

		assertNotEquals(id, 0);
	}

	@Test
	void saveEquationWithoutRoots() {
		String equationString = "12*x=4";
		String reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		Equation equation = new Equation(0, equationString, reversePolishNotation,
				List.of());

		equationDao.saveEquation(equation);
		equationDao.getEquationByEquationString(equationString).ifPresentOrElse(e -> assertEquals(e.roots(), Collections.EMPTY_LIST),
				Assertions::fail);
	}

	@Test
	void getAllEquationsWithNoEquations() {
		Collection<Equation> equations = equationDao.getAllEquations();
		assertTrue(equations.isEmpty());
	}
	@Test
	void getAllEquationsWithOneEquation() {
		String equationString = "x*x=4";
		String reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		Equation equation = new Equation(0, equationString, reversePolishNotation,
				List.of(new BigDecimal(2), new BigDecimal(-2)));

		equationDao.saveEquation(equation);

		List<Equation> equations = new ArrayList<>(equationDao.getAllEquations());
		assertEquals(equations.get(0), equation);
	}

	@Test
	void getAllEquationsWithTwoEquations() {
		List<Equation> equations = new ArrayList<>();

		String equationString = "x*x=4";
		String reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		Equation equation = new Equation(0, equationString, reversePolishNotation,
				List.of(new BigDecimal(2), new BigDecimal(-2)));
		equations.add(equation);
		equationDao.saveEquation(equation);

		equationString = "12*x=4";
		reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		equation = new Equation(0, equationString, reversePolishNotation,
				List.of());
		equations.add(equation);
		equationDao.saveEquation(equation);

		List<Equation> returnedEquations = new ArrayList<>(equationDao.getAllEquations());
		assertThat(equations, Matchers.containsInAnyOrder(returnedEquations.toArray()));
	}

	@Test
	void getAllEquationsWithDifferentEquations() {
		List<Equation> equations = new ArrayList<>();

		String equationString = "x*x=4";
		Equation equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of(new BigDecimal(2), new BigDecimal(-2)));
		equations.add(equation);
		equationDao.saveEquation(equation);

		equationString = "12*x=4";
		equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of());
		equations.add(equation);
		equationDao.saveEquation(equation);

		equationString = "12*x=4";
		equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of());
		//equations.add(equation);  	// Do not add, because duplicate
		equationDao.saveEquation(equation);

		equationString = "2*x+5=17";
		equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of(new BigDecimal("6")));
		equations.add(equation);
		equationDao.saveEquation(equation);

		equationString = " 2*(x+5+х)+5=10";
		equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of());
		equations.add(equation);
		equationDao.saveEquation(equation);

		equationString = "17=2*x+5";
		equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of());
		equations.add(equation);
		equationDao.saveEquation(equation);

		List<Equation> returnedEquations = new ArrayList<>(equationDao.getAllEquations());
		assertThat(equations, Matchers.containsInAnyOrder(returnedEquations.toArray()));
	}

	@Test
	void getAllEquationsWithRootTwoFromTwoEquations() {
		List<Equation> equations = new ArrayList<>();

		String equationString = "x*x=4";
		String reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		Equation equation = new Equation(0, equationString, reversePolishNotation,
				List.of(new BigDecimal(2), new BigDecimal(-2)));
		equations.add(equation);
		equationDao.saveEquation(equation);

		equationString = "12*x=4";
		reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		equation = new Equation(0, equationString, reversePolishNotation,
				List.of());
		equationDao.saveEquation(equation);

		List<Equation> returnedEquations = new ArrayList<>(equationDao.getAllEquationsByRoot(new BigDecimal(2)));
		assertThat(equations, Matchers.containsInAnyOrder(returnedEquations.toArray()));
	}

	@Test
	void getAllEquationsWithRootSixFromDifferentEquations() {
		List<Equation> equations = new ArrayList<>();

		String equationString = "x*x=4";
		Equation equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of(new BigDecimal(2), new BigDecimal(-2)));
		equationDao.saveEquation(equation);

		equationString = "12*x=4";
		equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of());
		equationDao.saveEquation(equation);

		equationString = "12*x=4";
		equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of());
		equationDao.saveEquation(equation);

		equationString = "2*x+5=17";
		equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of(new BigDecimal("6")));
		equations.add(equation);
		equationDao.saveEquation(equation);

		equationString = " 2*(x+5+х)+5=10";
		equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of());
		equationDao.saveEquation(equation);

		equationString = "17=2*x+5";
		equation = new Equation(0, equationString, EquationParser.getReversePolishNotation(equationString),
				List.of(new BigDecimal("6")));
		equations.add(equation);
		equationDao.saveEquation(equation);

		List<Equation> returnedEquations = new ArrayList<>(equationDao.getAllEquationsByRoot(new BigDecimal("6")));
		assertThat(equations, Matchers.containsInAnyOrder(returnedEquations.toArray()));
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