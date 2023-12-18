package ua.bieliaiev.kyrylo.math_assistant.model;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EquationDaoTest {

	@Test
	void saveEquation() {

		String equationString = "12*x=12";
		String reversePolishNotation = EquationParser.getReversePolishNotation(equationString);
		Equation equation = new Equation(0, equationString, reversePolishNotation, new ArrayList<>());

		try  {
			EquationDao equationDao = new EquationDao();
			int id = equationDao.saveEquation(equation).orElse(0);
			assertNotEquals(id, 0);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}