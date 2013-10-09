package test1;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;

import godEngine.gameContent.Actor;
import godEngine.gameContent.God;
import godEngine.gameDependencies.GameException;


public class TestActor extends Actor 
{
	int speed = 10;
	
	public TestActor()
	{
		//setStartImage("circle.png");
	}
	protected void addedToWorld() throws GameException
	{
		setSize(50,50);
	}
	public void act() throws GameException {
		if(God.isKeyDown(KeyEvent.VK_UP))
			move(0);
		if(God.isKeyDown(KeyEvent.VK_LEFT))
			move(1);
		if(God.isKeyDown(KeyEvent.VK_DOWN))
			move(2);
		if(God.isKeyDown(KeyEvent.VK_RIGHT))
			move(3);
		if(God.isKeyDown(KeyEvent.VK_W))
			rescale(1.1, 1.1);
		if(God.isKeyDown(KeyEvent.VK_S))
			if(getWidth()>11)rescale(0.9, 0.9);
		if(God.isKeyDown(KeyEvent.VK_A))
			rotate(-5);
		if(God.isKeyDown(KeyEvent.VK_D))
			rotate(5);
		if(God.isKeyDown(KeyEvent.VK_Q))
			fade(5);
		if(God.isKeyDown(KeyEvent.VK_E))
			fade(-5);
		//God.delay(50);
		//System.out.println("X: " + getX() + ", Y: " + getY());
		//move(2);

	}
	public void move(int dir) throws GameException
	{
		int length = isFreeInDirection(dir);
	
		if(length == 0) return;
		
		int dx = (int) getX();
		int dy = (int) getY();
		
		switch(dir)
		{
		case 0: dy-=length; break;
		case 1: dx-=length; break;
		case 2: dy+=length; break;
		case 3: dx+=length; break;
		}

		
		setLocation(dx, dy);
	}
	public int isFreeInDirection(int dir) throws GameException
	{
		Rectangle rect		=	null;
		Rectangle myRect	=	getRect();
		int walkable		=	0;
		for(walkable = 1; walkable <= speed; walkable++)
		{
			switch(dir)
			{
			case 0: // Oben
				rect = new Rectangle(myRect.x, myRect.y-walkable, myRect.width, 1);
				if(getWorld().getObjectsInRect(rect).size()>0) return (walkable-1);
				break;
			case 1: // Links
				rect = new Rectangle(myRect.x-walkable, myRect.y, 1, myRect.height);
				if(getWorld().getObjectsInRect(rect).size()>0) return (walkable-1);
				break;
			case 2: // Unten
 				rect = new Rectangle(myRect.x, myRect.y+(myRect.height-1)+ walkable, myRect.width, 1);
				List<Actor> l = getWorld().getObjectsInRect(rect);
				if(l.size()>0)
				{
					return (walkable-1);
				}
				break;
			case 3: // Rechts
				rect = new Rectangle(myRect.x+(myRect.width-1)+walkable, myRect.y, 1, myRect.height);
				if(getWorld().getObjectsInRect(rect).size()>0) return (walkable-1);
				break;
			}
			
		}
		return walkable-1;
	}
	public void push(int x, int y) throws GameException
	{
		
	}
	@Override
	protected void removedFromWorld() throws GameException {
		// TODO Auto-generated method stub
		
	}
}