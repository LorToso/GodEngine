package godEngine.gameContent;

import godEngine.gameDependencies.GameException;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;



public abstract class Actor 
{
	private World myWorld 		= null;
	private double x 			= 0;
	private double y 			= 0;
	private GodImage image 		= null;
	private int actorID 		= 0;
	private String startImage 	= GodUtilities.DEFAULT_ACTOR_IMAGE;
	private boolean isAlive		= false;
	
	//////////////////////////////////////////////////////////////
	/////////////////////////GENERAL//////////////////////////////
	//////////////////////////////////////////////////////////////
		
	
	public Actor() 
	{
	}

	protected abstract void addedToWorld() throws GameException;
	protected abstract void removedFromWorld() throws GameException;
	protected abstract void act() throws GameException;
	
	protected void addedToWorld(World world)  throws GameException
	{
		this.myWorld		=	world;
		world.acquireActorID(this);
		this.isAlive		=	true;
		setImage(startImage);
		addedToWorld();
	}
	protected void removedFromWorld(World world) throws GameException
	{
		this.isAlive		=	false;
		removedFromWorld();
	}
	public void doAct() throws GameException
	{
		if(isAlive) act();
	}
	
	public String toString()
	{
		return "Actor: " + getClass().getName() + "; X: " + getX() + "; Y: " + getY();
	}

	//////////////////////////////////////////////////////////////
	/////////////////////////POSITION/////////////////////////////
	//////////////////////////////////////////////////////////////
	
