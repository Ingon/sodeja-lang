package org.sodeja.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sodeja.functional.Pair;

public class PersistentMap<K, V> implements Map<K, V> {
	
	private static class LevelInfo {

		public int resolve(int hashCode) {
			// TODO Auto-generated method stub
			return 0;
		}

		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
	
	private static class LevelData {
		private Object[] data;
		
		public LevelData(LevelInfo info) {
			data = new Object[info.size()];
		}
	}
	
	private static class LevelPair<K, V> extends Pair<K, V> {
		protected LevelPair(K first, V second) {
			super(first, second);
		}
	}

	private LevelInfo[] infos;
	private LevelData root;

	public PersistentMap() {
		root = new LevelData(infos[0]);
	}

	private PersistentMap(LevelData root) {
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
			int index = infos[i].resolve(hashCode);
			for(int j = 0; j < oldCurr.data.length; i++) {
				if(j == index) {
					continue;
				}
				newCurr.data[j] = oldCurr.data[j];
			}
			Object oldData = oldCurr.data[index];
			if(oldData == null) {
			} else if(oldData instanceof LevelData) {
				oldCurr = (LevelData) oldData;
				newCurr = new LevelData(infos[i + 1]);
			} else if(oldData instanceof LevelPair){
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
				break;
			}
		}
		
		return new PersistentMap<K, V>(newRoot);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}
	
	////////// Contains
	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	////////// Sizes
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
}
