package ua.bieliaiev.kyrylo.math_assistant.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.BinaryOperator;

public enum Operation {
	PLUS('+', BigDecimal::add, 1),
	MINUS('-', BigDecimal::subtract, 1),
	MULTIPLICATION('*', BigDecimal::multiply, 2),
	DIVISION('/', BigDecimal::divide, 2);

	/* Char that represents operation */
	private final char operationSign;

	/* Functional interface that takes 2 doubles and returns double */
	private final BinaryOperator<BigDecimal> function;

	/* Priority of the operation. Higher - more priority */
	private final int priority;

	Operation(char operationSign, BinaryOperator<BigDecimal> function, int priority) {
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
	public static Operation getInstanceByChar(char operationSign) {
		return Arrays.stream(Operation.values())
				.filter(operation -> operation.operationSign == operationSign)
				.findAny()
				.orElse(null);
	}

}
