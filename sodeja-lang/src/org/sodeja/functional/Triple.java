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
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Triple other = (Triple) obj;
		if (third == null) {
			if (other.third != null)
				return false;
		} else if (!third.equals(other.third))
			return false;
		return true;
	}
}
