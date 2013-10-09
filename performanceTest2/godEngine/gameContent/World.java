package godEngine.gameContent;
import godEngine.gameDependencies.GameException;
import godEngine.gameDependencies.GodImage;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;



public abstract class World 
{
	private int width								=	1;
	private int height								=	1;
	private int cellSize							=	1;
	
	private GodImage background 					=	null;
	
	private HashMap<Integer, Actor> placedActors 	=	null;
	private ArrayList<Integer> occupiedCells[][]	=	null;
	
	public World()
	{
		this(1,1,1);
	}
	@SuppressWarnings("unchecked")
	public World(int width, int height, int cellSize)
	{
		if(width == 0 || height == 0 || cellSize == 0)
		{
			try{ throw new GameException(8);}
			catch(GameException ge)
			{
				ge.printMessage();
				ge.printStackTrace();
				System.exit(1);
			}
		}
		
		this.width			= width;
		this.height			= height;
		this.cellSize		= cellSize;
		
		placedActors 		= new HashMap<Integer, Actor>();
		occupiedCells 		= new ArrayList [width][height];
		setBackground(GodConstants.DEFAULT_BACKGROUND_IMAGE_PATH);
	}
	
	
	
	public void addObject(double dx, double dy, Class<? extends Actor> objectClass)
	{
		Actor newActor = null;
		
		try {
			newActor = (Actor)objectClass.newInstance();
		} catch (Exception e) {
			new GameException(5).printMessage();
			e.printStackTrace();
			System.exit(1);
		} 
		addObject(dx, dy, newActor);
	}
	public void addObject(double dx, double dy, Actor newActor)
	{		
		newActor.setLocation(dx,dy);
		newActor.addedToWorld(this);
	
		if( newActor.getImage() == null)
		{
			newActor.setImage(GodConstants.DEFAULT_ACTOR_IMAGE_PATH);
		}
	}
	
	
	
	public List<Actor> getObjectsAt(int dx, int dy)
	{
		return getObjectsAt(dx, dy, Actor.class);
	}
	public List<Actor> getObjectsAt(int dx, int dy, Class<? extends Actor> actorClass)
	{
		if(dx < 0 || dx >= getWidth() || dy < 0 || dy >=getHeight())
		{
			return null;
		}
		
		ArrayList<Actor> resultingActors	=	new ArrayList<Actor>();
		ArrayList<Integer> actorsOnCell		=	occupiedCells[dx][dy];
		
		if(actorsOnCell == null) return resultingActors;
		
		for(int i = 0; i < actorsOnCell.size(); i++)
		{
			Actor chosenActor = getActorByID(actorsOnCell.get(i));
			if(actorClass.isAssignableFrom(chosenActor.getClass()))
			{
				resultingActors.add(chosenActor);
			}
		}		
		return resultingActors;
	}
	
	public List<Actor> getObjectsInRect(Rectangle rect)
	{
		return getObjectsInRect((int)rect.getX(), (int)(rect.getX()+rect.getWidth()), (int)rect.getY(), (int)(rect.getY()+rect.getHeight()), Actor.class);
	}
	public List<Actor> getObjectsInRect(Rectangle rect, Class<? extends Actor> actorClass)
	{
		return getObjectsInRect((int)rect.getX(), (int)(rect.getX()+rect.getWidth()), (int)rect.getY(), (int)(rect.getY()+rect.getHeight()), actorClass);
	}
	public List<Actor> getObjectsInRect(Point topLeft, Point bottomRight)
	{
		return getObjectsInRect(topLeft, bottomRight, Actor.class);
	}
	public List<Actor> getObjectsInRect(Point topLeft, Point bottomRight, Class<? extends Actor> actorClass)
	{
		return getObjectsInRect((int)topLeft.getX(), (int)topLeft.getY(), (int)bottomRight.getX(), (int)bottomRight.getY(), actorClass);
	}
	public List<Actor> getObjectsInRect(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY, Class<? extends Actor> actorClass)
	{
		HashMap<Integer, Actor> resActorMap = new HashMap<Integer, Actor>();
				
		for(int y=topLeftY; y<=bottomRightY; y++)
		{
			for(int x=topLeftX; x<=bottomRightX; x++)
			{
				List<Actor> actorsAtPoint = getObjectsAt(x, y, actorClass);
				if(actorsAtPoint==null) continue;
				for(int i=0; i<actorsAtPoint.size(); i++)
				{
					Actor actorToAdd = actorsAtPoint.get(i);
					resActorMap.put(actorToAdd.getActorID(), actorToAdd);
				}
			}	
			
		}
		
		List<Actor> resultingActors = new ArrayList<Actor>();
		for (Map.Entry<Integer, Actor> entry : resActorMap.entrySet()) {
		    Actor chosenActor = entry.getValue();
			if(actorClass.isAssignableFrom(chosenActor.getClass()))
			{
				resultingActors.add(chosenActor);
			}
		}
		
		return resultingActors;	
	}
	

