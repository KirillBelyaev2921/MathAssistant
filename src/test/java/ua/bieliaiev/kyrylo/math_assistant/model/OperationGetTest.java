package ua.bieliaiev.kyrylo.math_assistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OperationGetTest {

	@Test
	public void getPlusOperation() {

		String operator = "+";

		Operation expectedOperation = Operation.getInstanceByChar(operator);

		assertEquals(expectedOperation, Operation.PLUS);
	}

	@Test
	public void getDivisionOperation() {

		String operator = "/";

		Operation expectedOperation = Operation.getInstanceByChar(operator);

		assertEquals(expectedOperation, Operation.DIVISION);
	}

	@Test
	public void getNoneExistingOperation() {

		String operator = "^";

		Operation expectedOperation = Operation.getInstanceByChar(operator);

		assertNull(expectedOperation);
	}

	@Test
	public void getPriorityFromMinus() {

		int priority = Operation.MINUS.getPriority();

		assertEquals(priority, 1);
	}

	@Test
	public void getPriorityFromMultiplication() {

		int priority = Operation.MULTIPLICATION.getPriority();

		assertEquals(priority, 2);
	}

}