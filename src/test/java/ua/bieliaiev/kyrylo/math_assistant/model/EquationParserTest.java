package ua.bieliaiev.kyrylo.math_assistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquationParserTest {

	@Test
	public void checkOnSymbolsWhenThereIsNormalEquationWithTrue() {
		String equation = "12=12*x";
		boolean expectedResult = EquationParser.checkIsSymbolsCorrect(equation);
		assertTrue(expectedResult);
	}
	@Test
	public void checkOnSymbolsWhenThereIsComplicatedEquationWithTrue() {
		String equation = "x-(x*12)=((12*x)+1)/(1-x*(x+2))";
		boolean expectedResult = EquationParser.checkIsSymbolsCorrect(equation);
		assertTrue(expectedResult);
	}
	@Test
	public void checkOnSymbolsWhenThereIsNoEquationSingWithFalse() {
		String equation = "x+2";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.checkIsSymbolsCorrect(equation));
	}
	@Test
	public void checkOnSymbolsWhenThereIsTwoEquationSingsWithFalse() {
		String equation = "x+2=x-2=0";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.checkIsSymbolsCorrect(equation));
	}
	@Test
	public void checkOnSymbolsWhenThereIsSpaceWithFalse() {
		String equation = "x+2 = 1";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.checkIsSymbolsCorrect(equation));
	}
	@Test
	public void checkOnSymbolsWhenThereIncorrectSymbolWithFalse() {
		String equation = "x^2=1";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.checkIsSymbolsCorrect(equation));
	}
	@Test
	public void checkOnSymbolsWhenThereIncorrectSymbol2WithFalse() {
		String equation = "x^2=a";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.checkIsSymbolsCorrect(equation));
	}

	@Test
	public void checkOnParenthesesWhenThereAreNoneOfThemWithTrue() {
		String equation = "12=12*x";
		boolean expectedResult = EquationParser.isParenthesesCorrect(equation);
		assertTrue(expectedResult);
	}
	@Test
	public void checkOnParenthesesWhenThereAreOneOfThemTrue() {
		String equation = "12=(12*x)+1";
		boolean expectedResult = EquationParser.isParenthesesCorrect(equation);
		assertTrue(expectedResult);
	}
	@Test
	public void checkOnParenthesesWhenThereAreManyOfThemTrue() {
		String equation = "x-(x*12)=((12*x)+1)/(1-x*(x+2))";
		boolean expectedResult = EquationParser.isParenthesesCorrect(equation);
		assertTrue(expectedResult);
	}
	@Test
	public void checkOnParenthesesWhenThereAreNotEnoughClosingWithFalse() {
		String equation = "(x+2=4";
		boolean expectedResult = EquationParser.isParenthesesCorrect(equation);
		assertFalse(expectedResult);
	}
	@Test
	public void checkOnParenthesesWhenThereAreNotEnoughOpeningWithFalse() {
		String equation = "x+2)=4";
		boolean expectedResult = EquationParser.isParenthesesCorrect(equation);
		assertFalse(expectedResult);
	}
	@Test
	public void checkOnParenthesesWhenThereAreWrongOrderWithFalse() {
		String equation = ")x+2(=4";
		boolean expectedResult = EquationParser.isParenthesesCorrect(equation);
		assertFalse(expectedResult);
	}

	@Test
	public void getReversePolishNotationWithSimpleEquation() {
		String equation = "12*x=12";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "12,x,*,12,-");
	}
	@Test
	public void getReversePolishNotationWithSimpleEquationWithSpaces() {
		String equation = "12 * x = 12";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "12,x,*,12,-");
	}
	@Test
	public void getReversePolishNotationWithIncorrectEqualsSign() {
		String equation = "12*x=/";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectEqualsSign2() {
		String equation = "/=12*x";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectEqualsSign3() {
		String equation = "=12*x";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectEqualsSign4() {
		String equation = "12*x=";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectSymbols() {
		String equation = "12*x-a=12";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectNumber() {
		String equation = "12x-a=12";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectOperations() {
		String equation = "12*+x=12";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectOperations2() {
		String equation = "12*+x=12-+123";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectOperations3() {
		String equation = "*12*x=12-123";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectOperations4() {
		String equation = "12*x=12-123/";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectOperations5() {
		String equation = "12*x/=12-123";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithIncorrectParentheses() {
		String equation = "(12*x=12";
		assertThrows(IllegalArgumentException.class, () -> EquationParser.getReversePolishNotation(equation));
	}
	@Test
	public void getReversePolishNotationWithCorrectParentheses() {
		String equation = "(12*x)=12";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "12,x,*,12,-");
	}
	@Test
	public void getReversePolishNotationWithUnaryMinusBeforeNumber() {
		String equation = "(12*x)=-12";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "12,x,*,-12,-");
	}
	@Test
	public void getReversePolishNotationWithUnaryMinusBeforeX() {
		String equation = "(12*-x)=12";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "12,-1,x,*,*,12,-");
	}
	@Test
	public void getReversePolishNotationWithUnaryMinusBeforeParentheses() {
		String equation = "-(12*x)=12";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "-1,12,x,*,*,12,-");
	}
	@Test
	public void getReversePolishNotationWithUnaryMinusBeforeManyParentheses() {
		String equation = "-(4/((12*x)+3))=12";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "-1,4,12,x,*,3,+,/,*,12,-");
	}
	@Test
	public void getReversePolishNotationWithUnaryMinusAfterSummation() {
		String equation = "1+-2=x";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "1,-1,2,*,+,x,-");
	}
	@Test
	public void getReversePolishNotationWithUnaryMinusAfterSubtraction() {
		String equation = "1--2=x";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "1,-1,2,*,-,x,-");
	}
	@Test
	public void getReversePolishNotationWithUnaryMinusAfterDivision() {
		String equation = "1/-2=x";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "1,-1,2,*,/,x,-");
	}
	@Test
	public void getReversePolishNotationWithUnaryMinusAfterMultiplication() {
		String equation = "1*-2=x";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "1,-1,2,*,*,x,-");
	}
	@Test
	public void getReversePolishNotationWithComplicatedEquation() {
		String equation = "x-(x*12)=((12*x)+1)/(1-x*(x+2))";
		String result = EquationParser.getReversePolishNotation(equation);
		assertEquals(result, "x,x,12,*,-,12,x,*,1,+,1,x,x,2,+,*,-,/,-");
	}

}