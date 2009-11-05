package org.sodeja.act;

import org.sodeja.functional.Pair;

public class PingPong {
	private static class Ping implements Actor {
		@Override
		public boolean receive(ActorId thisId, Object msg) throws Exception {
			if(msg instanceof ActorId) {
				((ActorId) msg).send(Pair.of(5000, thisId));
				return true;
			}
			
			Pair<Integer, ActorId> rmsg = (Pair<Integer, ActorId>) msg;
			if(rmsg.first == 0) {
				rmsg.second.send("Finished");
				System.out.println("Ping finished");
				return false;
			} else {
				System.out.println("Ping recieved pong: " + rmsg.first);
				rmsg.second.send(Pair.of(rmsg.first - 1, thisId));
				return true;
			}
		}
	}
	
	private static class Pong implements Actor {
		@Override
		public boolean receive(ActorId thisId, Object msg) throws Exception {
			if(msg instanceof String) {
				System.out.println("Pong finished");
				return false;
			}
			
			Pair<Integer, ActorId> rmsg = (Pair<Integer, ActorId>) msg;
			System.out.println("Pong received ping: " + rmsg.first);
			rmsg.second.send(Pair.of(rmsg.first, thisId));
			return true;
		}
	}
	
	public static void main(String[] args) {
		ActorManager m = new ActorManager();
		ActorId pongId = m.spawn(new Pong());
		m.spawn(new Ping(), pongId);
		
		pongId = m.spawn(new Pong());
		m.spawn(new Ping(), pongId);
		
		pongId = m.spawn(new Pong());
		m.spawn(new Ping(), pongId);
		
		System.out.println(m);
	}
}
