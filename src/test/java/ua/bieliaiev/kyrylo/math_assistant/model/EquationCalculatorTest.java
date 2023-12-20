package ua.bieliaiev.kyrylo.math_assistant.model;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EquationCalculatorTest {

	@Test
	void isCorrectRootOfEquation() {
		String notation = EquationParser.getReversePolishNotation("12*x=12");
		boolean result = EquationCalculator.isCorrectRootOfEquation(notation, new BigDecimal("1")) == 0;
		assertTrue(result);
	}
	@Test
	void isCorrectRootOfEquation2() {
		String notation = EquationParser.getReversePolishNotation("2*x+5=17");
		boolean result = EquationCalculator.isCorrectRootOfEquation(notation, new BigDecimal("6")) == 0;
		assertTrue(result);
	}
	@Test
	void isNotCorrectRootOfEquation2() {
		String notation = EquationParser.getReversePolishNotation("2*x+5=17");
		boolean result = EquationCalculator.isCorrectRootOfEquation(notation, new BigDecimal("1")) == 0;
		assertFalse(result);
	}
	@Test
	void isCorrectRootOfEquation3() {
		String notation = EquationParser.getReversePolishNotation("-1.3*5/x=1.2");
		boolean result = EquationCalculator.isCorrectRootOfEquation(notation, new BigDecimal("-5.416666666666")) == 0;
		assertTrue(result);
	}
	@Test
	void isCorrectRootOfEquation4() {
		String notation = EquationParser.getReversePolishNotation("2*x*x=10");
		boolean result = EquationCalculator.isCorrectRootOfEquation(notation, new BigDecimal("2.236067977499")) == 0;
		assertTrue(result);
	}
	@Test
	void isCorrectRootOfEquation5() {
		String notation = EquationParser.getReversePolishNotation("2*(x+5+x)+5=10");
		boolean result = EquationCalculator.isCorrectRootOfEquation(notation, new BigDecimal("-1.25")) == 0;
		assertTrue(result);
	}
	@Test
	void isCorrectRootOfEquation6() {
		String notation = EquationParser.getReversePolishNotation(" 17=2*x+5 ");
		boolean result = EquationCalculator.isCorrectRootOfEquation(notation, new BigDecimal("6")) == 0;
		assertTrue(result);
	}

	@Test
	void calculateReversePolishNotationNormalX() {
		String notation = EquationParser.getReversePolishNotation("12*x=12");
		BigDecimal result = EquationCalculator.calculateReversePolishNotationWithX(notation,new BigDecimal("1"));
		assertThat(result, Matchers.comparesEqualTo(new BigDecimal("0")));
	}
	@Test
	void calculateReversePolishNotationNormalX2() {
		String notation = EquationParser.getReversePolishNotation("12+3-4*12-x=124/2-12");
		BigDecimal result = EquationCalculator.calculateReversePolishNotationWithX(notation,new BigDecimal("-83"));
		assertThat(result, Matchers.comparesEqualTo(new BigDecimal("0")));
	}
	@Test
	void calculateReversePolishNotationNormalX3() {
		String notation = EquationParser.getReversePolishNotation("12.2+3-4*12-x=124/2-12");
		BigDecimal result = EquationCalculator.calculateReversePolishNotationWithX(notation,new BigDecimal("-82.8"));
		assertThat(result, Matchers.comparesEqualTo(new BigDecimal("0")));
	}
	@Test
	void calculateReversePolishNotationNormalX4() {
		String notation = EquationParser.getReversePolishNotation("x*x + 9*x + 8 = 0");
		BigDecimal result = EquationCalculator.calculateReversePolishNotationWithX(notation,new BigDecimal("-8"));
		assertThat(result, Matchers.comparesEqualTo(new BigDecimal("0")));
	}
	@Test
	void calculateReversePolishNotationNormalX5() {
		String notation = EquationParser.getReversePolishNotation("x*x + 9*x + 8 = 0");
		BigDecimal result = EquationCalculator.calculateReversePolishNotationWithX(notation,new BigDecimal("-1"));
		assertThat(result, Matchers.comparesEqualTo(new BigDecimal("0")));
	}
}