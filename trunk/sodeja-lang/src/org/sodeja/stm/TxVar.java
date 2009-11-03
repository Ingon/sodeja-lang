package org.sodeja.stm;

public class TxVar<T> {
	private final Tx tx;
	
	public TxVar(Tx tx) {
		this.tx = tx;
	}
	
	public TxVar(Tx tx, T initial) {
		this.tx = tx;
		set(initial);
	}
	
	public T get() {
		return (T) tx.get(this);
	}
	
	public void set(T t) {
		tx.set(this, t);
	}
}
