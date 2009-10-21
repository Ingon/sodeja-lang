package org.sodeja.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.sodeja.functional.Pair;

public class PersistentMap<K, V> implements Map<K, V> {
	
	private static class LevelInfo {
		private final int maskSize;
		private final int pushSize;
		private final int mask;
		public final int dataSize;
		
		public LevelInfo(int maskSize, int pushSize) {
			this.maskSize = maskSize;
			this.pushSize = pushSize;
			
			int maskResolve = 0;
			int tempSize = 1;
			for(int i = 0; i < maskSize; i++) {
				maskResolve <<= 1;
				maskResolve |= 1;
				tempSize *= 2;
			}
			this.mask = maskResolve << pushSize;
			this.dataSize = tempSize;
		}
		
		public int resolve(int hashCode) {
			int temp = mask & hashCode;
			return temp >>> pushSize;
		}
	}
	
	private static class LevelData {
		public final Object[] data;
		
		public LevelData(LevelInfo info) {
			data = new Object[info.dataSize];
		}
		
		public boolean isEmpty() {
			for(int i = 0; i < data.length; i++) {
				if(data[i] != null) {
					return false;
				}
			}
			
			return true;
		}
	}
	
	private static class LevelPair<K, V> extends Pair<K, V> implements Map.Entry<K, V> {
		protected LevelPair(K first, V second) {
			super(first, second);
		}

		@Override
		public K getKey() {
			return first;
		}

		@Override
		public V getValue() {
			return second;
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}
	}

	private final LevelInfo[] infos;
	private final LevelData root;
	private final int size;

	public PersistentMap() {
		infos = new LevelInfo[8];
		for(int i = 0; i < infos.length; i++) {
			infos[i] = new LevelInfo(4, i * 4);
		}
		root = new LevelData(infos[0]);
		size = 0;
	}

	private PersistentMap(LevelInfo[] infos, LevelData root, int size) {
		this.infos = infos;
		this.root = root;
		this.size = size;
	}
	
	////////// Base
	@Override
	public V get(Object key) {
		int hashCode = key.hashCode();
		
		LevelData curr = root;
		for(int i = 0; i < infos.length; i++) {
			Object next = curr.data[infos[i].resolve(hashCode)];
			if(next == null) {
				return null;
			} else if(next instanceof LevelData) {
				curr = (LevelData) next;
			} else if(next instanceof LevelPair){
				LevelPair<K, V> e = (LevelPair<K, V>) next;
				if(e.first == key || e.first.equals(key)) {
					return e.second;
				} else {
					return null;
				}
			} else {
				LevelPair<K, V>[] l = (LevelPair[]) next;
				for(int m = 0; m < l.length; m++) {
					if(l[m].first == key || l[m].first.equals(key)) {
						return l[m].second;
					}
				}
				return null;
			}
		}
		
		return null; // sure?
	}
	
	public PersistentMap<K, V> putValue(K key, V value) {
		if(value == null) {
			return this;
		}
		
		int hashCode = key.hashCode();
		
		LevelData newRoot = new LevelData(infos[0]);
		int newSize = size + 1;
		
		LevelData oldCurr = root;
		LevelData newCurr = newRoot;
		for(int i = 0; i < infos.length; i++) {
			LevelInfo info = infos[i];
			int index = info.resolve(hashCode);
			copyToNew(oldCurr, newCurr);
			Object oldData = oldCurr.data[index];
			if(oldData == null) {
				newCurr.data[index] = new LevelPair<K, V>(key, value);
				break;
			} else if(oldData instanceof LevelData) {
				oldCurr = (LevelData) oldData;
				LevelData nextData = new LevelData(infos[i + 1]);
				newCurr.data[index] = nextData;
				newCurr = nextData;
			} else if(oldData instanceof LevelPair){
				LevelPair<K, V> p = (LevelPair<K, V>) oldData;
				if(p.first == key || p.first.equals(key)) {
					newCurr.data[index] = new LevelPair<K, V>(key, value);;
					newSize--;
					break;
				}
				
				newCurr.data[index] = createChildren(i + 1, p, new LevelPair<K, V>(key, value));
				break;
			} else {
				LevelPair<K, V>[] l = (LevelPair[]) oldData;
				LevelPair<K, V>[] nl = l;
				
				for(int m = 0; m < l.length; m++) {
					if(l[m].first == key || l[m].first.equals(key)) {
						nl = new LevelPair[l.length];
						System.arraycopy(l, 0, nl, 0, l.length);
						nl[m] = new LevelPair<K, V>(key, value);
						newSize--;
						break;
					}
				}
				
				if(nl == l) { // did not override any old 
					nl = new LevelPair[l.length + 1];
					System.arraycopy(l, 0, nl, 0, l.length);
					nl[l.length] = new LevelPair<K, V>(key, value);
				}
				
				newCurr.data[index] = nl;
				break;
			}
		}
		
		return new PersistentMap<K, V>(this.infos, newRoot, newSize);
	}
	
