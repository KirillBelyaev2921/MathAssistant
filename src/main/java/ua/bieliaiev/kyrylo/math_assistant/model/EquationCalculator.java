package ua.bieliaiev.kyrylo.math_assistant.model;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.LinkedList;

public class EquationCalculator {

	/**
	 * Return true if value of variable x is the root of the equation, false otherwise.
	 *
	 * @param equation string equation in reverse Polish notation with ',' delimiter
	 * @param x        value of x variable
	 * @return true if value of variable x is the root of the equation, false otherwise
	 */
	public static boolean isCorrectRootOfEquation(String equation, BigDecimal x) {

		return calculateReversePolishNotationWithX(equation, x).abs().doubleValue() < 1e-9;
	}

	/**
	 * Return the result of calculation of an equation of reverse Polish notation,
	 * based on value of variable x.
	 * <p>
	 * This method don't check, if the equation string is valid, and will be used with
	 * the result of EquationParser method getReversePolishNotation()
	 *
	 * @param equation string equation in reverse Polish notation with ',' delimiter
	 * @param x        value of x variable
	 * @return result of expression
	 */
	public static BigDecimal calculateReversePolishNotationWithX(String equation, BigDecimal x) {

		String[] tokens = equation.split(",");

		Deque<BigDecimal> stack = new LinkedList<>();

		for (String token : tokens) {
			Operation operation;
			if ((operation = Operation.getInstanceByChar(token)) != null) {
				BigDecimal secondNumber = stack.pop();
				BigDecimal firstNumber = stack.pop();
				BigDecimal result = operation.operate(firstNumber, secondNumber);
				stack.push(result);
			} else if (token.equals("x")) {
				stack.push(x);
			} else {
				stack.push(new BigDecimal(token));
			}
		}

		return stack.pop();
	}
}
