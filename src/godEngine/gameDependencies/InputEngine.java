package godEngine.gameDependencies;


import godEngine.gameContent.God;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

public class InputEngine implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	private HashMap<Integer, Boolean> keyStates = null;
	private GodMouseInfo mouseInfo 				= null;
	private int inputMethod						= 0;
	
	public InputEngine(int inputMethod) 
	{
		this.inputMethod = inputMethod;
		keyStates = new HashMap<Integer, Boolean>();
		mouseInfo = new GodMouseInfo();
	}
	public void initialize(GamePanel panel, God god)
	{
		if((inputMethod & Game.KEYBOARD_INPUT) != 0)
			panel.addKeyListener		(this);

		if((inputMethod & Game.MOUSE_INPUT) != 0)
		{
			panel.addMouseListener		(this);
			panel.addMouseMotionListener(this);
		}
		if((inputMethod & Game.MOUSE_WHEEL_INPUT) != 0)
			panel.addMouseWheelListener	(this);	
	}
	
	
	public void mouseDragged(MouseEvent arg0)
	{
		mouseMoved(arg0);
	}
	public void mouseMoved(MouseEvent arg0)
	{
		mouseInfo.setX(arg0.getX());
		mouseInfo.setY(arg0.getY());
	}
	public void mouseClicked(MouseEvent arg0){ /* currently unused */ }
	public void mouseEntered(MouseEvent arg0)
	{
		mouseInfo.setMouseOnScreen(true);
	}
	public void mouseExited(MouseEvent arg0) 
	{
		mouseInfo.setMouseOnScreen(false);
	}
	public void mousePressed(MouseEvent arg0) 
	{
		mouseInfo.setButtonPressed(arg0.getButton(), true);
	}
	public void mouseReleased(MouseEvent arg0) 
	{
		mouseInfo.setButtonPressed(arg0.getButton(), false);
	}
	public void mouseWheelMoved(MouseWheelEvent arg0) 
	{
		mouseInfo.addMouseWheelPosition(arg0.getPreciseWheelRotation());
	}
	
	public void keyPressed(KeyEvent arg0) {
		keyStates.put(arg0.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent arg0) {
		keyStates.put(arg0.getKeyCode(), false);
	}
	public void keyTyped(KeyEvent arg0) { /* Currently unused */ }
	
	public boolean isKeyDown(int keyCode)
	{
		if(!keyStates.containsKey(keyCode))
		{
			return false;
		}
		return keyStates.get(keyCode);
	}
	public HashMap<Integer, Boolean> getKeyState()
	{
		return keyStates;
	}
	public GodMouseInfo getMouseInfo()
	{
		return new GodMouseInfo(mouseInfo);
	}
}
