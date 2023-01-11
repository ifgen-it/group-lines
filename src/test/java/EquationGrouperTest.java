import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class EquationGrouperTest {

    @Test
    public void testPrecision() {
        EquationGrouper equationGrouper = new EquationGrouper();
        String t1 = "2.55x + 0.01y + 5 = 0";
        String t2 = "255x + y + 5 = 0";
        List<String> tokens = List.of(t1, t2);

        Set<Set<String>> expected = Set.of(Set.of(t1, t2));
        Set<Set<String>> actual = equationGrouper.groupByParallel(tokens);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testZeros_invalid_coefficients() {
        EquationGrouper equationGrouper = new EquationGrouper();
        String t1 = "1x + 0.1y + 5 = 0";
        String t2_failed = "2x + 0y + 5 = 0";
        String t3 = "0.1x - y = 0";
        String t4_failed = "-0x - 2y - 2 = 0";
        List<String> tokens = List.of(t1, t2_failed, t3, t4_failed);

        Set<Set<String>> expected = Set.of(Set.of(t1), Set.of(t3));
        Set<Set<String>> actual = equationGrouper.groupByParallel(tokens);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_ok_empty() {
        EquationGrouper equationGrouper = new EquationGrouper();
        List<String> tokens = Collections.emptyList();

        Set<Set<String>> expected = Collections.emptySet();
        Set<Set<String>> actual = equationGrouper.groupByParallel(tokens);

        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void test_ok() {
        EquationGrouper equationGrouper = new EquationGrouper();
        String t1 = "1x + 4y + 5 = 0";
        String t2 = "2x + 8y + 5 = 0";
        String t3 = "-3x - y = 0";
        String t4 = "-6x - 2y - 2 = 0";
        List<String> tokens = List.of(t1, t2, t3, t4);

        Set<Set<String>> expected = Set.of(Set.of(t1, t2), Set.of(t3, t4));
        Set<Set<String>> actual = equationGrouper.groupByParallel(tokens);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_ok_with_skip_failed_tokens() {
        EquationGrouper equationGrouper = new EquationGrouper();
        String t1 = "1X + 4y + 5 = 0";
        String t2_failed = "2x + 5 = 0";
        String t3 = "-3x - y = 0";
        String t4_failed = "-6x - 2Y - 2 = 8";
        List<String> tokens = List.of(t1, t2_failed, t3, t4_failed);

        Set<Set<String>> expected = Set.of(Set.of(t1), Set.of(t3));
        Set<Set<String>> actual = equationGrouper.groupByParallel(tokens);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_ok_with_skip_failed_tokens_2() {
        EquationGrouper equationGrouper = new EquationGrouper();
        String t1 = "1x + 4y + 5 = 0";
        String t2_failed = "2x + 5z -6 = 0";
        String t3 = "-3x - y = 0";
        String t4_failed = "-6x -- 2y -+ 2 = 0";
        List<String> tokens = List.of(t1, t2_failed, t3, t4_failed);

        Set<Set<String>> expected = Set.of(Set.of(t1), Set.of(t3));
        Set<Set<String>> actual = equationGrouper.groupByParallel(tokens);

        Assertions.assertEquals(expected, actual);
    }
}
