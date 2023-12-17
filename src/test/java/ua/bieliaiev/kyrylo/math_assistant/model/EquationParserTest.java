package ua.bieliaiev.kyrylo.math_assistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquationParserTest {

	@Test
	public void checkOnSymbolsWhenThereIsNormalEquationWithTrue() {
		String expression = "12=12*x";
		boolean expectedResult = EquationParser.isSymbolsCorrect(expression);
		assertTrue(expectedResult);
	}
	@Test
	public void checkOnSymbolsWhenThereIsComplicatedEquationWithTrue() {
		String expression = "x-(x*12)=((12*x)+1)/(1-x*(x+2))";
		boolean expectedResult = EquationParser.isSymbolsCorrect(expression);
		assertTrue(expectedResult);
	}
	@Test
	public void checkOnSymbolsWhenThereIsNoEquationSingWithFalse() {
		String expression = "x+2";
		boolean expectedResult = EquationParser.isSymbolsCorrect(expression);
		assertFalse(expectedResult);
	}
	@Test
	public void checkOnSymbolsWhenThereIsTwoEquationSingsWithFalse() {
		String expression = "x+2=x-2=0";
		boolean expectedResult = EquationParser.isSymbolsCorrect(expression);
		assertFalse(expectedResult);
	}
	@Test
	public void checkOnSymbolsWhenThereIsSpaceWithFalse() {
		String expression = "x+2 = 1";
		boolean expectedResult = EquationParser.isSymbolsCorrect(expression);
		assertFalse(expectedResult);
	}
	@Test
	public void checkOnSymbolsWhenThereIncorrectSymbolWithFalse() {
		String expression = "x^2=1";
		boolean expectedResult = EquationParser.isSymbolsCorrect(expression);
		assertFalse(expectedResult);
	}
	@Test
	public void checkOnSymbolsWhenThereIncorrectSymbol2WithFalse() {
		String expression = "x^2=a";
		boolean expectedResult = EquationParser.isSymbolsCorrect(expression);
		assertFalse(expectedResult);
	}

	@Test
	public void checkOnParenthesesWhenThereAreNoneOfThemWithTrue() {
		String expression = "12=12*x";
		boolean expectedResult = EquationParser.isParenthesesCorrect(expression);
		assertTrue(expectedResult);
	}
	@Test
	public void checkOnParenthesesWhenThereAreOneOfThemTrue() {
		String expression = "12=(12*x)+1";
		boolean expectedResult = EquationParser.isParenthesesCorrect(expression);
		assertTrue(expectedResult);
	}
	@Test
	public void checkOnParenthesesWhenThereAreManyOfThemTrue() {
		String expression = "x-(x*12)=((12*x)+1)/(1-x*(x+2))";
		boolean expectedResult = EquationParser.isParenthesesCorrect(expression);
		assertTrue(expectedResult);
	}
	@Test
	public void checkOnParenthesesWhenThereAreNotEnoughClosingWithFalse() {
		String expression = "(x+2=4";
		boolean expectedResult = EquationParser.isParenthesesCorrect(expression);
		assertFalse(expectedResult);
	}
	@Test
	public void checkOnParenthesesWhenThereAreNotEnoughOpeningWithFalse() {
		String expression = "x+2)=4";
		boolean expectedResult = EquationParser.isParenthesesCorrect(expression);
		assertFalse(expectedResult);
	}
	@Test
	public void checkOnParenthesesWhenThereAreWrongOrderWithFalse() {
		String expression = ")x+2(=4";
		boolean expectedResult = EquationParser.isParenthesesCorrect(expression);
		assertFalse(expectedResult);
	}

}