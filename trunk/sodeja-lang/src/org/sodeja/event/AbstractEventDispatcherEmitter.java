package org.sodeja.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEventDispatcherEmitter implements EventDispatcher, EventEmitter {

	private List<EventListener<?>> listeners;
	
	public AbstractEventDispatcherEmitter() {
		listeners = new ArrayList<EventListener<?>>();
	}
	
	@Override
	public void addListener(EventListener<?> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(EventListener<?> listener) {
		listeners.remove(listener);
	}

	protected void dispatch(Event event) {
		List<EventListener<?>> listenersCopy = new ArrayList<EventListener<?>>(listeners);
		for(EventListener<?> listener : listenersCopy) {
			dispatch(event, listener);
		}
	}

	@SuppressWarnings("unchecked")
	protected void dispatch(Event event, EventListener<?> listener) {
		if(listener.getClass().equals(EventListener.class)) {
			((EventListener) listener).onEvent(event);
		}

		if(event.getClass().equals(listener.getClassT())) {
        	((EventListener) listener).onEvent(event);
			return;
		}
		
        ParameterizedType parameterizedType = ((ParameterizedType) listener.getClass().getGenericInterfaces()[0]);
        Type type = parameterizedType.getActualTypeArguments()[0];
        if(! (type instanceof Class)) {
        	throw new IllegalArgumentException();
        }
        
        Class listenerParamerClass = (Class) type; 
        if(listenerParamerClass.equals(event.getClass())) {
        	((EventListener) listener).onEvent(event);
        	return;
        }
        
		throw new UnsupportedOperationException();
	}
}
