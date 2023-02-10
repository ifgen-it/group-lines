import java.math.BigDecimal;
import java.math.RoundingMode;

public class Main {
    public static void main(String[] args) {

        System.out.println("double:");
        double x1 = 2.55;
        double x2 = 0.01;
        double y1 = 255;
        double y2 = 1;
        double result1 = x1/x2;
        double result2 = y1/y2;
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result1 == result2);

        System.out.println("BigDecimal:");
        BigDecimal x1_bd = BigDecimal.valueOf(2.55);
        BigDecimal x2_bd = BigDecimal.valueOf(0.01);
        BigDecimal y1_bd = BigDecimal.valueOf(255);
        BigDecimal y2_bd = BigDecimal.valueOf(1);
        BigDecimal result1_bd = x1_bd.divide(x2_bd);
        BigDecimal result2_bd = y1_bd.divide(y2_bd);
        System.out.println(result1_bd);
        System.out.println(result2_bd);
        System.out.println(result1_bd.equals(result2_bd));

        /////////

     /*   BigDecimal a = new BigDecimal("0");
        BigDecimal a1 = new BigDecimal("-0");
        BigDecimal b = new BigDecimal("0.0");
        BigDecimal b2 = new BigDecimal("-0.0");
        System.out.println(a.equals(a1));
        System.out.println(a.equals(b));
        System.out.println(b.equals(b2));

        double aD = a.doubleValue();
        double bD = b.doubleValue();
        double ZERO = 0;
        System.out.println(aD == bD);
        System.out.println(aD == ZERO);
        System.out.println(bD == ZERO);*/


    }
}