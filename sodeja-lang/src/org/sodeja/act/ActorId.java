package org.sodeja.act;

public class ActorId {
	private final ActorManager manager;
	private boolean running = true;
	
	protected ActorId(ActorManager manager) {
		this.manager = manager;
	}
	
	public void send(Object msg) {
		if(running) {
			manager.send(this, msg);
		}
	}
	
	public void link(ActorId otherId) {
		if(running) {
			manager.link(this, otherId);
		}
	}

	protected void stop() {
		running = false;
	}
}
