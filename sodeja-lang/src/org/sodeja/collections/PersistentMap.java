package org.sodeja.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	}
	
	private static class LevelPair<K, V> extends Pair<K, V> {
		protected LevelPair(K first, V second) {
			super(first, second);
		}
	}

	private final LevelInfo[] infos;
	private final LevelData root;

	public PersistentMap() {
		infos = new LevelInfo[8];
		for(int i = 0; i < infos.length; i++) {
			infos[i] = new LevelInfo(4, i * 4);
		}
		root = new LevelData(infos[0]);
	}

	private PersistentMap(LevelInfo[] infos, LevelData root) {
		this.infos = infos;
		this.root = root;
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
		int hashCode = key.hashCode();
		
		LevelData newRoot = new LevelData(infos[0]);
		
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
						break;
					}
				}
				nl.add(new LevelPair<K, V>(key, value));
				newCurr.data[index] = nl;
				break;
			}
		}
		
		return new PersistentMap<K, V>(this.infos, newRoot);
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
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<K> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}
}
