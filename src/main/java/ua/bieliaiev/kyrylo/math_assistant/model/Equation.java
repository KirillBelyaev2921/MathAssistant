package ua.bieliaiev.kyrylo.math_assistant.model;

import java.math.BigDecimal;
import java.util.List;

public record Equation(Integer id, String equation, String reversePolishNotation, List<BigDecimal> roots) {


}
