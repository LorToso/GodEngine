package catchMeIfYouCan;

import godEngine.gameContent.God;
import godEngine.gameDependencies.GameException;

import java.awt.event.KeyEvent;

public class Player extends MovingObject {

	Map map;
	public Player() throws GameException
	{
	}
	protected void addedToWorld() throws GameException 
	{
		setSize(originalSize,originalSize);
		map = (Map)getWorld();
	}

	public void act() throws GameException 
	{
		if(God.isKeyDown(KeyEvent.VK_UP))
			move(Direction.TOP);
		else if(God.isKeyDown(KeyEvent.VK_RIGHT))
			move(Direction.RIGHT);
		else if(God.isKeyDown(KeyEvent.VK_LEFT))
			move(Direction.LEFT);
		else if(God.isKeyDown(KeyEvent.VK_DOWN))
			move(Direction.BOTTOM);
		else move(Direction.NO_MOVE);
	}
	public boolean move(Direction dir) throws GameException
	{
		if(super.move(dir))
			map.addMove(dir);
		return true;
	}

}