	private Object createChildren(int infoIndex, LevelPair<K, V> oldPair, LevelPair<K, V> newPair) {
		if(infoIndex >= infos.length) {
			LevelPair<K, V>[] nl = new LevelPair[2];
			nl[0] = oldPair;
			nl[1] = newPair;
			return nl;
		}
		
		LevelInfo currentInfo = infos[infoIndex];
		LevelData data = new LevelData(currentInfo);
		
		int oldHashCode = oldPair.first.hashCode();
		int oldIndex = currentInfo.resolve(oldHashCode);
		
		int newHashCode = newPair.first.hashCode();
		int newIndex = currentInfo.resolve(newHashCode);
		
		if(oldIndex == newIndex) {
			data.data[oldIndex] = createChildren(infoIndex + 1, oldPair, newPair);
		} else {
			data.data[oldIndex] = oldPair;
			data.data[newIndex] = newPair;
		}
		return data;
	}
	
	private void copyToNew(LevelData oldData, LevelData newData) {
		System.arraycopy(oldData.data, 0, newData.data, 0, oldData.data.length);
	}
	
	public PersistentMap<K, V> removeValue(K key) {
		int hashCode = key.hashCode();
		
		LevelData newRoot = new LevelData(infos[0]);
		int newSize = size - 1;
		
		LevelData oldCurr = root;
		LevelData newCurr = newRoot;
		for(int i = 0; i < infos.length; i++) {
			LevelInfo info = infos[i];
			int index = info.resolve(hashCode);
			copyToNew(oldCurr, newCurr);
			Object oldData = oldCurr.data[index];
			if(oldData == null) {
				newSize++;
				break;
			} else if(oldData instanceof LevelData) {
				oldCurr = (LevelData) oldData;
				LevelData nextData = new LevelData(infos[i + 1]);
				newCurr.data[index] = nextData;
				newCurr = nextData;
			} else if(oldData instanceof LevelPair){
				LevelPair<K, V> p = (LevelPair<K, V>) oldData;
				if(p.first == key || p.first.equals(key)) {
					newCurr.data[index] = null;
				} else {
					newSize++;
				}
				break;
			} else {
				LevelPair<K, V>[] l = (LevelPair[]) oldData;
				LevelPair<K, V>[] nl = l;
				newSize++;
				
				for(int m = 0; m < l.length; m++) {
					if(l[m].first == key || l[m].first.equals(key)) {
						nl = new LevelPair[l.length - 1];
						System.arraycopy(l, 0, nl, 0, m);
						System.arraycopy(l, m + 1, nl, m, l.length - m);
						newSize--;
						break;
					}
				}
				
				newCurr.data[index] = nl;
				break;
			}
		}
		
		return new PersistentMap<K, V>(this.infos, newRoot, newSize);
	}

