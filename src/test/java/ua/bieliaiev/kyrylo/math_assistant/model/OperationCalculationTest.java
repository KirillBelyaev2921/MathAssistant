package ua.bieliaiev.kyrylo.math_assistant.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OperationCalculationTest {

	@Test
	public void simplePlusOperation() {

		Operation operation = Operation.PLUS;
		BigDecimal firstNumber = new BigDecimal(5);
		BigDecimal secondNumber = new BigDecimal(12);

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertEquals(result, new BigDecimal(17));
	}

	@Test
	public void simpleMinusOperation() {

		Operation operation = Operation.MINUS;
		BigDecimal firstNumber = new BigDecimal(5);
		BigDecimal secondNumber = new BigDecimal(12);

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertEquals(result, new BigDecimal(-7));
	}

	@Test
	public void simpleMultiplicationOperation() {

		Operation operation = Operation.MULTIPLICATION;
		BigDecimal firstNumber = new BigDecimal(5);
		BigDecimal secondNumber = new BigDecimal(12);

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertEquals(result, new BigDecimal(60));
	}

	@Test
	public void simpleDivisionOperation() {

		Operation operation = Operation.DIVISION;
		BigDecimal firstNumber = new BigDecimal(15);
		BigDecimal secondNumber = new BigDecimal(5);

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertEquals(result, new BigDecimal(3));
	}

	@Test
	public void doubleDivisionOperation() {

		Operation operation = Operation.DIVISION;
		BigDecimal firstNumber = new BigDecimal(12);
		BigDecimal secondNumber = new BigDecimal(5);

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertEquals(result, new BigDecimal("2.4"));
	}

	@Test
	public void divisionByZero() {

		Operation operation = Operation.DIVISION;
		BigDecimal firstNumber = new BigDecimal(12);
		BigDecimal secondNumber = new BigDecimal(0);

		try {
			BigDecimal result = operation.operate(firstNumber, secondNumber);
			fail();
		} catch (ArithmeticException ignored) {}

	}


}