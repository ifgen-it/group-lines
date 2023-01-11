import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The equation of a line is given by the formula:
 * Ax + By + C = 0,
 * where A ≠ 0 or B ≠ 0.
 * A, B and C are real numbers (double).
 * Given a list of N equations, split this list in groups, where each group includes parallel lines.
 */
public class EquationGrouper {
    private static final int NOT_FOUND_INDEX = -1;
    private static final double ZERO_DOUBLE = 0;
    private static final int DIVIDE_SCALE = 20;
    private static final Logger logger = Logger.getLogger(EquationGrouper.class.getName());

    public Set<Set<String>> groupByParallel(List<String> tokens) {
        List<Equation> equations = tokens.stream()
                .map(token -> {
                    try {
                        return parseToken(token);
                    } catch (RuntimeException ex) {
                        logger.info(String.format("Error during parsing token = %s, exception: %s", token, ex.getMessage()));
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        Map<String, List<Equation>> map = equations.stream()
                .collect(Collectors.groupingBy(equation -> {
                    BigDecimal a = equation.getA();
                    BigDecimal b = equation.getB();
                    if (!BigDecimal.ZERO.equals(a) && !BigDecimal.ZERO.equals(b))
                        return equation.getA().divide(equation.getB(), DIVIDE_SCALE, RoundingMode.UP).toString();
                    else if (BigDecimal.ZERO.equals(a))
                        return "y";
                    else
                        return "x";
                }));

        return map.values().stream()
                .map(parallelEquations -> parallelEquations.stream()
                        .map(Equation::getToken).collect(Collectors.toSet()))
                .collect(Collectors.toSet());
    }

    private Equation parseToken(String srcToken) {
        String token = srcToken.trim().toLowerCase().replaceAll(",", ".");
        int xIndex = token.indexOf("x");
        if (xIndex == NOT_FOUND_INDEX)
            throw new RuntimeException("x variable is not defined");
        String strA = token.substring(0, xIndex);
        BigDecimal a = parseAToDouble(strA);
        token = token.substring(xIndex + 1);
        int yIndex = token.indexOf("y");
        if (yIndex == NOT_FOUND_INDEX)
            throw new RuntimeException("y variable is not defined");
        String strB = token.substring(0, yIndex);
        BigDecimal b = parseBToDouble(strB);
        token = token.substring(yIndex + 1);
        int eqIndex = token.indexOf("=");
        if (eqIndex == NOT_FOUND_INDEX)
            throw new RuntimeException("= symbol is absent");
        String strC = token.substring(0, eqIndex);
        BigDecimal c = parseCToDouble(strC);
        token = token.substring(eqIndex + 1);
        String strZero = token;
        checkZero(strZero);

        return new Equation(a, b, c, srcToken);
    }

    private BigDecimal parseAToDouble(String str) {
        str = str.trim().replaceAll(" ", "");
        if (str.length() == 0)
            return BigDecimal.ONE;
        Pair pair = getSignAndRemain(str, "a");
        BigDecimal sign = pair.sign;
        if (pair.remain.length() == 0) return sign;
        return sign.multiply(new BigDecimal(pair.remain));
    }

    private BigDecimal parseBToDouble(String str) {
        str = str.trim().replaceAll(" ", "");
        if (str.length() == 0)
            throw new RuntimeException("Wrong B-coefficient");

        Pair pair = getSignAndRemain(str, "b");
        BigDecimal sign = pair.sign;
        if (pair.remain.length() == 0) return sign;
        return sign.multiply(new BigDecimal(pair.remain));
    }
    private BigDecimal parseCToDouble(String str) {
        str = str.trim().replaceAll(" ", "");
        if (str.isBlank())
            return BigDecimal.ZERO;
        return new BigDecimal(str);
    }

    private void checkZero(String strZero) {
        if (!strZero.trim().equals("0"))
            throw new RuntimeException("Right to = must be 0");
    }

    // str.length must be > 0
    private Pair getSignAndRemain(String str, String coefficient) {
        char first = str.charAt(0);
        BigDecimal sign = BigDecimal.ONE;
        String remain = str;
        if (first == '-') {
            sign = BigDecimal.valueOf(-1);
            remain = str.substring(1);
        } else if (first == '+') {
            remain = str.substring(1);
        } else {
            if (Set.of("b", "c").contains(coefficient)) {
                throw new RuntimeException("There is no sign for B or C coefficient");
            }
        }
        if (remain.length() > 0) {
            char mustBeDigit = remain.charAt(0);
            if (!Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9').contains(mustBeDigit))
                throw new RuntimeException("Wrong string for parse to Double: " + str);
        }
        return new Pair(sign, remain);
    }
    private static class Pair {
        private final BigDecimal sign;
        private final String remain;

        public Pair(BigDecimal sign, String remain) {
            this.sign = sign;
            this.remain = remain;
        }

        public BigDecimal getSign() {
            return sign;
        }

        public String getRemain() {
            return remain;
        }
    }


    private static class Equation {
        private final BigDecimal a;
        private final BigDecimal b;
        private final BigDecimal c;

        private final String token;

        public Equation(BigDecimal a, BigDecimal b, BigDecimal c, String token) {
            this.a = fixZeroValue(a);
            this.b = fixZeroValue(b);
            this.c = fixZeroValue(c);
            this.token = token;
            if (BigDecimal.ZERO.equals(this.a) && BigDecimal.ZERO.equals(this.b))
                throw new RuntimeException("A, B-coefficients cannot be = 0 simultaneously");
        }

        private BigDecimal fixZeroValue(BigDecimal var) {
            double varDouble = var.doubleValue();
            if (ZERO_DOUBLE == varDouble)
                return BigDecimal.ZERO;
            return var;
        }

        public BigDecimal getA() {
            return a;
        }

        public BigDecimal getB() {
            return b;
        }

        public BigDecimal getC() {
            return c;
        }

        public String getToken() {
            return token;
        }
    }
}
