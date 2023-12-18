package ua.bieliaiev.kyrylo.math_assistant.model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationParser {

	// Defines a pattern, that checks for incorrect symbols.
	private static final Pattern incorrectSymbols = Pattern.compile("[^0-9.x=()/*+-]");

	/* Defines a pattern, that used for mather.find() iteration.
	   Group 1 defines opening parentheses with possibility of unary minus.
	   Group 2 defines double number or x variable with possibility of unary minus.
	   Group 3 defines closing parentheses. Group 4 defines next operation sign.
	 */
	private static final Pattern equationPattern = Pattern.compile("(-?[(]*)(-?[0-9.x]+)([)]*)([*/+-]?)");


	/**
	 * Check if an equation is correct. If it is, return reverse Polish Notation that must
	 * be equals to 0. If it is incorrect, return empty string. Also, if there is no x variable,
	 * equation will return empty string.
	 *
	 * @param equation String equation
	 * @return reverse Polish Notation if equation is correct, empty String otherwise.
	 * @throws IllegalArgumentException if there are problems in equation.
	 */
	public static String getReversePolishNotation(String equation) {

		String newEquation = equation.replace(" ", "");
		// Replace 'x' in Cyrillic script on normal 'x'
		newEquation = newEquation.replace("х", "x");

		checkIsSymbolsCorrect(newEquation);
		if (!isParenthesesCorrect(equation))
			throw new IllegalArgumentException("Problem with parentheses");
		if (!newEquation.contains("x")) {
			throw new IllegalArgumentException("No x variable");
		}

		String[] equations = newEquation.split("=");

		if (!checkEquation(equations[0]) ||
			!checkEquation(equations[1])) {
			throw new IllegalArgumentException("Extra operation sign");
		}
		newEquation = equations[0] + "-(" + equations[1] + ")";

		return calculateReversePolishNotation(newEquation);

	}


	/**
	 * Check if an equation has the correct number of equation signs
	 * and no incorrect symbols.
	 *
	 * @param equation String equation to check that there are no problems with symbols
	 * @throws IllegalArgumentException if there are problems in equation.
	 */
	public static boolean checkIsSymbolsCorrect(String equation) {

		// Check on incorrect symbols
		Matcher matcher = incorrectSymbols.matcher(equation);
		if (matcher.find()) {
			throw new IllegalArgumentException("Incorrect symbols: " + matcher.group());
		}

		// Check on normal amount of equation signs and correct placement
		int firstEquationSign = equation.indexOf('=');
		int lastEquationSign = equation.lastIndexOf('=');

		if (!(firstEquationSign != -1 &&
				firstEquationSign == lastEquationSign &&
				firstEquationSign != 0 &&
				firstEquationSign != equation.length() - 1)) {
			throw new IllegalArgumentException("Problems with equation sign");
		}

		return true;
	}

	/**
	 * Check if an equation has the correct number of parentheses and
	 * in right place (equations like '())(' is not allowed)
	 *
	 * @param equation String equation to check that there are no problems with parentheses
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

	/**
	 * Check if an equation doesn't have extra operation sign at the end.
	 *
	 * @param equation String equation to check that there are no problems with operation signs
	 * @return true if there are no incorrect operation signs at the end.
	 */
	private static boolean checkEquation(String equation) {

		return Operation.getInstanceByChar(equation.substring(equation.length() - 1)) == null;
	}

	/**
	 * Converts an equation into reverse Polish Notation that must
	 * be equals to 0. If equation has extra operation sings (as "12+*3=x"), or if
	 * they are in incorrect (as "+12=3"), return empty string.
	 *
	 * @param equation String equation
	 * @return reverse Polish Notation if equation is correct, empty String otherwise.
	 * @throws IllegalArgumentException if there are problems in equation.
	 */
	private static String calculateReversePolishNotation(String equation) {

		// Get matcher from equation Pattern
		Matcher m = equationPattern.matcher(equation);

		// Output queue represents result, while operator stack represents operations in stack
		Deque<String> outputQueue = new LinkedList<>();
		Deque<String> operatorStack = new LinkedList<>();

		// store last index of previous subsequence.
		int last = 0;
		while (m.find()) {
			 /* If start of new subsequence not equals to end of last,
			 	this means that there are some extra characters left.
			    This will occur, if equation has extra operation sings, or
			 	they are in incorrect place.
			 */
			if (m.start() != last) {
				throw new IllegalArgumentException("Extra symbols");
			}

			// For each group, add values to outputQueue and operatorStack, if they present.
			addOpeningParentheses(outputQueue, operatorStack, m.group(1));
			addNumberOrX(outputQueue, operatorStack, m.group(2));
			addClosingParentheses(outputQueue, operatorStack, m.group(3));
			addNextOperation(outputQueue, operatorStack, m.group(4));

			last = m.end();
		}

		while (!operatorStack.isEmpty()) {
			outputQueue.add(operatorStack.pop());
		}

		return String.join(",", outputQueue);
	}

	/**
	 * Add opening parentheses into operatorStack. If there are many of them,
	 * add all. If there are unary minus before parentheses, add it as -1 * (...
	 *
	 * @param outputQueue Output queue of reverse Polish notation.
	 * @param operatorStack Operator Stack of reverse Polish notation.
	 * @param str opening parentheses that needed to be added.
	 */
	private static void addOpeningParentheses(Deque<String> outputQueue, Deque<String> operatorStack, String str) {
		if (!str.equals("")) {
			if (str.charAt(0) == '-') {
				outputQueue.add("-1");
				operatorStack.push("*");
				str = str.substring(1);
			}
			for (char bracket : str.toCharArray()) {
				operatorStack.push("" + bracket);
			}
		}
	}

	/**
	 * Add number or variable into outputQueue. If there are unary minus
	 * before x variable, add it as -1 * x.
	 *
	 * @param outputQueue Output queue of reverse Polish notation.
	 * @param operatorStack Operator Stack of reverse Polish notation.
	 * @param str number or variable that needed to be added.
	 * @throws IllegalArgumentException if there are problems like "12x=12".
	 */
	private static void addNumberOrX(Deque<String> outputQueue, Deque<String> operatorStack, String str) {
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
				throw new IllegalArgumentException("Wrong number: " + str);
			}
		}
	}

	/**
	 * Check on closing parentheses. If there are some, add all from
	 * operatorStack to outputDeque, until opening parenthesis appears.
	 *
	 * @param outputQueue Output queue of reverse Polish notation.
	 * @param operatorStack Operator Stack of reverse Polish notation.
	 * @param str closing parentheses that needed to be processed.
	 */
	private static void addClosingParentheses(Deque<String> outputQueue, Deque<String> operatorStack, String str) {
		if (!str.equals("")) {
			for (int i = 0; i < str.length(); i++) {
				String operator;
				while (!(operator = operatorStack.pop()).equals("(")) {
					outputQueue.add(operator);
				}
			}
		}
	}

	/**
	 * Add next operation sign. If it has less priority than previous,
	 * add previous from stack to queue.
	 *
	 * @param outputQueue Output queue of reverse Polish notation.
	 * @param operatorStack Operator Stack of reverse Polish notation.
	 * @param str operation sign that needed to be added.
	 */
	private static void addNextOperation(Deque<String> outputQueue, Deque<String> operatorStack, String str) {
		if (!str.equals("")) {
			String operator;
			while (!operatorStack.isEmpty() &&
					!(operator = operatorStack.peek()).equals("(") &&
					Operation.comparePriorities(str, operator) <= 0) {
				outputQueue.add(operatorStack.pop());
			}

			operatorStack.push(str);
		}
	}
}
