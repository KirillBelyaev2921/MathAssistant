package ua.bieliaiev.kyrylo.math_assistant.controller;

import ua.bieliaiev.kyrylo.math_assistant.model.Equation;
import ua.bieliaiev.kyrylo.math_assistant.model.EquationCalculator;
import ua.bieliaiev.kyrylo.math_assistant.model.EquationDao;
import ua.bieliaiev.kyrylo.math_assistant.model.EquationParser;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class EquationsController {

	private final EquationDao equationDao;

	public EquationsController(Properties properties) {
		try {
			equationDao = new EquationDao(properties);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public String saveEquation(String equation) {

		return saveEquationWithRoot(equation, "");
	}

	public String saveEquationWithRoot(String equation, String x) {

		String notation;
		List<BigDecimal> roots = new ArrayList<>();
		try {
			notation = EquationParser.getReversePolishNotation(equation);
		} catch (Exception e) {
			return e.getMessage();
		}

		if (!x.isBlank()) {
			double isRoot;
			try {
				isRoot = EquationCalculator.isCorrectRootOfEquation(notation, new BigDecimal(x));
			} catch (Exception e) {
				return e.getMessage();
			}
			if (isRoot != 0) {
				return x + " is not root of this equation, the difference is " + isRoot;
			} else {
				roots.add(new BigDecimal(x));
			}
		}

		Equation newEquation = new Equation(0, equation, notation, roots);
		Optional<Integer> result = equationDao.saveEquation(newEquation);

		if (result.isPresent()) {
			return "Saved successfully";
		} else {
			return "Not saved";
		}
	}

	public List<String> getAllEquations() {
		return equationDao.getAllEquations().stream()
				.map(Equation::equation)
				.toList();
	}

	public List<String> getAllEquationsByRoot(String x) {
		return equationDao.getAllEquationsByRoot(new BigDecimal(x))
				.stream()
				.map(Equation::equation)
				.toList();
	}

	public List<String> getAllRootsOfEquation(String equation) {
		return equationDao.getEquationByEquationString(equation)
				.stream()
				.flatMap(e -> e.roots().stream())
				.map(BigDecimal::toPlainString)
				.collect(Collectors.toList());
	}
}
