package godEngine.gameDependencies;

import godEngine.gameContent.Actor;
import godEngine.gameContent.GodImage;
import godEngine.gameContent.GodUtilities;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class CellMap 
{
	private ArrayList<Integer> occupiedCells[][]	=	null;
	
	@SuppressWarnings("unchecked")
	public CellMap(int width, int height)
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
	 * @throws GameException 
	 */
	public void recalculateOccupiedPixelMovement(Actor movedActor, int newX, int newY) throws GameException
	{
		Rectangle oldRect = GodUtilities.calcCorrectRect((int)movedActor.getX(), (int)movedActor.getY(), movedActor.getWidth(), movedActor.getHeight());
		Rectangle newRect = GodUtilities.calcCorrectRect(newX, newY, movedActor.getWidth(), movedActor.getHeight());
		Rectangle combinedRect = oldRect.union(newRect);
		
		int combinedExpanse = 	combinedRect.height * combinedRect.width;
		int singleExpanse	=	oldRect.height*oldRect.width + newRect.height*newRect.width;

		if(combinedExpanse < singleExpanse)
			recalculateOccupiedPixelMovementCE(movedActor, newX, newY);
		else
			recalculateOccupiedPixelMovementSE(movedActor, newX, newY);
		
	}
	public void recalculateOccupiedPixelImageChange(Actor changedActor, Rectangle oldRect, Rectangle newRect) throws GameException
	{
		Rectangle maxRect;
		
		if(oldRect.isEmpty())
			maxRect		= newRect;
		else
			maxRect		= oldRect.union(newRect);
		
		GodImage newImage 	= changedActor.getImage();

		int xOffset = -(newImage.getWidth()		> occupiedCells.length ? 	(occupiedCells.length	-newImage.getWidth())/2		:	0);
		int yOffset = -(newImage.getHeight()	> occupiedCells[0].length ? (occupiedCells[0].length-newImage.getHeight())/2	:	0);
		
											
		Point iterator 		= new Point(0,0);
		for(int y = maxRect.y; y < maxRect.y + maxRect.height; y++)
		{
			for(int x = maxRect.x; x < maxRect.x + maxRect.width; x++)
			{
				iterator.move(x, y);
				if(oldRect.contains(iterator))
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
					if(newImage.isTransparentAt(x - newRect.x + xOffset, y - newRect.y + yOffset))
						continue;
					
					if(occupiedCells[x][y] == null)
						occupiedCells[x][y] = new ArrayList<>();
				
					occupiedCells[x][y].add(changedActor.getActorID());
				}				
			}
		}
	}	
	
	
	private void recalculateOccupiedPixelMovementCE(Actor movedActor, int newX, int newY) throws GameException
	{
		Rectangle oldRect 	= GodUtilities.calcCorrectRect((int)movedActor.getX(), 	(int)movedActor.getY(), movedActor.getWidth(), movedActor.getHeight());
		Rectangle newRect 	= GodUtilities.calcCorrectRect(newX, 					newY, 					movedActor.getWidth(), movedActor.getHeight());
		Rectangle maxRect	= oldRect.union(newRect);
		
		for(int y = (int) maxRect.getMinY(); y < (int) maxRect.getMaxY(); y++)
		{
			for(int x = (int) maxRect.getMinX(); x < (int) maxRect.getMaxX(); x++)
			{
				// Remove old Actor
				if(occupiedCells[x][y] != null)
				{
					occupiedCells[x][y].remove(new Integer(movedActor.getActorID()));
					if(occupiedCells[x][y].size() == 0) occupiedCells[x][y] = null;
				}
				
				// Add new Actor

				if(newRect.contains(new Point(x, y)))
				{
					// Wenn Transparent, �berspringen;
					if(movedActor.getImage().isTransparentAt(x-newRect.x, y-newRect.y)) continue;
					
					if(occupiedCells[x][y] == null)
					{
						occupiedCells[x][y] = new ArrayList<>();
					}
					occupiedCells[x][y].add(movedActor.getActorID());					
				}	
			}
		}
		
	}
	private void recalculateOccupiedPixelMovementSE(Actor movedActor, int newX, int newY) throws GameException
	{
		Rectangle oldRect = GodUtilities.calcCorrectRect((int)movedActor.getX(), (int)movedActor.getY(), movedActor.getWidth(), movedActor.getHeight());
		Rectangle newRect = GodUtilities.calcCorrectRect(newX, newY, movedActor.getWidth(), movedActor.getHeight());

		// Remove old image
		for(int y = oldRect.y; y < oldRect.y+oldRect.height; y++)
		{
			for(int x = oldRect.x; x < oldRect.x+oldRect.width; x++)
			{
				if(occupiedCells[x][y] != null)
				{
					occupiedCells[x][y].remove(new Integer(movedActor.getActorID()));
					if(occupiedCells[x][y].size() == 0) occupiedCells[x][y] = null;
				}
			}
		}
		
		// Add new image
		for(int y = newRect.y; y < newRect.y+newRect.height; y++)
		{
			for(int x = newRect.x; x < newRect.x+newRect.width; x++)
			{
				// Wenn Transparent, �berspringen;
				if(movedActor.getImage().isTransparentAt(x-newRect.x, y-newRect.y)) continue;
				
				if(occupiedCells[x][y] == null)
				{
					occupiedCells[x][y] = new ArrayList<>();
				}
				occupiedCells[x][y].add(movedActor.getActorID());
			}
		}
	}
}