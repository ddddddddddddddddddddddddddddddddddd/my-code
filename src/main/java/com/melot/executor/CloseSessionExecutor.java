package com.melot.executor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

public class CloseSessionExecutor extends Thread implements Executor{

	private static ConcurrentHashMap<Long,List<Object>> sessionMap = new ConcurrentHashMap<Long,List<Object>>();
	
	@Override
	public void execute() {
		this.start();
	}

	public void run(){
		while(true){
			try {
				Iterator<Long> it = sessionMap.keySet().iterator();
				Long now = System.currentTimeMillis();
				while(it.hasNext()){
					Long time = it.next();
					if(time <= now){
						List<Object> list = sessionMap.remove(time);
						Integer roomId = Integer.valueOf(list.get(0).toString());
						Session session = (Session)list.get(1);
						session.close();
						ReapExecutor.removeUserByRoomId(roomId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void putSession(Long time, Integer roomId, Session session){
		List<Object> list = new ArrayList<Object>(2);
		list.add(roomId);
		list.add(session);
		sessionMap.put(time, list);
	}
}
