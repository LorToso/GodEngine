package test1;

import java.util.List;

import godEngine.gameContent.Actor;
import godEngine.gameDependencies.GameException;

public class SmallActor extends TestActor {
	
	public SmallActor()
	{
		super();
		speed = 1;
		setStartImage("minime.jpg");
	}
	protected void addedToWorld() throws GameException
	{
		setSize(getWorld().getCellSize(), getWorld().getCellSize());
	}
	public void act() throws GameException 
	{
		move(2);
	}
	public void push(int dx, int dy) throws GameException
	{
		/*if(dx<0 && getX()==0)
			dx=0;
		if(dx>0 && getX()==getWorld().getWidth()-1)
			dx=0;
		if(dy<0 && getY()==0)
			dy=0;
		if(dy>0 && getY()==getWorld().getHeight()-1)
			dy=0;
		
		List<Actor> l = getWorld().getObjectsAt((int)(getX()+dx), (int)(getY()+dy), SmallActor.class); 
		if(l.size()>0)
		{
			for(int i=0; i<l.size(); i++)
			{
				SmallActor s = (SmallActor)l.get(i);
				if(s==this) continue;
				s.push(dx, dy);
			}
		}
		setLocation(getX()+dx, getY()+dy);
	*/		
	}
}
