package ua.bieliaiev.kyrylo.math_assistant.model;

import java.util.Optional;

public interface Dao<T, I> {
	Optional<I> saveEquation(T t);
//	Optional<I> addRootToEquation(T t);
//	Optional<T> getEquationByEquationString(String equationString);
//	Collection<T> getAllEquation();
//	Collection<T> getAllEquationsByRoot(BigDecimal root);

}
