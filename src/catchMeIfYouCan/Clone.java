package catchMeIfYouCan;

import godEngine.gameContent.Actor;
import godEngine.gameDependencies.GameException;

import java.util.ArrayList;

public class Clone extends MovingObject 
{
	Map map;
	ArrayList<Direction> allMoves;
	int nextMove 					= 0;
	int myCloneNumber 				= 0;
	private static int cloneNumber 	= 0;
	
	protected void addedToWorld() throws GameException
	{
		setSize(originalSize,originalSize);
		map = (Map) getWorld();
		allMoves = map.registerClone(this);
		myCloneNumber = retrieveCloneNumber();
	}
	public void act() throws GameException
	{
		cloneMove();
		eat();
	}
	private int retrieveCloneNumber()
	{
		cloneNumber++;
		return cloneNumber;
	}
	private void cloneMove() throws GameException
	{
		if(nextMove < allMoves.size())
		{
			move(allMoves.get(nextMove));
			nextMove++;
		}
	}
	public boolean move(Direction dir) throws GameException
	{
		switch(dir)
		{
		case TOP:
			setLocation(getX(), getY()-speed);
			break;
		case BOTTOM:
			setLocation(getX(), getY()+speed);
			break;
		case LEFT:
			setLocation(getX()-speed, getY());
			break;
		case RIGHT:
			setLocation(getX()+speed, getY());
			break;
		default:
			break;
		}
		return true;
	}
	public void eat() throws GameException
	{
		ArrayList<Actor> intersectingObjects = (ArrayList<Actor>) getIntersectingObjects(MovingObject.class);

        if(intersectingObjects.isEmpty())
            return;
		
		for(int i=0; i < intersectingObjects.size(); i++)
		{
			MovingObject chosenActor = (MovingObject)intersectingObjects.get(0);

			if(chosenActor instanceof Clone)
			{
				Clone chosenClone = (Clone) chosenActor;

                if(chosenClone.getCloneNumber() < getCloneNumber())
                    return;

                int newSize = Math.max(getSize(), chosenClone.getSize())+2;
				getWorld().removeObject(chosenActor);
				intersectingObjects.remove(0);
				setSize(originalSize*newSize, originalSize*newSize);
				size+=2;
			}
			else
			{
				getWorld().removeObject(chosenActor);
				intersectingObjects.remove(0);
				
				System.out.println("You loose!");
			}
		}
	}
	public int getCloneNumber()
	{
		return myCloneNumber;
	}
}
