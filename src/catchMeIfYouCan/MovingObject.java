package catchMeIfYouCan;

import godEngine.gameContent.Actor;
import godEngine.gameDependencies.GameException;

import java.awt.*;

public class MovingObject extends Actor {
	double speed = 2;
	int size	=	1;
	int originalSize = 5;
	
	public MovingObject()
	{
	}
	
	protected void addedToWorld() throws GameException {	}
	public void act() throws GameException {	}
	
	public int getMaximumFreeDistance(Direction dir, int length)
	{
        Rectangle rectangle = getRect();

        for(int distance = 1; distance <= length; distance++)
        {
            switch (dir)
            {
                case NO_MOVE:
                    return 0;
                case TOP:
                    rectangle.translate(0,-1);
                    break;
                case LEFT:
                    rectangle.translate(-1,0);
                    break;
                case RIGHT:
                    rectangle.translate(1,0);
                    break;
                case BOTTOM:
                    rectangle.translate(0,1);
                    break;
            }
            if(getWorld().getObjectsInRect(rectangle, Actor.class).size() > 1)
                return distance -1;
        }
        return length;


	}
	public boolean move(Direction dir) throws GameException
	{
		int length = getMaximumFreeDistance(dir, (int)speed);
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
	protected void removedFromWorld() throws GameException {	}
}
