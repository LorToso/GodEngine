package godEngine.gameContent;
import godEngine.gameDependencies.ActorHolder;
import godEngine.gameDependencies.GameException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public abstract class World 
{
	private boolean isInitialized					=	false;
	
	private int width								=	-1;
	private int height								=	-1;
	private int cellSize							=	-1;
	
	
	private GodImage background 					=	null;

	private ActorHolder actorHolder				    =	null;
	
	
	public World(int width, int height, int cellSize) throws GameException
	{
		if(width <= 0 || height <= 0 || cellSize <= 0)
		{
			throw new GameException(GameException.ERROR_INVALID_WORLD_DIMENSIONS);
		}
		
		this.width			= width;
		this.height			= height;
		this.cellSize		= cellSize;

        actorHolder 		= new ActorHolder();
	}
	protected void initialize() throws GameException
	{
		isInitialized = true;
		setBackground(GodUtilities.DEFAULT_BACKGROUND_IMAGE);
		initializeWorld();
	}

	public void addObject(double dx, double dy, Actor newActor) throws GameException
	{		
		if(!isInitialized)
			throw new GameException(GameException.ERROR_WORLD_WAS_NOT_INITIALIZED_YET);
		
		newActor.setLocation(dx,dy);
        actorHolder.addObject(newActor);
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
	public ArrayList<Actor> getObjectsAt(int dx, int dy, Class<? extends Actor> actorClass)
	{
		return actorHolder.getObjectsAt(dx,dy,actorClass);
	}
	
	public ArrayList<Actor> getObjectsInRect(Rectangle rect, Class<? extends Actor> actorClass)
	{
		return actorHolder.getObjectsInRect(rect, actorClass);
	}

	public List<Actor> getObjects(Class<Actor> class1)
	{
        return actorHolder.getObjects(class1);
	}
	
	public void removeObject(Actor toRemove) throws GameException {
        actorHolder.removeObject(toRemove);
        toRemove.beingRemovedFromWorld();
	}

    public boolean doObjectsIntersect(Actor firstActor, Actor secondActor)
	{
        return firstActor.getRect().intersects(secondActor.getRect());
    }
	
	private Actor getActorByID(int actorID)
	{
		return actorHolder.getById(actorID);
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
