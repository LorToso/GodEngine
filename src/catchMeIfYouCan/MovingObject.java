package catchMeIfYouCan;

import godEngine.gameContent.Actor;
import godEngine.gameDependencies.GameException;

import java.awt.*;
import java.util.ArrayList;

public class MovingObject extends Actor {
	double speed = 2;
	int size	=	1;
	int originalSize = 5;
	
	public MovingObject()
	{
	}
	
	protected void addedToWorld() throws GameException {	}
	public void act() throws GameException {	}
	
	public int isFreeInDirection(Direction dir, int length) throws GameException
	{
		return isFreeInDirection(dir, length, 1);
	}
	public int isFreeInDirection(Direction dir, int length, int position) throws GameException
	{
		if(length <= 0)
			return 0;
		
		while(position<length)
		{
			Rectangle myRect = getRect();
			ArrayList<Actor> objects = null;
		
			switch(dir)
			{
			case NO_MOVE: return 0;
			case TOP:
				objects = (ArrayList<Actor>) getWorld().getObjectsInRect(new Rectangle(myRect.x, myRect.y-position, getWidth(), 1));
				break;
			case BOTTOM:
				objects = (ArrayList<Actor>) getWorld().getObjectsInRect(new Rectangle(myRect.x, myRect.y+getHeight()+position, getWidth(), 1));
				break;
			case LEFT:
				objects = (ArrayList<Actor>) getWorld().getObjectsInRect(new Rectangle(myRect.x-position, myRect.y, 1, getHeight()));
				break;
			case RIGHT:
				objects = (ArrayList<Actor>) getWorld().getObjectsInRect(new Rectangle(myRect.x+getWidth()+1, myRect.y, 1, getHeight()));
				break;
			}
			if(objects.size() != 0) return position-1;
			position++;
		}
		return length;

	}
	public boolean move(Direction dir) throws GameException
	{
		int length = isFreeInDirection(dir, (int)speed);
		switch(dir)
		{
		case TOP:
			setLocation(getX(), getY()-length); 
			break;
		case BOTTOM:
			setLocation(getX(), getY()+length);
			break;
		case LEFT:
			setLocation(getX()-length, getY());
			break;
		case RIGHT:
			setLocation(getX()+length, getY());
			break;
		default:
			break;
		}
        return length != 0 || dir == Direction.NO_MOVE;
				
	}
	public int getSize()
	{
		return size;
	}

	@Override
	protected void removedFromWorld() throws GameException {
		// TODO Auto-generated method stub
		
	}
}
