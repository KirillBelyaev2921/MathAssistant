package ua.bieliaiev.kyrylo.math_assistant.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationParser {

	private static final Pattern incorrectSymbols = Pattern.compile("[^0-9.x=()/*+-]");

	public static boolean isCorrectEquation(String equation) {

		String newEquation = equation.replace(" ", "");
		return false;

	}

	/**
	 * Checks that the equation has correct number of equation signs
	 * and no incorrect symbols.
	 *
	 * @param expression String equation to check if there are no problems with parenthesis
	 * @return true if there are no problems with parentheses
	 */
	public static boolean isSymbolsCorrect(String expression) {

		Matcher matcher = incorrectSymbols.matcher(expression);
		if (matcher.find()) {
			return false;
		}

		int firstEquationSign = expression.indexOf('=');
		int lastEquationSign = expression.lastIndexOf('=');

		return (firstEquationSign != -1 &&
				firstEquationSign == lastEquationSign &&
				firstEquationSign != 0 &&
				firstEquationSign != expression.length() - 1);

	}

	/**
	 * Checks that the equation has correct number of parentheses and
	 * in right place (expressions like '())(' is not allowed)
	 *
	 * @param expression String equation to check if there are no problems with parenthesis
	 * @return true if there are no problems with parentheses
	 */
	public static boolean isParenthesesCorrect(String expression) {

		/* Amount of currently opened parentheses */
		int openedParentheses = 0;

		for (char c : expression.toCharArray()) {
			if (c == '(') {

				// Add 1 if opening parentheses
				openedParentheses++;
			} else if (c == ')') {

				// Subtract 1 if closing parentheses and check that there are not negative number.
				openedParentheses--;
				if (openedParentheses < 0) {
					return false;
				}
			} else if (c == '=') {

				// If there are equation sign - check for closed parentheses.
				if (openedParentheses != 0) {
					return false;
				}
			}
		}

		return openedParentheses == 0;
	}
}
