package ua.bieliaiev.kyrylo.math_assistant.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

public record Equation(Integer id, String equation, String reversePolishNotation, Collection<BigDecimal> roots) {

	public void addRoot(BigDecimal root) {
		roots.add(root);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Equation equation1 = (Equation) o;

		if (!Objects.equals(equation, equation1.equation)) return false;
		if (!Objects.equals(reversePolishNotation, equation1.reversePolishNotation))
			return false;
		return Objects.equals(roots, equation1.roots);
	}

	@Override
	public int hashCode() {
		int result = equation != null ? equation.hashCode() : 0;
		result = 31 * result + (reversePolishNotation != null ? reversePolishNotation.hashCode() : 0);
		result = 31 * result + (roots != null ? roots.hashCode() : 0);
		return result;
	}
}
