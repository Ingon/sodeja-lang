package org.sodeja.dataservice;

import java.util.List;

import org.sodeja.functional.Predicate1;

public interface DataService<T> {
	public void create(T data);
	public void update(T data);
	public void delete(T data);
	
	public List<T> findAll();
	public List<T> find(Predicate1<T> filter);
	
	public void addDataServiceListener(DataServiceListener<T> listener);
	public void removeDataServiceListener(DataServiceListener<T> listener);
}
