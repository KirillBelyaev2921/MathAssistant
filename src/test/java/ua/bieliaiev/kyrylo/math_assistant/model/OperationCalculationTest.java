package ua.bieliaiev.kyrylo.math_assistant.model;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OperationCalculationTest {

	@Test
	public void simplePlusOperation() {

		Operation operation = Operation.PLUS;
		BigDecimal firstNumber = new BigDecimal("5.0");
		BigDecimal secondNumber = new BigDecimal("12.0");

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertEquals(result, new BigDecimal("17.0"));
	}

	@Test
	public void simpleMinusOperation() {

		Operation operation = Operation.MINUS;
		BigDecimal firstNumber = new BigDecimal("5.0");
		BigDecimal secondNumber = new BigDecimal("12.0");

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertThat(result, Matchers.comparesEqualTo(new BigDecimal("-7.0")));
	}

	@Test
	public void simpleMultiplicationOperation() {

		Operation operation = Operation.MULTIPLICATION;
		BigDecimal firstNumber = new BigDecimal("5.0");
		BigDecimal secondNumber = new BigDecimal("12.0");

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertThat(result, Matchers.comparesEqualTo(new BigDecimal("60.0")));
	}

	@Test
	public void simpleDivisionOperation() {

		Operation operation = Operation.DIVISION;
		BigDecimal firstNumber = new BigDecimal("15.0");
		BigDecimal secondNumber = new BigDecimal("5.0");

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertThat(result, Matchers.comparesEqualTo(new BigDecimal("3.0")));
	}

	@Test
	public void doubleDivisionOperation() {

		Operation operation = Operation.DIVISION;
		BigDecimal firstNumber = new BigDecimal("12.0");
		BigDecimal secondNumber = new BigDecimal("5.0");

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertThat(result, Matchers.comparesEqualTo(new BigDecimal("2.4")));
	}
	@Test
	public void doubleDivisionOperationWithDoubles() {

		Operation operation = Operation.DIVISION;
		BigDecimal firstNumber = new BigDecimal("6.0");
		BigDecimal secondNumber = new BigDecimal("3.0");

		BigDecimal result = operation.operate(firstNumber, secondNumber);

		assertThat(result, Matchers.comparesEqualTo(new BigDecimal("2.0")));
	}

	@Test
	public void divisionByZero() {

		Operation operation = Operation.DIVISION;
		BigDecimal firstNumber = new BigDecimal("12.0");
		BigDecimal secondNumber = new BigDecimal("0.0");

		assertThrows(ArithmeticException.class,() -> operation.operate(firstNumber, secondNumber));

	}


}