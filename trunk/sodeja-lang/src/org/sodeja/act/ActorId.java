package org.sodeja.act;

public class ActorId {
	private final ActorManager manager;
	private boolean running = true;
	
	protected ActorId(ActorManager manager) {
		this.manager = manager;
	}
	
	public ActorId send(Object msg) {
		if(running) {
			manager.send(this, msg);
		} else {
			throw new RuntimeException("Actor is already death");
		}
		return this;
	}
	
	public void link(ActorId otherId) {
		if(running) {
			manager.link(this, otherId);
		} else {
			throw new RuntimeException("Actor is already death");
		}
	}

	protected void stop() {
		running = false;
	}
}
