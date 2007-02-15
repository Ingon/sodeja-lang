package org.sodeja.functional;

public class Triple<P1, P2, P3> extends Pair<P1, P2>{

	public final P3 p3;
	
	public Triple(P1 p1, P2 p2, P3 p3) {
		super(p1, p2);
		this.p3 = p3;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((p3 == null) ? 0 : p3.hashCode());
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
		if (p3 == null) {
			if (other.p3 != null)
				return false;
		} else if (!p3.equals(other.p3))
			return false;
		return true;
	}
}
