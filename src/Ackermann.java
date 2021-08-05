import java.math.BigInteger;

import static java.math.BigInteger.ONE;

public class Ackermann {
    public static final BigInteger TWO = ONE.add(ONE);
    public static final BigInteger THREE = ONE.add(TWO);
    public static final BigInteger MAX_INT = new BigInteger("" + Integer.MAX_VALUE);
    public static void main(String[] args) {
        //System.out.println(ackermann(TWO.add(TWO), THREE).toString());
        System.out.println(TWO.pow(Integer.MAX_VALUE-1).toString());
    }

    public static BigInteger ackermann(BigInteger x, BigInteger y) {
        if (x.equals(BigInteger.ZERO)) return (y.add(ONE));
        else if (x.equals(ONE)) return (y.add(TWO));
        else if (x.equals(TWO)) return (y.multiply(TWO).add(THREE));
        else if (x.equals(THREE) && y.compareTo(MAX_INT) < 1) return (TWO.pow(y.add(THREE).intValue()).subtract(THREE));
        else if (y.equals(BigInteger.ZERO)) return (ackermann(x.subtract(ONE), ONE));
        else return (ackermann(x.subtract(ONE), ackermann(x, y.subtract(ONE))));
    }
}