	public void setLocation(double dx, double dy) throws GameException
	{
		World world = getWorld();

		if(world != null)
		{			
			if((int)dx != (int)getX() || (int)dy != (int)getY())
				recalculateOccupiedCellsAfterMovement((int)dx, (int)dy);
		}
		
		this.x=dx;
		this.y=dy;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	

	//////////////////////////////////////////////////////////////
	////////////////////GENERAL GETTERS///////////////////////////
	//////////////////////////////////////////////////////////////
	

	public int getWidth() 
	{
		if(getImage() == null)
			return 0;
		
		return getImage().getWidth();
	}
	public int getHeight() 
	{
		if(getImage() == null)
			return 0;
		
		return getImage().getHeight();
	}
	public int getAbsoluteWidth() 
	{
		if(getImage() == null)
			return 0;
		
		return getImage().getAbsoluteWidth();
	}
	public int getAbsoluteHeight() 
	{
		if(getImage() == null)
			return 0;
		
		return getImage().getAbsoluteHeight();
	}
	public World getWorld() 
	{
		return myWorld;
	}
	public GodImage getImage()
	{
		return image;
	}
	public Rectangle getRect() throws GameException
	{
		GodImage image = getImage();
		
		if(image==null)
			return new Rectangle(0,0,0,0);
		
		Rectangle rect = getImage().getRect();
		rect.setLocation((int)(getX()-getWidth()/2), (int)(getY()-getHeight()/2));
		return GodUtilities.calcCorrectRect((int)getX(), (int)getY(), getWidth(), getHeight());
	}
	//////////////////////////////////////////////////////////////
	////////////////////ACTOR MANIPULATION////////////////////////
	//////////////////////////////////////////////////////////////
	

	public void setImage(String path) throws GameException
	{
		if(getWorld() == null)
		{
			throw new GameException(GameException.ERROR_USING_SETIMAGE);
		}
	
		GodImage newImage = new GodImage(path);
		
		Rectangle oldRect = getRect();
		image = newImage;
		
		recalculateOccupiedCellsAfterImageChange(oldRect, getRect());
	}

	public void resetManipulation() throws GameException
	{
		Rectangle oldRect = getRect();
		getImage().resetImage();
		recalculateOccupiedCellsAfterImageChange(oldRect, getRect());
	}
	public void setRotation(double angle) throws GameException
	{
		Rectangle oldRect = getRect();
		getImage().setRotation(angle);
		recalculateOccupiedCellsAfterImageChange(oldRect, getRect());
	}
	public void setSize(int xSize, int ySize) throws GameException
	{
		if(getImage() == null) throw new GameException(GameException.ERROR_INVALID_CONSTRUCTOR_CALL);
		if(xSize == getAbsoluteWidth() && ySize == getAbsoluteHeight()) return;
		Rectangle oldRect = getRect();
		getImage().setSize(xSize, ySize);
		recalculateOccupiedCellsAfterImageChange(oldRect, getRect());
	}
	public void setTransparency(double transparency) throws GameException
	{
		double oldTransparency = getTransparency();
		getImage().setTransparency(transparency);
		if(oldTransparency==100 || transparency == 100)
		{
			recalculateOccupiedCellsAfterImageChange(getRect(), getRect());
		}
	}
	public void rescale(double xScaleFactor, double yScaleFactor) throws GameException
	{
		setSize((int)(getImage().getScaledWidth()*xScaleFactor), 	
				(int)(getImage().getScaledHeight()*yScaleFactor));
	}
	public void rotate(double angle) throws GameException
	{
		setRotation(getRotation()+angle);
	}
	public void fade(double transparencyFade) throws GameException
	{
		setTransparency(getTransparency()+transparencyFade);
	}

	public double getTransparency()
	{
		return getImage().getTransparency();
	}
	public double getRotation()
	{
		return getImage().getRotation();
	}
	public double getXScale()
	{
		return getImage().getXScale();
	}
	public double getYScale()
	{
		return getImage().getYScale();
	}
	
	

	//////////////////////////////////////////////////////////////
	///////////////////////////OTHER//////////////////////////////
	//////////////////////////////////////////////////////////////

	public List<Actor> getIntersectingObjects(Class<? extends Actor> actorClass) throws GameException
	{
		List<Actor> intersectingObjectsAtPosition = null;
		HashSet<Actor> allIntersectingObjects = new HashSet<Actor>();
		Rectangle myRect = getRect();
		
		for(double dy = myRect.getMinY(); dy < myRect.getMaxY(); dy++)
		{
			for(double dx = myRect.getMinX(); dx < myRect.getMaxX(); dx++)
			{
				intersectingObjectsAtPosition = getWorld().getObjectsAt((int)dx,(int)dy, actorClass);
				if(intersectingObjectsAtPosition.contains(this))
				{
					for(int i = 0; i<intersectingObjectsAtPosition.size(); i++)
					{
						Actor chosenActor = intersectingObjectsAtPosition.get(i);
						if(chosenActor == this) continue;
						if(actorClass.isAssignableFrom(chosenActor.getClass()))
						{
							allIntersectingObjects.add(chosenActor);
						}
					}
				}
			}	
		}
		
		return new ArrayList<Actor>(allIntersectingObjects);
	}
	public List<Actor> getIntersectingObjects() throws GameException
	{
		return getIntersectingObjects(Actor.class);
	}
	
	public boolean intersectsWith(Actor actor)
	{
		return getWorld().doObjectsIntersect(this, actor);
	}
	public int getActorID() 
	{
		return actorID;
	}
	protected void setActorID(int actorID) 
	{
		this.actorID = actorID;
	}
	
	//////////////////////////////////////////////////////////////
	////////////////////HELP METHODS//////////////////////////////
	//////////////////////////////////////////////////////////////
	
	private void recalculateOccupiedCellsAfterMovement(int newX, int newY) throws GameException
	{
		getWorld().getCellMap().recalculateOccupiedPixelMovement(this, newX, newY);
	}
	private void recalculateOccupiedCellsAfterImageChange(Rectangle oldRect, Rectangle newRect) throws GameException
	{
		getWorld().getCellMap().recalculateOccupiedPixelImageChange(this, oldRect, newRect);
	}
	protected void setStartImage(String path)
	{
		startImage = path;
	}	
	protected boolean isInWorld()
	{
		return this.isAlive;
	}
}
