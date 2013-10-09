package test1;

import java.awt.Point;
import java.util.List;

import godEngine.gameContent.Actor;
import godEngine.gameContent.God;

public class TestActor extends Actor 
{
	int speed = 1;

	public void act()
	{
		move(God.getRandomNumber(4));/*
		//return;
		
		if(God.isKeyDown(KeyEvent.VK_LEFT))
		{
			move(1);
			//System.out.println("left");
		}
		if(God.isKeyDown(KeyEvent.VK_RIGHT))
		{
			move(3);
			//System.out.println("right");
		}
		if(God.isKeyDown(KeyEvent.VK_UP))
		{
			move(0);
			//System.out.println("up");
		}
		if(God.isKeyDown(KeyEvent.VK_DOWN))
		{
			move(2);
			//System.out.println("down");
		}*/
	}
	public void move(int dir)
	{
		int length = isFreeInDirection(dir, speed);
		
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
	public boolean isFreeAt(int dx, int dy)
	{
		if(dx<0 || dy<0 || dx >= getWorld().getWidth() || dy >= getWorld().getHeight()) return false;
		if(dx>getX()) dx+=getWidth();
		if(dy>getY()) dy+=getHeight();
		
		List<Actor> l = getWorld().getObjectsAt(dx, dy);
		return (l.size() == 0) || (l.size()==1 && l.get(0)==this);
	}
	public int isFreeInDirection(int dir, int speed)
	{
		List<Actor> l= null;
		if(speed==0) return 0;
		
		switch(dir)
		{
		case 0: // Oben
			if(getY()-speed < 0) break;
			l=getWorld().getObjectsInRect(	(int)getX(), (int)getY()-speed,
											(int)getX()+(getWidth()-1), (int)getY()-1, Actor.class);
			break;
		case 1: // Links
			if(getX()-speed < 0) break;
			l=getWorld().getObjectsInRect(	(int)getX()-speed, (int)getY(),
											(int)getX()-1, (int)getY()+(getHeight()-1), Actor.class);
			break;
		case 2: // Unten
			if(getY()+(getHeight()-1)+speed >= getWorld().getHeight()) break;
			l=getWorld().getObjectsInRect(	(int)getX(), (int)getY()+getHeight(),
											(int)getX()+(getWidth()-1), (int)getY()+(getHeight()-1)+speed, Actor.class);
			break;
		case 3: // Rechts
			if(getX()+getWidth()+speed > getWorld().getWidth()) break;
			l=getWorld().getObjectsInRect(	(int)getX()+ getWidth(), (int)getY(),
											(int)getX()+(getWidth()-1)+speed, (int)getY()+(getHeight()-1), Actor.class);
			break;
		}
		if( l== null) return isFreeInDirection(dir, speed-1);
		
		if(l.size()==0) return speed;
		
		return isFreeInDirection(dir, speed-1);
	}
}