	public List<Actor> getObjects(Class<Actor> actorClass)
	{
		List<Actor> resultingActors = null;
		
		if(actorClass == Actor.class)
		{
			return resultingActors = new ArrayList<Actor>(placedActors.values());
		}
		
		resultingActors = new ArrayList<Actor>();
				

		for (Map.Entry<Integer, Actor> entry : placedActors.entrySet()) {
		    Actor chosenActor = entry.getValue();
			if(chosenActor.getClass() == actorClass)
			{
				resultingActors.add(chosenActor);
			}
		}
		
		return resultingActors;
	}
	
	public int getNumberOfObjectsAt(int dx, int dy)
	{
		return occupiedCells[dx][dy].size();
	}
	
	public void removeObject(Actor toRemove)
	{
		if(placedActors.remove(toRemove.getActorID())==null)
		{
			try {
				throw new GameException(1);
			} catch (GameException e) {
				e.printMessage();
				e.printStackTrace();
				System.exit(1);
			}	
		}
	}	
	
	public void gameStopped(){	}
	
	public void acquireActorID(Actor newActor)
	{
		int newUniqueID = 0;
		if(!placedActors.containsKey(placedActors.size()+1))
		{
			newUniqueID = placedActors.size()+1;
		}
		else
		{
			int i=0;
			while(i>0)
			{
				if(!placedActors.containsKey(i))
				{
					newUniqueID = i;
				}
			}
			if(newUniqueID == 0) // Falls keine ID gefunden werden konnte ( ALLE positiven Integer belegt)
			{
				try
				{ 
					throw new GameException(7); 
				}
				catch(GameException e)
				{
					e.printMessage();
					System.exit(1);
				}
			}
		}
		newActor.setActorID(newUniqueID);
		placedActors.put(newUniqueID, newActor);
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
		int width 	= movedActor.getWidth();
		int height	= movedActor.getHeight();
		int minX	= Math.min((int)movedActor.getX(), newX);
		int minY	= Math.min((int)movedActor.getY(), newY);
		int maxX	= Math.max((int)movedActor.getX(), newX);
		int maxY	= Math.max((int)movedActor.getY(), newY);
		
		for(int y = minY; y < maxY+height; y++)
		{
			for(int x = minX; x < maxX+width; x++)
			{
				// Remove old Actor
				if(occupiedCells[x][y] != null)
				{
					occupiedCells[x][y].remove(new Integer(movedActor.getActorID()));
					if(occupiedCells[x][y].size() == 0) occupiedCells[x][y] = null;
				}
				
				// Add new Actor

				if(		(x >= movedActor.getX()) && (x < movedActor.getX()+width) && 
						(y >= movedActor.getY()) && (y < movedActor.getY()+height))
				{
					// Wenn Transparent, überspringen;
					if(movedActor.getImage().getOccupiedCells()
							[(int) (x-movedActor.getX())][(int) (y-movedActor.getY())] == 0) continue;
					
					if(occupiedCells[x][y]==null)
					{
						occupiedCells[x][y] = new ArrayList<Integer>();
						occupiedCells[x][y].add(movedActor.getActorID());
					}
					else
					{
						occupiedCells[x][y].add(movedActor.getActorID());
					}
					
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
				
				if(occupiedCells[x][y]==null)
				{
					occupiedCells[x][y] = new ArrayList<Integer>();
					occupiedCells[x][y].add(movedActor.getActorID());
				}
				else
				{
					occupiedCells[x][y].add(movedActor.getActorID());
				}
			}
		}
	}
	
	public void recalculateOccupiedPixels(Actor changedActor)
	{
		int newImageWidth = changedActor.getWidth();
		int newImageHeight = changedActor.getHeight();
		
		// Remove all occupied cells first
		// And readd it
		for(int y = 0; y < occupiedCells[0].length; y++)
		{
			for(int x = 0; x < occupiedCells.length; x++)
			{
				// Remove old Actor
				if(occupiedCells[x][y] != null)
				{
					occupiedCells[x][y].remove(new Integer(changedActor.getActorID()));
					if(occupiedCells[x][y].size() == 0) occupiedCells[x][y] = null;
				}
				
				// Add new Actor

				if(		(x >= changedActor.getX()) && (x < changedActor.getX()+newImageWidth) && 
						(y >= changedActor.getY()) && (y < changedActor.getY()+newImageHeight))
				{
					// Wenn Transparent, überspringen;
					if(changedActor.getImage().isTransparentAt(
							(int) (x-changedActor.getX()), (int) (y-changedActor.getY()))) continue;
					
					if(occupiedCells[x][y]==null)
					{
						occupiedCells[x][y] = new ArrayList<Integer>();
						occupiedCells[x][y].add(changedActor.getActorID());
					}
					else
					{
						occupiedCells[x][y].add(changedActor.getActorID());
					}
					
				}
			}
		}		
	}
	public Actor getActorByID(int actorID)
	{
		return placedActors.get(actorID);
	}
	
	
	
	public void act()	{	}
		
	public int getNumberOfObjects()
	{
		return placedActors.size();
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getCellSize() {
		return cellSize;
	}
	public GodImage getBackground()
	{
		return background;
	}
	public void setBackground(GodImage image)
	{
		background = image;
	}
	public void setBackground(String path)
	{
		if(background == null || !path.equals(background.getImagePath()))
		{
			background = new GodImage(path, cellSize);
		}
	}
}
