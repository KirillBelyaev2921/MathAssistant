package ua.bieliaiev.kyrylo.math_assistant.model;

import java.sql.*;
import java.util.Optional;

public class EquationDao implements Dao<Equation, Integer>{

	private final Connection connection;

	public EquationDao() throws SQLException {
		this.connection = JDBCConnection.getConnection();
	}

	@Override
	public Optional<Integer> saveEquation(Equation equation) {

		Optional<Integer> generatedId = Optional.empty();

		String query = """
				INSERT INTO equations(equation_string, rev_polish_notation) VALUES(?, ?);
				""";

		try (PreparedStatement statement = connection.prepareStatement(
				query, Statement.RETURN_GENERATED_KEYS)){

			statement.setString(1, equation.equation());
			statement.setString(2, equation.reversePolishNotation());
			int numberOfInsertedRows = statement.executeUpdate();

			if (numberOfInsertedRows > 0) {
				try (ResultSet resultSet = statement.getGeneratedKeys()) {
					if (resultSet.next()) {
						generatedId = Optional.of(resultSet.getInt(1));
					}
				}
			}

			return generatedId;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
