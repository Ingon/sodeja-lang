package org.sodeja.event;

public interface EventListener<T extends Event> {
	public void onEvent(T event);
	
	public Class<T> getClassT();
}
