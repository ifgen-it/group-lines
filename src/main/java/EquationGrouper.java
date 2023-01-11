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

        Map<Double, List<Equation>> map = equations.stream()
                .collect(Collectors.groupingBy(equation -> equation.getA() / equation.getB()));

        return map.values().stream()
                .map(parallelEquations -> parallelEquations.stream()
                        .map(Equation::getToken).collect(Collectors.toSet()))
                .collect(Collectors.toSet());
    }

    private Equation parseToken(String srsToken) {
        String token = srsToken.trim().toLowerCase().replaceAll(",", ".");
        int xIndex = token.indexOf("x");
        if (xIndex == NOT_FOUND_INDEX)
            throw new RuntimeException("x variable is not defined");
        String strA = token.substring(0, xIndex);
        double a = parseAToDouble(strA);
        token = token.substring(xIndex + 1);
        int yIndex = token.indexOf("y");
        if (yIndex == NOT_FOUND_INDEX)
            throw new RuntimeException("y variable is not defined");
        String strB = token.substring(0, yIndex);
        double b = parseBToDouble(strB);
        token = token.substring(yIndex + 1);
        int eqIndex = token.indexOf("=");
        if (eqIndex == NOT_FOUND_INDEX)
            throw new RuntimeException("= symbol is absent");
        String strC = token.substring(0, eqIndex);
        double c = parseCToDouble(strC);
        token = token.substring(eqIndex + 1);
        String strZero = token;
        checkZero(strZero);

        return new Equation(a, b, c, srsToken);
    }

    private double parseAToDouble(String str) {
        str = str.trim().replaceAll(" ", "");
        if (str.length() == 0)
            return 1;
        Pair pair = getSignAndRemain(str, "a");
        double sign = pair.sign;
        if (pair.remain.length() == 0) return sign;
        return sign * Double.parseDouble(pair.remain);
    }

    private double parseBToDouble(String str) {
        str = str.trim().replaceAll(" ", "");
        if (str.length() == 0)
            throw new RuntimeException("Wrong B-coefficient");

        Pair pair = getSignAndRemain(str, "b");
        double sign = pair.sign;
        if (pair.remain.length() == 0) return sign;
        return sign * Double.parseDouble(pair.remain);
    }
    private double parseCToDouble(String str) {
        str = str.trim().replaceAll(" ", "");
        if (str.isBlank())
            return 0;
        return Double.parseDouble(str);
    }

    private void checkZero(String strZero) {
        if (!strZero.trim().equals("0"))
            throw new RuntimeException("Right to = must be 0");
    }

    // str.length must be > 0
    private Pair getSignAndRemain(String str, String coefficient) {
        char first = str.charAt(0);
        double sign = 1;
        String remain = str;
        if (first == '-') {
            sign = -1;
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
        private final double sign;
        private final String remain;

        public Pair(double sign, String remain) {
            this.sign = sign;
            this.remain = remain;
        }

        public double getSign() {
            return sign;
        }

        public String getRemain() {
            return remain;
        }
    }


    private static class Equation {
        private final double a;
        private final double b;
        private final double c;

        private final String token;

        public Equation(double a, double b, double c, String token) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.token = token;
        }

        public double getA() {
            return a;
        }

        public double getB() {
            return b;
        }

        public double getC() {
            return c;
        }

        public String getToken() {
            return token;
        }
    }
}
