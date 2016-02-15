package engine;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public abstract class Keys
{
	public static HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
	private static KeyEvent newEvent = null;
	
	public static KeyEvent getNewEvent()
	{
		return newEvent;
	}

	public static void setNewEvent(KeyEvent newEvent)
	{
		Keys.newEvent = newEvent;
	}

	public static void clear(Integer key)
	{
		Keys.keys.put(key, false);
	}
	
	public static void set(Integer key)
	{
		Keys.keys.put(key, true);
	}

	public static boolean check(Integer key)
	{
		return (Keys.keys.get(key) != null && Keys.keys.get(key));
	}
	
	public static boolean pull(Integer key)
	{
		boolean answer = (Keys.keys.get(key) != null && Keys.keys.get(key));
		Keys.clear(key);
		
		// if the requested key is a match, clear the keyevent also
		if (answer)
			Keys.newEvent = null;
		
		return answer;
	}
	
	public static KeyEvent pullKeyEvent()
	{
		if (Keys.hasKey())
		{
			KeyEvent e = newEvent;
			newEvent = null;
			Keys.clear(e.getKeyCode());
			return e;
		}
		
		return null;
	}
	
	public static boolean hasKey()
	{
		return (newEvent != null && Keys.check(newEvent.getKeyCode()));
	}
}
