package ua.bieliaiev.kyrylo.math_assistant.model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

public class EquationDao implements Dao<Equation, BigDecimal, Integer> {

	private final Connection connection;

	public EquationDao(Properties properties) throws SQLException {
		this.connection = JDBCConnection.getConnection(properties);
	}

	@Override
	public Optional<Integer> saveEquation(Equation equation) {

		Optional<Integer> optionalId = Optional.empty();
		Optional<Equation> equationOptional = getEquationByEquationString(equation.equation());
		Collection<BigDecimal> roots = new ArrayList<>(equation.roots());

		if (equationOptional.isEmpty()) {

			String query = """
					INSERT INTO equations(equation_string, rev_polish_notation) VALUES(?, ?);
					""";

			try (PreparedStatement statement = connection.prepareStatement(
					query, Statement.RETURN_GENERATED_KEYS)) {

				statement.setString(1, equation.equation());
				statement.setString(2, equation.reversePolishNotation());
				int numberOfInsertedRows = statement.executeUpdate();

				if (numberOfInsertedRows > 0) {
					try (ResultSet resultSet = statement.getGeneratedKeys()) {
						if (resultSet.next()) {
							optionalId = Optional.of(resultSet.getInt(1));
						}
					}
				}

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			roots.removeAll(equationOptional.get().roots());
			optionalId = Optional.of(equationOptional.get().id());
		}

		optionalId.ifPresent(integer -> saveRootsOfEquation(roots, integer));

		return optionalId;
	}

	@Override
	public void saveRootsOfEquation(Collection<BigDecimal> roots, int id) {
		String query = """
				INSERT INTO roots(root_value, equation_id) VALUES(?, ?);
				""";

		for (BigDecimal root : roots) {
			try (PreparedStatement statement = connection.prepareStatement(
					query, Statement.RETURN_GENERATED_KEYS)) {

				statement.setString(1, root.toString());
				statement.setInt(2, id);
				statement.executeUpdate();

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Optional<Equation> getEquationByEquationString(String equationString) {
		Optional<Equation> optionalEquation = Optional.empty();

		String equationQuery = "SELECT * FROM equations WHERE equation_string='" + equationString + "'";

		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(equationQuery)) {

			if (resultSet.next()) {
				int id = resultSet.getInt("equation_id");
				String newEquationString = resultSet.getString("equation_string");
				String newNotation = resultSet.getString("rev_polish_notation");
				Collection<BigDecimal> roots = getRootsByEquationId(id);
				optionalEquation = Optional.of(new Equation(id, newEquationString, newNotation, roots));

			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return optionalEquation;
	}

	@Override
	public Collection<BigDecimal> getRootsByEquationId(int id) {

		Collection<BigDecimal> roots = new ArrayList<>();
		String rootsQuery = "SELECT * FROM roots WHERE equation_id=" + id;

		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(rootsQuery)) {

			while (resultSet.next()) {
				roots.add(resultSet.getBigDecimal("root_value"));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return roots;
	}

	@Override
	public Collection<Equation> getAllEquations() {

		Collection<Equation> equations;

		String query = "SELECT e.equation_id, equation_string, rev_polish_notation, root_value " +
				"FROM equations e LEFT JOIN roots r on e.equation_id = r.equation_id";

		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(query)) {

			equations = getEquationsFromResultSet(resultSet);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return equations;
	}

	@Override
	public Collection<Equation> getAllEquationsByRoot(BigDecimal root) {
		
		Collection<Equation> equations;

		String query = """
				SELECT e.equation_id, equation_string, rev_polish_notation, root_value
				FROM equations e INNER JOIN roots r on e.equation_id = r.equation_id, (
				    SELECT e.equation_id
				    FROM equations e INNER JOIN roots r on e.equation_id = r.equation_id WHERE r.root_value='%s'
				) AS fr WHERE e.equation_id=fr.equation_id;
				""".formatted(root);

		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(query)) {

			equations = getEquationsFromResultSet(resultSet);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return equations;
	}

	private Collection<Equation> getEquationsFromResultSet(ResultSet resultSet) throws SQLException {

		Collection<Equation> equations = new ArrayList<>();

		int lastId = 0;
		Equation lastEquation = new Equation(0, "", "", new ArrayList<>());
		while (resultSet.next()) {
			if (resultSet.getInt(1) != lastId) {
				int id = resultSet.getInt(1);
				String newEquationString = resultSet.getString("equation_string");
				String newNotation = resultSet.getString("rev_polish_notation");
				lastId = id;
				lastEquation = new Equation(id, newEquationString, newNotation, new ArrayList<>());
				equations.add(lastEquation);
			}
			if (resultSet.getString("root_value") != null)
				lastEquation.addRoot(new BigDecimal(resultSet.getString("root_value")));

		}

		return equations;
	}
}
