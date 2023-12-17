package ua.bieliaiev.kyrylo.math_assistant.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.BinaryOperator;

public enum Operation {
	PLUS("+", BigDecimal::add, 1),
	MINUS("-", BigDecimal::subtract, 1),
	MULTIPLICATION("*", BigDecimal::multiply, 2),
	DIVISION("/", BigDecimal::divide, 2);

	/* Char that represents operation */
	private final String operationSign;

	/* Functional interface that takes 2 doubles and returns double */
	private final BinaryOperator<BigDecimal> function;

	/* Priority of the operation. Higher - more priority */
	private final int priority;

	Operation(String operationSign, BinaryOperator<BigDecimal> function, int priority) {
		this.operationSign = operationSign;
		this.function = function;
		this.priority = priority;
	}


	/**
	 * Calculates result and returns result of expression.
	 *
	 * @param a Left value
	 * @param b Right value
	 * @return Result of expression
	 */
	public BigDecimal operate(BigDecimal a, BigDecimal b) {
		return function.apply(a, b);
	}

	public int getPriority() {
		return priority;
	}

	/**
	 * Static factory that returns operation based on sing.
	 * For example, if the argument '+', returns Operation.PLUS.
	 *
	 * @param operationSign Char that represents operation.
	 * @return Operation of enum based on char
	 */
	public static Operation getInstanceByChar(String operationSign) {
		return Arrays.stream(Operation.values())
				.filter(operation -> operation.operationSign.equals(operationSign))
				.findAny()
				.orElse(null);
	}

	public static int comparePriorities(String operationSign1, String operationSign2) {

		Operation operation1 = getInstanceByChar(operationSign1);
		Operation operation2 = getInstanceByChar(operationSign2);
		return Integer.compare(operation1.priority, operation2.priority);
	}
}
