package org.sodeja.act;

public interface Actor {
	public boolean receive(ActorId thisId, Object msg) throws Exception;
}
