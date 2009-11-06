package org.sodeja.act;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ActorManager {
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final ExecutorService execs;
	
	private final Map<ActorId, Actor> actors;
	private final Map<ActorId, Set<ActorId>> links;
	
	public ActorManager() {
		execs = Executors.newCachedThreadPool();
		actors = new HashMap<ActorId, Actor>();
		links = new HashMap<ActorId, Set<ActorId>>();
	}
	
	public ActorId spawn(Actor a) {
		ActorId id = new ActorId(this);
		lock.writeLock().lock();
		actors.put(id, a);
		lock.writeLock().unlock();
		return id;
	}
	
	public ActorId spawn(Actor a, Object initMsg) {
		return spawn(a).send(initMsg);
	}

	protected void send(final ActorId actorId, final Object msg) {
		lock.readLock().lock();
		final Actor act = actors.get(actorId);
		lock.readLock().unlock();
		
		execs.execute(new Runnable() {
			@Override
			public void run() {
				try {
					boolean keepAlive = act.receive(actorId, msg); 
					if(! keepAlive) {
						clearId(actorId);
					}
				} catch (Exception e) {
					notifyFailure(actorId, e);
					clearId(actorId);
				}
			}
		});
	}

	private void clearId(ActorId actorId) {
		lock.writeLock().lock();
		actorId.stop();
		
		if(links.containsKey(actorId)) {
			Set<ActorId> refIds = links.remove(actorId); // Synchs
			for(ActorId refId : refIds) {
				links.get(refId).remove(actorId);
			}
		}
		
		actors.remove(actorId);
		
		if(actors.isEmpty()) { // TODO as setter
			execs.shutdownNow();
		}
		lock.writeLock().unlock();
	}
	
	public void link(ActorId actorId, ActorId otherId) {
		linkup(actorId, otherId);
		linkup(otherId, actorId);
	}
	
	private void linkup(ActorId from, ActorId to) {
		lock.writeLock().lock();
		Set<ActorId> ids = links.get(from);
		if(ids == null) {
			ids = new HashSet<ActorId>();
			links.put(from, ids);
		}
		ids.add(to);
		lock.writeLock().unlock();
	}

	private void notifyFailure(final ActorId actorId, Exception e) {
		lock.readLock().lock();
		if(links.containsKey(actorId)) {
			for (ActorId lid : links.get(actorId)) {
				send(lid, e);
			}
		}
		lock.readLock().unlock();
	}
}
