package org.sodeja.functional;

public class Pair<P1, P2> {
	
	public final P1 p1;
	public final P2 p2;
	
	public Pair(P1 p1, P2 p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((p1 == null) ? 0 : p1.hashCode());
		result = PRIME * result + ((p2 == null) ? 0 : p2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pair other = (Pair) obj;
		if (p1 == null) {
			if (other.p1 != null)
				return false;
		} else if (!p1.equals(other.p1))
			return false;
		if (p2 == null) {
			if (other.p2 != null)
				return false;
		} else if (!p2.equals(other.p2))
			return false;
		return true;
	}
}