	////////// Mutators 
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}
	
	private static class IteratorStatePart {
		public LevelData data;
		public int index;
	}
	
	////////// Views
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new AbstractSet<Entry<K, V>>() {
			@Override
			public Iterator<Entry<K, V>> iterator() {
				return new Iterator<Entry<K,V>>() {
					private final IteratorStatePart[] state = new IteratorStatePart[infos.length];
					private int stateIndex;
					
					{
						for(int i = 0;i < state.length; i++) {
							state[i] = new IteratorStatePart();
						}
						stateIndex = 0;
						state[stateIndex].data = root;
						state[stateIndex].index = -1;
					}
					private int listIndex = -1;
					private Entry<K, V> current;
					
					@Override
					public boolean hasNext() {
						if(listIndex >= 0) {
							IteratorStatePart current = state[stateIndex];
							LevelPair<K, V>[] l = (LevelPair[]) current.data.data[current.index];
							int nextListIndex = listIndex + 1;
							if(nextListIndex < l.length) {
								listIndex = nextListIndex;
								this.current = l[listIndex];
								return true;
							}
							listIndex = -1;
						}
						
						OUTER: while(stateIndex >= 0) {
							IteratorStatePart current = state[stateIndex--];
							int nextIndex = current.index + 1;
							int infoIndex = stateIndex + 1;
							
							LevelInfo info = infos[infoIndex];
							if(nextIndex >= info.dataSize) {
								continue;
							}
							for(int i = nextIndex; i < info.dataSize; i++) {
								Object cdata = current.data.data[i];
								if(cdata == null) {
									continue;
								} else if(cdata instanceof LevelData) {
									stateIndex++;
									this.state[stateIndex].index = i;
									stateIndex++;
									this.state[stateIndex].data = (LevelData) cdata;
									this.state[stateIndex].index = -1;
									continue OUTER;
								} else if(cdata instanceof LevelPair) {
									stateIndex++;
									this.state[stateIndex].index = i;
									this.current = ((LevelPair<K, V>) cdata);
									return true;
								} else {
									stateIndex++;
									this.state[stateIndex].data = (LevelData) cdata;
									this.state[stateIndex].index = i;
									listIndex = 0;
									LevelPair<K, V>[] l = (LevelPair[]) cdata;
									this.current = l[listIndex];
									return true;
								}
							}
						}
						
						return false;
					}

					@Override
					public java.util.Map.Entry<K, V> next() {
						if(current == null) {
							throw new NoSuchElementException();
						}
						Entry<K, V> tmp = current;
						current = null;
						return tmp;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			@Override
			public int size() {
				return size;
			}
		};
	}

	@Override
	public Set<K> keySet() {
		return new AbstractSet<K>() {
			@Override
			public Iterator<K> iterator() {
				return new Iterator<K>() {
					Iterator<Entry<K, V>> delegate = entrySet().iterator();
					@Override
					public boolean hasNext() {
						return delegate.hasNext();
					}

					@Override
					public K next() {
						return delegate.next().getKey();
					}

					@Override
					public void remove() {
						delegate.remove();
					}
				};
			}

			@Override
			public int size() {
				return size;
			}
		};
	}

	@Override
	public Collection<V> values() {
		return new AbstractSet<V>() {
			@Override
			public Iterator<V> iterator() {
				return new Iterator<V>() {
					Iterator<Entry<K, V>> delegate = entrySet().iterator();
					@Override
					public boolean hasNext() {
						return delegate.hasNext();
					}

					@Override
					public V next() {
						return delegate.next().getValue();
					}

					@Override
					public void remove() {
						delegate.remove();
					}
				};
			}

			@Override
			public int size() {
				return size;
			}
		};
	}
	
	////////// Contains
	@Override
	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	////////// Sizes
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public int size() {
		return size;
	}

	// TODO extend AbstractMap ?
	@Override
	public String toString() {
		Iterator<Entry<K, V>> i = entrySet().iterator();
		if (!i.hasNext())
			return "{}";

		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (;;) {
			Entry<K, V> e = i.next();
			K key = e.getKey();
			V value = e.getValue();
			sb.append(key == this ? "(this Map)" : key);
			sb.append('=');
			sb.append(value == this ? "(this Map)" : value);
			if (!i.hasNext())
				return sb.append('}').toString();
			sb.append(", ");
		}
	}
}
