package test1;

import java.awt.event.KeyEvent;
import java.util.List;

import godEngine.gameContent.Actor;
import godEngine.gameContent.God;
import godEngine.gameDependencies.GameException;

public class TestObstacle extends TestActor 
{
	public TestObstacle()
	{
		setStartImage("toast.jpg");
	}
	protected void addedToWorld()
	{
	}
	
	boolean grow = false;
	
	public void act() throws GameException
	{
		/*if(God.isKeyDown(KeyEvent.VK_RIGHT))
		{
			rotate(1);
			God.delay(10);
			System.out.println("X: " + getX() + ", Y: " + getY() + ", width: " + getAbsoluteWidth() + ", height: " + getAbsoluteHeight() + ", Angle: " + getRotation());
		}
		if(God.isKeyDown(KeyEvent.VK_LEFT))
		{
			rotate(-1);
			God.delay(10);
			System.out.println("X: " + getX() + ", Y: " + getY() + ", width: " + getAbsoluteWidth() + ", height: " + getAbsoluteHeight() + ", Angle: " + getRotation());
		}*/
		rotate(0.5);
		if(grow)
		{
			if(getWidth()<getWorld().getWidth()/2 && getHeight() < getWorld().getHeight()/2)
			{
				rescale(1.05,1.05);
			}
			else grow=false;
		}
		else
		{
			if(getWidth()>10 && getHeight()>10)
			{
				rescale(0.95,0.95);
			}
			else grow=true;
		}
		
		List<Actor> l = getIntersectingObjects(SmallActor.class);
		while(l.size()>0)
		{
			for(int i=0; i<l.size(); i++)
			{
				SmallActor s = (SmallActor)l.get(i);
				int dx = 0;
				int dy = 0;
				if(s.getX()<=getX()) dx=-1;
				if(s.getX()>getX()) dx=1;
				if(s.getY()<=getY()) dy=-1;
				if(s.getY()>getY()) dy=1;
				s.push(dx,dy);
			}
			l = getIntersectingObjects(SmallActor.class);
		}
	}
}
