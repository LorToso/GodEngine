package godEngine.gameDependencies;

import godEngine.gameContent.Actor;
import godEngine.gameContent.GodImage;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class CopyOfCellMap 
{
	private ArrayList<Integer> occupiedCells[][]	=	null;
	
	
	@SuppressWarnings("unchecked")
	public CopyOfCellMap(int width, int height)
	{
		occupiedCells 		= new ArrayList [width][height];
	}

	public ArrayList<Integer> getObjectsAt(int dx, int dy)
	{
		return occupiedCells[dx][dy];
	}
	public void setObjectsAt(int dx, int dy, ArrayList<Integer> toPut)
	{
		occupiedCells[dx][dy] = toPut;
	}

	/**
	 * Recalculates the occupied Pixels after an Actor moved.
	 * @param movedActor 	Specifies the moved Actor. Is used to acquire the actor ID and the old Position.
	 * @param newX			Specifies the new X - coordinate of the Actor.
	 * @param newY			Specifies the new Y - coordinate of the Actor.
	 */
	public void recalculateOccupiedPixelMovement(Actor movedActor, int newX, int newY)
	{
		int width 	= movedActor.getWidth();
		int height	= movedActor.getHeight();
		int minX	= 0;
		int minY	= 0;
		int maxX	= 0;
		int maxY	= 0;

		if(movedActor.getX() < newX)
		{
			minX = (int)movedActor.getX();
			maxX = newX;
		}
		else
		{
			minX = newX;
			maxX = (int)movedActor.getX();
		}
		if(movedActor.getY() < newY)
		{
			minY = (int)movedActor.getY();
			maxY = newY;
		}
		else
		{
			minY = newY;
			maxY = (int)movedActor.getY();
		}
		int combinedExpanse = ((maxX+width)-minX)*((maxY+height)-minY);
		int singleExpanse	=	2*width*height;

		if(combinedExpanse < singleExpanse)
			recalculateOccupiedPixelMovementCE(movedActor, newX, newY);
		else
			recalculateOccupiedPixelMovementSE(movedActor, newX, newY);
		
	}
	private void recalculateOccupiedPixelMovementCE(Actor movedActor, int newX, int newY)
	{
		int minX	= Math.min((int)movedActor.getX(), newX);
		int minY	= Math.min((int)movedActor.getY(), newY);
		int maxX	= Math.max((int)movedActor.getX(), newX);
		int maxY	= Math.max((int)movedActor.getY(), newY);
		
		for(int y = minY; y < maxY+movedActor.getHeight(); y++)
		{
			for(int x = minX; x < maxX+movedActor.getWidth(); x++)
			{
				// Remove old Actor
				if(occupiedCells[x][y] != null)
				{
					occupiedCells[x][y].remove(new Integer(movedActor.getActorID()));
					if(occupiedCells[x][y].size() == 0) occupiedCells[x][y] = null;
				}
				
				// Add new Actor

				if(		(x >= newX) && (x < newX+movedActor.getWidth()) && 
						(y >= newY) && (y < newY+movedActor.getHeight()))
				{
					// Wenn Transparent, überspringen;
					if(movedActor.getImage().isTransparentAt(x-newX, y-newY)) continue;
					
					if(occupiedCells[x][y] == null)
					{
						occupiedCells[x][y] = new ArrayList<Integer>();
					}
					occupiedCells[x][y].add(movedActor.getActorID());					
				}	
			}
		}
		
	}
	private void recalculateOccupiedPixelMovementSE(Actor movedActor, int newX, int newY)
	{
		int dx = (int) movedActor.getX();
		int dy = (int) movedActor.getY();
		
		// Remove old image
		for(int y = dy; y < dy+movedActor.getHeight(); y++)
		{
			for(int x = dx; x < dx+movedActor.getWidth(); x++)
			{
				if(occupiedCells[x][y] != null)
				{
					occupiedCells[x][y].remove(new Integer(movedActor.getActorID()));
					if(occupiedCells[x][y].size() == 0) occupiedCells[x][y] = null;
				}
			}
		}
		
		// Add new image
		for(int y = newY; y < newY+movedActor.getHeight(); y++)
		{
			for(int x = newX; x < newX+movedActor.getWidth(); x++)
			{
				// Wenn Transparent, überspringen;
				if(movedActor.getImage().isTransparentAt((int) (x-newX), (int) (y-newY))) continue;
				
				if(occupiedCells[x][y] == null)
				{
					occupiedCells[x][y] = new ArrayList<Integer>();
				}
				occupiedCells[x][y].add(movedActor.getActorID());
			}
		}
	}
	public void recalculateOccupiedPixelImageChange(Actor changedActor, GodImage newImage)
	{
		boolean hasOldImage = false;
		int oldWidth		= 0;
		int oldHeight		= 0;
		
		
		if(changedActor.getImage() != null)
		{
			hasOldImage 	= true;
			oldWidth 		= changedActor.getWidth();
			oldHeight 		= changedActor.getHeight();
		}
			
		
		int maxWidth		= Math.max(oldWidth,	newImage.getWidth());
		int maxHeight		= Math.max(oldHeight, 	newImage.getHeight());

		Rectangle oldRect 	= new Rectangle((int)changedActor.getX(), (int)changedActor.getY(), oldWidth, oldHeight);
		Rectangle newRect 	= new Rectangle((int)changedActor.getX(), (int)changedActor.getY(), newImage.getWidth(), newImage.getHeight());
		
		Point iterator 		= new Point(0,0);
		for(int y = (int)changedActor.getY(); y < (int)changedActor.getY() + maxHeight; y++)
		{
			for(int x = (int)changedActor.getX(); x < (int)changedActor.getX() + maxWidth; x++)
			{
				iterator.move(x, y);
				if(hasOldImage && oldRect.contains(iterator))
				{
					if(occupiedCells[x][y] != null)
					{
						occupiedCells[x][y].remove(new Integer(changedActor.getActorID()));
						if(occupiedCells[x][y].size()==0)
							occupiedCells[x][y] = null;
					}
				}
				if(newRect.contains(iterator))
				{
					if(newImage.isTransparentAt(x-(int)changedActor.getX(), y-(int)changedActor.getY()))
						continue;
					
					if(occupiedCells[x][y] == null)
					{
						occupiedCells[x][y] = new ArrayList<Integer>();
					}
					occupiedCells[x][y].add(changedActor.getActorID());
				}				
			}
		}
	}	
}
