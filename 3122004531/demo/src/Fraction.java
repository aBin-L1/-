public class Fraction {
    private int numerator; // 分子
    private int denominator; // 分母

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("分母不能为零");
        }
        int gcd = gcd(numerator, denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;

        if (this.denominator < 0) {
            this.numerator = -this.numerator;
            this.denominator = -this.denominator;
        }
    }

    public static Fraction fromString(String str) {
        if (str.contains("'")) {
            String[] parts = str.split("'");
            int wholeNumber = Integer.parseInt(parts[0]);
            String[] fractionParts = parts[1].split("/");
            int numerator = Integer.parseInt(fractionParts[0]);
            int denominator = Integer.parseInt(fractionParts[1]);
            return new Fraction(wholeNumber * denominator + numerator, denominator);
        } else if (str.contains("/")) {
            String[] parts = str.split("/");
            return new Fraction(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } else {
            return new Fraction(Integer.parseInt(str), 1);
        }
    }

    public Fraction add(Fraction other) {
        int commonDenominator = this.denominator * other.denominator;
        int newNumerator = this.numerator * other.denominator + other.numerator * this.denominator;
        return new Fraction(newNumerator, commonDenominator);
    }

    public Fraction subtract(Fraction other) {
        int commonDenominator = this.denominator * other.denominator;
        int newNumerator = this.numerator * other.denominator - other.numerator * this.denominator;
        return new Fraction(newNumerator, commonDenominator);
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(this.numerator * other.numerator, this.denominator * other.denominator);
    }

    public Fraction divide(Fraction other) {
        return new Fraction(this.numerator * other.denominator, this.denominator * other.numerator);
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    @Override
    public String toString() {
        if (numerator % denominator == 0) {
            return String.valueOf(numerator / denominator); // 整数形式
        } else if (Math.abs(numerator) > denominator) {
            int wholeNumber = numerator / denominator;
            int remainder = Math.abs(numerator % denominator);
            return wholeNumber + "'" + remainder + "/" + denominator; // 带分数形式
        } else {
            return numerator + "/" + denominator; // 真分数形式
        }
    }
}
