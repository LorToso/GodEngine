package godEngine.gameContent;
import godEngine.gameDependencies.CellMap;
import godEngine.gameDependencies.GameException;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public abstract class World 
{
	private boolean isInitialized					=	false;
	
	private int width								=	-1;
	private int height								=	-1;
	private int cellSize							=	-1;
	
	
	private GodImage background 					=	null;
	
	private HashMap<Integer, Actor> placedActors 	=	null;
	private CellMap occupiedCells					=	null;
	
	
	public World(int width, int height, int cellSize) throws GameException
	{
		if(width <= 0 || height <= 0 || cellSize <= 0)
		{
			throw new GameException(GameException.ERROR_INVALID_WORLD_DIMENSIONS);
		}
		
		this.width			= width;
		this.height			= height;
		this.cellSize		= cellSize;
		
		placedActors 		= new HashMap<>();
		occupiedCells 		= new CellMap(width, height);
		

	}
	protected void initialize() throws GameException
	{
		isInitialized = true;
		setBackground(GodUtilities.DEFAULT_BACKGROUND_IMAGE);
		initializeWorld();
	}

	public void addObject(double dx, double dy, Class<? extends Actor> objectClass) throws GameException
	{
		Actor newActor = null;
		
		try {
			newActor = objectClass.newInstance();
		} catch (Exception e) 
		{
			throw new GameException(GameException.ERROR_CREATING_OBJECT);
		} 
		addObject(dx, dy, newActor);
	}
	public synchronized void addObject(double dx, double dy, Actor newActor) throws GameException
	{		
		if(!isInitialized)
			throw new GameException(GameException.ERROR_WORLD_WAS_NOT_INITIALIZED_YET);
		
		newActor.setLocation(dx,dy);
		newActor.addedToWorld(this);
	}
	
	
	
	public List<Actor> getObjectsAt(int dx, int dy)
	{
		return getObjectsAt(dx, dy, Actor.class);
	}
	/**
	 * Returns an ArrayList of all objects at the given location. CAREFUL! If the coordinates are outside the world it does currently always return null.
	 * @param dx The X-Coordinate of the location.
	 * @param dy The Y-Coordinate of the location.
	 * @param actorClass The Class the objects shall be filtered at
	 * @return A List of all objects at the given location.
	 */
	public List<Actor> getObjectsAt(int dx, int dy, Class<? extends Actor> actorClass)
	{
		if(dx < 0 || dx >= getWidth() || dy < 0 || dy >=getHeight())
		{
			return null;
		}
		
		ArrayList<Actor> resultingActors	=	new ArrayList<>();
		ArrayList<Integer> actorsOnCell		=	occupiedCells.getObjectsAt(dx, dy);
		
		if(actorsOnCell == null) return resultingActors;

        for (Integer anActorsOnCell : actorsOnCell) {
            Actor chosenActor = getActorByID(anActorsOnCell);
            if (actorClass.isAssignableFrom(chosenActor.getClass())) {
                resultingActors.add(chosenActor);
            }
        }
		return resultingActors;
	}
	
	public List<Actor> getObjectsInRect(Rectangle rect)
	{
		return getObjectsInRect(rect, Actor.class);
	}
	public List<Actor> getObjectsInRect(Rectangle rect, Class<? extends Actor> actorClass){
		return getObjectsInRect((int)rect.getX(), (int)rect.getY(), (int)(rect.getX()+rect.getWidth()-1), (int)(rect.getY()+rect.getHeight()-1), actorClass);
	}
	public List<Actor> getObjectsInRect(Point topLeft, Point bottomRight)
	{
		return getObjectsInRect(topLeft, bottomRight, Actor.class);
	}
	public List<Actor> getObjectsInRect(Point topLeft, Point bottomRight, Class<? extends Actor> actorClass)
	{
		return getObjectsInRect((int)topLeft.getX(), (int)topLeft.getY(), (int)bottomRight.getX(), (int)bottomRight.getY(), actorClass);
	}
	public List<Actor> getObjectsInRect(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY)
	{
		return getObjectsInRect(topLeftX, topLeftY, bottomRightX, bottomRightY, Actor.class);
	}
	public List<Actor> getObjectsInRect(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY, Class<? extends Actor> actorClass)
	{
		HashMap<Integer, Actor> resActorMap = new HashMap<>();
				
		for(int y=topLeftY; y<=bottomRightY; y++)
		{
			for(int x=topLeftX; x<=bottomRightX; x++)
			{
				List<Actor> actorsAtPoint = getObjectsAt(x, y, actorClass);
				if(actorsAtPoint==null) continue;
                for (Actor actorToAdd : actorsAtPoint) {
                    resActorMap.put(actorToAdd.getActorID(), actorToAdd);
                }
			}	
			
		}
		
		List<Actor> resultingActors = new ArrayList<>();
		for (Map.Entry<Integer, Actor> entry : resActorMap.entrySet()) {
		    Actor chosenActor = entry.getValue();
			if(actorClass.isAssignableFrom(chosenActor.getClass()))
			{
				resultingActors.add(chosenActor);
			}
		}
		
		return resultingActors;	
	}
	

	public List<Actor> getObjects(Class<Actor> class1)
	{
		List<Actor> resultingActors;
		
		if(class1 == Actor.class)
		{
			return new ArrayList<>(placedActors.values());
		}
		
		resultingActors = new ArrayList<>();
				

		for (Map.Entry<Integer, Actor> entry : placedActors.entrySet()) {
		    Actor chosenActor = entry.getValue();
			if(chosenActor.getClass() == class1)
			{
				resultingActors.add(chosenActor);
			}
		}
		
		return resultingActors;
	}
	
	public int getNumberOfObjectsAt(int dx, int dy)
	{
		ArrayList<Integer> l = occupiedCells.getObjectsAt(dx, dy);
		
		if(l == null )
			return 0;
		
		return l.size();
	}
	
	public synchronized void removeObject(Actor toRemove) throws GameException
	{
		if(placedActors.remove(toRemove.getActorID())==null)
		{
			throw new GameException(GameException.ERROR_OBJECT_NOT_IN_WORLD);
		}
		removeFromCells(toRemove);
		toRemove.removedFromWorld(this);
	}
	
	private void removeFromCells(Actor toRemove) throws GameException
	{
		Rectangle r = toRemove.getRect();
		for(int y = (int) r.getMinY(); y < r.getMaxY(); y++)
		{
			for(int x = (int) r.getMinX(); x < r.getMaxX(); x++)
			{
				occupiedCells.getObjectsAt(x, y).remove(new Integer(toRemove.getActorID()));
			}
		}
	}
	
	
	public void acquireActorID(Actor newActor) throws GameException
	{
		int newUniqueID	=	placedActors.size();
		while(placedActors.containsKey(newUniqueID)) 	// probiert den erstbesten Key
		{												// Wenn belegt, sucht er einen
			newUniqueID++;
			if(newUniqueID == placedActors.size()) // Falls keine ID gefunden werden konnte (Integer Bereich belegt)
			{
				throw new GameException(GameException.ERROR_NO_VALID_ACTOR_ID);
			}
		}
		newActor.setActorID(newUniqueID);
		placedActors.put(newUniqueID, newActor);
	}
	
	public boolean doObjectsIntersect(Actor firstActor, Actor secondActor)
	{
		int firstImageArea 	= firstActor.getHeight() * firstActor.getWidth();
		int secondImageArea	= secondActor.getHeight() * secondActor.getWidth();
		
		int cornerX			= 0;
		int cornerY			= 0;
		int widthToSearch	= 0;
		int heightToSearch	= 0;
		
		if(firstImageArea < secondImageArea)
		{
			cornerX			= (int) firstActor.getX();
			cornerY			= (int) firstActor.getY();
			widthToSearch	= firstActor.getWidth();
			heightToSearch	= firstActor.getHeight();
		}
		else
		{
			cornerX			= (int) secondActor.getX();
			cornerY			= (int) secondActor.getY();
			widthToSearch	= secondActor.getWidth();
			heightToSearch	= secondActor.getHeight();
		}
		
		for(int dy = cornerY; dy < cornerY+heightToSearch; dy++)
		{
			for(int dx = cornerX; dx < cornerX+widthToSearch; dx++)
			{
				List<Actor> al = getObjectsAt(dx, dy);
				if(al == null) 		continue;
				if(al.size() < 2) 	continue;
				
				if(
						al.contains(firstActor) &&
						al.contains(secondActor)
					) return true;
			}
		}
		return false;
	}
	
	private Actor getActorByID(int actorID)
	{
		return placedActors.get(actorID);
	}
	protected CellMap getCellMap()
	{
		return occupiedCells;
	}
	
	
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
	public void setBackground(String path) throws GameException
	{
		background = new GodImage(path); 		//TODO:
		background.setSize(getWidth()*getCellSize(), getHeight()*getCellSize());
	}

	
	
	protected abstract void gameStopped() 		throws GameException;
	protected abstract void preGameMode() 		throws GameException;
	protected abstract void act() 				throws GameException;
	protected abstract void initializeWorld() 	throws GameException;
}
