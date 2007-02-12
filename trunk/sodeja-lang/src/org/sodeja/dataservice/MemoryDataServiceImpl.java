package org.sodeja.dataservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sodeja.collections.ListUtils;
import org.sodeja.functional.Predicate1;

public class MemoryDataServiceImpl<T> implements DataService<T> {

	private List<T> data;
	private List<DataServiceListener<T>> listeners;
	
	public MemoryDataServiceImpl() {
		data = new ArrayList<T>();
		listeners = new ArrayList<DataServiceListener<T>>();
	}
	
	public void create(T exercise) {
		data.add(exercise);
		for(DataServiceListener<T> listner : listeners) {
			listner.created(this, exercise);
		}
	}
	
	public void update(T exercise) {
		for(DataServiceListener<T> listner : listeners) {
			listner.updated(this, exercise);
		}
	}

	public void delete(T exercise) {
		data.remove(exercise);
		for(DataServiceListener<T> listner : listeners) {
			listner.deleted(this, exercise);
		}
	}

	public List<T> find(Predicate1<T> filter) {
		return ListUtils.filter(data, filter);
	}

	public List<T> findAll() {
		return Collections.unmodifiableList(data);
	}

	public void addDataServiceListener(DataServiceListener<T> listener) {
		listeners.add(listener);
	}

	public void removeDataServiceListener(DataServiceListener<T> listener) {
		listeners.remove(listener);
	}
}
