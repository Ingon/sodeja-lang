package org.sodeja.collections;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.sodeja.functional.Pair;

public class PersistentMap<K, V> implements Map<K, V> {
	
	private static class LevelInfo {
		private final int size;
		private final int pushSize;
		private final int mask;
		private final int dataSize;
		
		public LevelInfo(int size, int pushSize) {
			this.size = size;
			this.pushSize = pushSize;
			
			int maskResolve = 0;
			int tempSize = 1;
			for(int i = 0; i < size; i++) {
				maskResolve <<= 1;
				maskResolve |= 1;
				tempSize *= 2;
			}
			this.mask = maskResolve << pushSize;
			this.dataSize = tempSize;
		}
		
		public int resolve(int hashCode) {
			int temp = mask & hashCode;
			return temp >> pushSize;
		}

		public int size() {
			return dataSize;
		}
	}
	
	private static class LevelData {
		private final Object[] data;
		
		public LevelData(LevelInfo info) {
			data = new Object[info.size()];
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
	
	private static class LevelPair<K, V> extends Pair<K, V> {
		protected LevelPair(K first, V second) {
			super(first, second);
		}
		
		protected Entry<K, V> toEntry() {
			return new Map.Entry<K, V>() {
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
			};
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
				if(e.first.equals(key)) {
					return e.second;
				} else {
					return null;
				}
			} else {
				List<LevelPair<K, V>> l = (List<LevelPair<K, V>>) next;
				for(LevelPair<K, V> e : l) {
					if(e.first.equals(key)) {
						return e.second;
					} else {
						return null;
					}
				}
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
			for(int j = 0; j < oldCurr.data.length; j++) {
				if(j == index) {
					continue;
				}
				newCurr.data[j] = oldCurr.data[j];
			}
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
				if(p.first.equals(key)) {
					newCurr.data[index] = new LevelPair<K, V>(key, value);;
					newSize--;
					break;
				}
				
				newCurr.data[index] = createChildren(i + 1, p, new LevelPair<K, V>(key, value));
				break;
			} else {
				List<LevelPair<K, V>> l = (List<LevelPair<K, V>>) oldData;
				List<LevelPair<K, V>> nl = new ArrayList<LevelPair<K,V>>(l);
				for(Iterator<LevelPair<K, V>> ite = nl.iterator(); ite.hasNext(); ) {
					LevelPair<K, V> p = ite.next();
					if(p.first.equals(key)) {
						ite.remove();
						newSize--;
						break;
					}
				}
				nl.add(new LevelPair<K, V>(key, value));
				newCurr.data[index] = nl;
				break;
			}
		}
		
		return new PersistentMap<K, V>(this.infos, newRoot, newSize);
	}
	
	private Object createChildren(int infoIndex, LevelPair<K, V> oldPair, LevelPair<K, V> newPair) {
		if(infoIndex >= infos.length) {
			List<LevelPair<K, V>> nl = new ArrayList<LevelPair<K,V>>();
			nl.add(oldPair);
			nl.add(newPair);
			return nl;
		}
		
		LevelInfo currentInfo = infos[infoIndex];
		LevelData data = new LevelData(currentInfo);
		
		int oldIndex = currentInfo.resolve(oldPair.first.hashCode());
		int newIndex = currentInfo.resolve(newPair.first.hashCode());
		
		if(oldIndex == newIndex) {
			data.data[oldIndex] = createChildren(infoIndex + 1, oldPair, newPair);
		} else {
			data.data[oldIndex] = oldPair;
			data.data[newIndex] = newPair;
		}
		return data;
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
			for(int j = 0; j < oldCurr.data.length; j++) {
				if(j == index) {
					continue;
				}
				newCurr.data[j] = oldCurr.data[j];
			}
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
				if(p.first.equals(key)) {
					newCurr.data[index] = null;
				} else {
					newSize++;
				}
				break;
			} else {
				List<LevelPair<K, V>> l = (List<LevelPair<K, V>>) oldData;
				List<LevelPair<K, V>> nl = new ArrayList<LevelPair<K,V>>(l);
				newSize++;
				for(Iterator<LevelPair<K, V>> ite = nl.iterator(); ite.hasNext(); ) {
					LevelPair<K, V> p = ite.next();
					if(p.first.equals(key)) {
						ite.remove();
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
	
	////////// Views
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new AbstractSet<Entry<K, V>>() {
			@Override
			public Iterator<Entry<K, V>> iterator() {
				return new Iterator<Entry<K,V>>() {
					private Deque<Pair<LevelData, Integer>> data;
					private int listIndex = -1;
					
					private Entry<K, V> current;
					
					{
						data = new LinkedList<Pair<LevelData,Integer>>();
						data.offerFirst(Pair.of(root, -1));
					}
					
					@Override
					public boolean hasNext() {
						if(listIndex >= 0) {
							Pair<LevelData,Integer> current = this.data.peekLast();
							List<LevelPair<K, V>> l = (List<LevelPair<K,V>>) current.first.data[current.second];
							int nextListIndex = listIndex + 1;
							if(nextListIndex < l.size()) {
								listIndex = nextListIndex;
								this.current = l.get(listIndex).toEntry();
								return true;
							}
							listIndex = -1;
						}
						
						OUTER: while(! data.isEmpty()) {
							Pair<LevelData,Integer> current = this.data.pollLast();
							int nextIndex = current.second + 1;
							int infoIndex = this.data.size();
							LevelInfo info = infos[infoIndex];
							if(nextIndex >= info.size()) {
								continue;
							}
							for(int i = nextIndex; i < info.size(); i++) {
								Object cdata = current.first.data[i];
								if(cdata == null) {
									continue;
								} else if(cdata instanceof LevelData) {
									this.data.offerLast(Pair.of(current.first, i));
									this.data.offerLast(Pair.of((LevelData) cdata, -1));
									continue OUTER;
								} else if(cdata instanceof LevelPair) {
									this.data.offerLast(Pair.of(current.first, i));
									this.current = ((LevelPair<K, V>) cdata).toEntry();
									return true;
								} else {
									this.data.offerLast(Pair.of(current.first, i));
									listIndex = 0;
									List<LevelPair<K, V>> l = (List<LevelPair<K,V>>) cdata;
									this.current = l.get(listIndex).toEntry();
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
		throw new UnsupportedOperationException();
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
}
