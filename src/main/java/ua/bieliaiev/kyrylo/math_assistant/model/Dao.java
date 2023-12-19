package ua.bieliaiev.kyrylo.math_assistant.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface Dao<T, U, I> {
	Optional<I> saveEquation(T t);
	void saveRootsOfEquation(Collection<U> u, int id);
	Optional<T> getEquationByEquationString(String equationString);
	Collection<U> getRootsByEquationId(int id);
	Collection<T> getAllEquations();
	Collection<T> getAllEquationsByRoot(BigDecimal root);

}
