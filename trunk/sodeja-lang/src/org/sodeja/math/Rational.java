package org.sodeja.math;

import java.math.BigInteger;

import org.sodeja.functional.Pair;

public class Rational extends Number {

	private static final long serialVersionUID = -8750573005378678423L;

	public static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);

	public static final Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);
	
	private final BigInteger divident;
	private final BigInteger divisor;
	
	public Rational(long value) {
		this(BigInteger.valueOf(value));
	}

	public Rational(BigInteger divident) {
		this(divident, BigInteger.ONE);
	}	
	
	public Rational(BigInteger divident, BigInteger divisor) {
		Pair<BigInteger, BigInteger> simplified = simplify(divident, divisor);
		this.divident = simplified.first;
		this.divisor = simplified.second;
	}

	public Rational(String value) {
		// TODO add parsing of E[+/-]N syntax
		if(value.contains(".")) {
			Pair<BigInteger, BigInteger> fractional = parseFractional(value);
			this.divident = fractional.first;
			this.divisor = fractional.second;
			return;
		}
		
		if(value.contains("/")) {
			Pair<BigInteger, BigInteger> rational = parseRational(value);
			this.divident = rational.first;
			this.divisor = rational.second;
			return;
		}
		
		this.divident = new BigInteger(value);
		this.divisor = BigInteger.ONE;
		return;
	}
	
	private static Pair<BigInteger, BigInteger> parseFractional(String value) {
		String[] parts = value.split("\\.");
		if(parts.length != 2) {
			throw new NumberFormatException("Wrong rational value");
		}
		
		BigInteger whole = new BigInteger(parts[0]);
		BigInteger fract = new BigInteger(parts[1]);
		if(fract.equals(BigInteger.ZERO)) {
			return Pair.of(whole, BigInteger.ONE);
		}
		
		int exponent = parts[1].length();
		
		BigInteger tents = BigInteger.TEN;
		for(int i = 1;i < exponent;i++) {
			tents = tents.multiply(BigInteger.TEN);
		}
		
		return simplify(whole.multiply(tents).add(fract), tents);
	}
	
	private static Pair<BigInteger, BigInteger> parseRational(String value) {
		String[] parts = value.split("/");
		if(parts.length != 2) {
			throw new NumberFormatException("Wrong rational value");
		}
		
		BigInteger divident = new BigInteger(parts[0]);
		BigInteger divisor = new BigInteger(parts[1]);
		
		return simplify(divident, divisor);
	}
	
	private static Pair<BigInteger, BigInteger> simplify(BigInteger divident, BigInteger divisor) {
		BigInteger gcd = divident.gcd(divisor);
		return Pair.of(divident.divide(gcd), divisor.divide(gcd));
	}
	
	public Rational add(Rational other) {
		BigInteger firstDivident = this.divident.multiply(other.divisor);
		BigInteger secondDivident = other.divident.multiply(this.divisor);
		return new Rational(firstDivident.add(secondDivident), this.divisor.multiply(other.divisor));
	}
	
	public Rational substract(Rational other) {
		BigInteger firstDivident = this.divident.multiply(other.divisor);
		BigInteger secondDivident = other.divident.multiply(this.divisor);
		return new Rational(firstDivident.subtract(secondDivident), this.divisor.multiply(other.divisor));
	}
	
	public Rational multiply(Rational other) {
		return new Rational(this.divident.multiply(other.divident), this.divisor.multiply(other.divisor));
	}

	public Rational divide(Rational other) {
		return new Rational(this.divident.multiply(other.divisor), this.divisor.multiply(other.divident));
	}
	
	public Rational negate() {
		return new Rational(this.divident.negate(), this.divisor);
	}
	
	public Rational sqrt() {
		double valDivident = divident.doubleValue();
		double valDivisor = divisor.doubleValue();
		
		double sqDivident = Math.sqrt(valDivident);
		double sqDivisor = Math.sqrt(valDivisor);
		
		// FIXME maybe add a constructor?!?!?
		Rational ratDivident = new Rational(String.valueOf(sqDivident));
		Rational ratDivisor = new Rational(String.valueOf(sqDivisor));
		
		return ratDivident.divide(ratDivisor);
	}

	public int compareTo(Rational other) {
		if(this.divisor.equals(other.divisor)) {
			return this.divident.compareTo(other.divident);
		}
		BigInteger first = this.divident.multiply(other.divisor);
		BigInteger second = other.divident.multiply(this.divisor);
		return first.compareTo(second);
	}

	@Override
	public double doubleValue() {
		return divident.doubleValue() / divisor.doubleValue();
	}

	@Override
	public float floatValue() {
		return divident.floatValue() / divisor.floatValue();
	}

	@Override
	public int intValue() {
		return divident.intValue() / divisor.intValue();
	}

	@Override
	public long longValue() {
		return divident.longValue() / divisor.longValue();
	}

	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof Rational)) {
			return false;
		}
		
		Rational other = (Rational) obj;
		return this.divident.equals(other.divident) && this.divisor.equals(other.divisor);
	}

	@Override
	public int hashCode() {
		return divident.hashCode() + divisor.hashCode();
	}

	@Override
	public String toString() {
		return String.valueOf(doubleValue());
//		return divident.toString() + "/" + divisor.toString();
	}
}
