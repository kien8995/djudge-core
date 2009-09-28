package djudge.acmcontester.server;

import java.util.LinkedList;
import java.util.Queue;

public class SyncronizedQueue<T>
{
	
	private Queue<T> queue = new LinkedList<T>();
	
	public SyncronizedQueue()
	{
		// TODO Auto-generated constructor stub
	}
	
	synchronized public boolean  add(T value)
	{
		return queue.offer(value);
	}
	
	synchronized public T get()
	{
		return queue.poll();
	}
}
