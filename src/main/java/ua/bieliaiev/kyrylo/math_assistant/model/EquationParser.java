package ua.bieliaiev.kyrylo.math_assistant.model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationParser {

	private static final Pattern incorrectSymbols = Pattern.compile("[^0-9.x=()/*+-]");
	private static final Pattern equationPass = Pattern.compile("([(]?)(-?[0-9.x]+)([)]?)([*/+-]?)");


	/**
	 * Checks if an equation is correct. If it is, return reverse Polish Notation that must
	 * be equals to 0. If it is incorrect, return empty string.
	 *
	 * @param equation String equation
	 * @return reverse Polish Notation if correct equation, empty String otherwise.
	 */
	public static String getReversePolishNotation(String equation) {

		String newEquation = equation.replace(" ", "");

		if (!isSymbolsCorrect(newEquation) || !isParenthesesCorrect(equation))
			return "";


		String[] equations = newEquation.split("=");
		newEquation = equations[0] + "-(" + equations[1] + ")";

		return calculateReversePolishNotation(newEquation);

	}


	/**
	 * Checks if an equation has the correct number of equation signs
	 * and no incorrect symbols.
	 *
	 * @param equation String equation to check if there are no problems with symbols
	 * @return true if there are no incorrect symbols
	 */
	public static boolean isSymbolsCorrect(String equation) {

		// Check on incorrect symbols
		Matcher matcher = incorrectSymbols.matcher(equation);
		if (matcher.find()) {
			return false;
		}

		// Check on normal amount of equation signs and correct placement
		int firstEquationSign = equation.indexOf('=');
		int lastEquationSign = equation.lastIndexOf('=');

		return (firstEquationSign != -1 &&
				firstEquationSign == lastEquationSign &&
				firstEquationSign != 0 &&
				firstEquationSign != equation.length() - 1);

	}

	/**
	 * Checks if an equation has the correct number of parentheses and
	 * in right place (equations like '())(' is not allowed)
	 *
	 * @param equation String equation to check if there are no problems with parenthesis
	 * @return true if there are no problems with parentheses
	 */
	public static boolean isParenthesesCorrect(String equation) {

		/* Amount of currently opened parentheses */
		int openedParentheses = 0;

		for (char c : equation.toCharArray()) {
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

	private static String calculateReversePolishNotation(String equation) {

		Matcher m = equationPass.matcher(equation);

		Deque<String> outputQueue = new LinkedList<>();
		Deque<String> operatorStack = new LinkedList<>();

		int last = 0;
		while (m.find()) {
			if (m.start() != last) {
				return "";
			}
			String str = m.group(1);
			if (!str.equals("")) {
				operatorStack.push(str);
			}
			str = m.group(2);
			if (!str.equals("")) {
				if (!str.contains("x")) {
					outputQueue.add(str);
				} else if (str.equals("-x")) {
					outputQueue.add("-1");
					operatorStack.push("*");
					outputQueue.add("x");
				} else if (str.equals("x")) {
					outputQueue.add("x");
				} else {
					return "";
				}
			}
			str = m.group(3);
			if (!str.equals("")) {
				String operator;
				while (!(operator = operatorStack.pop()).equals("(")) {
					outputQueue.add(operator);
				}
			}
			str = m.group(4);
			if (!str.equals("")) {
				String operator;
				while (!operatorStack.isEmpty() &&
						!(operator = operatorStack.peek()).equals("(") &&
						Operation.comparePriorities(str, operator) < 0) {
					outputQueue.add(operatorStack.pop());
				}

				operatorStack.push(str);
			}
			last = m.end();
		}

		while (!operatorStack.isEmpty()) {
			outputQueue.add(operatorStack.pop());
		}

		return String.join(",", outputQueue);
	}
}
