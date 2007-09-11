package org.sodeja.event;

public interface EventDispatcher {
	public void addListener(EventListener<?> listener);
	public void removeListener(EventListener<?> listener);
}
