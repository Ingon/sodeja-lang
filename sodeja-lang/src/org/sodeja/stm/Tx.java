package org.sodeja.stm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.sodeja.collections.PersistentSet;
import org.sodeja.lang.IDGenerator;

public class Tx {
	private final AtomicReference<Version> versionRef;
	private final ThreadLocal<TransactionInfo> state = new ThreadLocal<TransactionInfo>();
	private final IDGenerator idGen = new IDGenerator();
	private final ConcurrentLinkedQueue<TransactionInfo> order = new ConcurrentLinkedQueue<TransactionInfo>();
	private PersistentSet<TxListener> listners = new PersistentSet<TxListener>();
	
	public Tx() {
		this.versionRef = new AtomicReference<Version>(new Version(idGen.next(), new HashMap<TxVar<?>, Object>(), null));
	}
	
	public void addListner(TxListener l) {
		listners = listners.addValue(l);
	}

	public void begin(boolean readonly) {
		TransactionInfo newInfo = null;
		while(true) {
			Version version = versionRef.get();
			newInfo = new TransactionInfo(version, readonly);
			version.transactionInfoCount.incrementAndGet();
			if(versionRef.compareAndSet(version, version)) {
				break;
			}
			version.transactionInfoCount.decrementAndGet();
		}
		state.set(newInfo);
		order.offer(newInfo);
		return;
	}
	
	public void commit() {
		TransactionInfo info = state.get();
		if(info == null) {
			throw new RuntimeException("No transaction");
		}
		if(info.rolledback) {
			throw new RuntimeException("Already rolledback");
		}
		if(info.readonly) {
			clearInfo(info);
			return;
		}
		
		waitOtherTransactions(info);
		try {
			commitTransaction(info);
		} finally {
			clearInfo(info);
		}
	}
	
	private void waitOtherTransactions(TransactionInfo info) {
		while(order.peek() != info) { // if we are not the head, wait for it
			synchronized(info) { 
				try {
					info.wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace(); // Hmmm ? rollback?
				} 
			}
		}
	}
	
	private void commitTransaction(TransactionInfo info) {
		// Cheks?
		long verId = idGen.next();
		boolean result = versionRef.compareAndSet(info.version, new Version(verId, info.data, info.version));
		if(result) {
			notifyOnCommit();
			return;
		}
		
		Version ver = versionRef.get();
		if(hasConflictingChanges(ver, info)) { 
			rollback();
			throw new RuntimeException("Rolled back due conflicts");
		}
		
		Map<TxVar<?>, Object> relationInfo = merge(ver, info); 
		//Checks again?
		state.set(info);
		
		Version newVersion = new Version(verId, relationInfo, ver);
		result = versionRef.compareAndSet(ver, newVersion);
		if(! result) {
			throw new RuntimeException("WTF");
		}
		notifyOnCommit();
		newVersion.clearOld();
	}
	
	private void notifyOnCommit() {
		PersistentSet<TxListener> listners = this.listners;
		for(TxListener l : listners) {
			l.onCommit();
		}
	}

	private boolean hasConflictingChanges(Version ver, TransactionInfo info) {
		for(TxVar<?> v : info.data.keySet()) {
			Object orig = info.version.data.get(v);
			Object ov = ver.data.get(v);
			Object oi = info.data.get(v);
			if(oi != orig && ov != orig) {
				return true;
			}
		}
		return false;
	}

	private Map<TxVar<?>, Object> merge(Version ver, TransactionInfo info) {
		Map<TxVar<?>, Object> result = new HashMap<TxVar<?>, Object>(ver.data);
		for(Map.Entry<TxVar<?>, Object> e : info.data.entrySet()) {
			result.put(e.getKey(), e.getValue());
		}
		return result;
	}

	public void rollback() {
		TransactionInfo info = state.get();
		info.rolledback = true;
		clearInfo(info);
	}
	
	private void clearInfo(TransactionInfo info) {
		order.remove(info);
//		order.poll();
		state.remove();
		info.version.transactionInfoCount.decrementAndGet();
		
		TransactionInfo nextInfo = null;
		while((nextInfo = order.peek()) != null && nextInfo.rolledback) {
			order.poll();
		}
		if(nextInfo != null) {
			synchronized (nextInfo) {
				nextInfo.notifyAll();
			}
		}
	}
	
	protected Object get(TxVar<?> var) {
		ValuesDelta current = getInfo(false);
		return current.data.get(var);
	}
	
	protected void set(TxVar<?> var, Object nval) {
		ValuesDelta current = getInfo(true);
		current.data.put(var, nval);
	}

	private ValuesDelta getInfo(boolean withTransaction) {
		TransactionInfo transactionInfo = state.get();
		if(transactionInfo != null && transactionInfo.rolledback) {
			throw new RuntimeException("Transaction already rolledback");
		}
		if(withTransaction && transactionInfo == null) {
			throw new RuntimeException("Must be in transaction");
		}
		if(withTransaction && transactionInfo.readonly) {
			throw new RuntimeException("Must start write transaction");
		}
		return transactionInfo != null ? transactionInfo : versionRef.get();
	}
	
	private static abstract class ValuesDelta {
		protected final Map<TxVar<?>, Object> data;

		public ValuesDelta(Map<TxVar<?>, Object> data) {
			this.data = data;
		}
	}

	private static class TransactionInfo extends ValuesDelta {
		protected final Version version;
		protected final boolean readonly;
		protected boolean rolledback;
		
		public TransactionInfo(Version version, boolean readonly) {
			super(version.newData());
			this.version = version;
			this.readonly = readonly;
		}
		
		public TransactionInfo(Version version, Map<TxVar<?>, Object> data) {
			super(data);
			this.version = version;
			this.readonly = false;
		}
	}

	private static class Version extends ValuesDelta {
		protected final long id;
		protected final AtomicReference<Version> previousRef;
		protected final AtomicInteger transactionInfoCount = new AtomicInteger();
		
		public Version(long id, Map<TxVar<?>, Object> data, Version previous) {
			super(data);
			this.id = id;
			this.previousRef = new AtomicReference<Version>(previous);
		}
		
		public void clearOld() {
			Version previous = previousRef.get();
			if(previous != null) {
				previous.internalClear(this);
			}
		}

		private void internalClear(Version next) {
			Version previous = previousRef.get();
			if(previous != null) {
				previous.internalClear(this);
			}
			
			previous = previousRef.get();
			if(previous != null) {
				return;
			}
			
			if(transactionInfoCount.get() == 0) {
				next.previousRef.set(null);
			}
		}

		public Map<TxVar<?>, Object> newData() {
			return new HashMap<TxVar<?>, Object>(data);
		}

		@Override
		public String toString() {
			return "V" + id;
		}
	}
}
