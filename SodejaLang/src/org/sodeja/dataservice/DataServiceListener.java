package org.sodeja.dataservice;

public interface DataServiceListener<T> {
	public void created(DataService<T> service, T data);
	public void updated(DataService<T> service, T data);
	public void deleted(DataService<T> service, T data);
}
