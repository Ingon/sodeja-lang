package org.sodeja.functional;

public class Triple<First, Second, Third> extends Pair<First, Second>{

	public final Third third;
	
	public Triple(First first, Second second, Third third) {
		super(first, second);
		this.third = third;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((third == null) ? 0 : third.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
	   	return obj instanceof Triple 
	   		&& equals(first, ((Triple<?, ?, ?>) obj).first) 
	   		&& equals(second, ((Triple<?, ?, ?>) obj).second)
	   		&& equals(third, ((Triple<?, ?, ?>) obj).third);
	}	
	@Override
	public String toString() {
		return "(" + first + ", " + second + ", " + third + ")";
	}

	public static <F, S, T> Triple<F, S, T> of(F first, S second, T third) {
		return new Triple<F, S, T>(first, second, third);
	}
}
