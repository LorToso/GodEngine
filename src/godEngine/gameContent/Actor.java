package godEngine.gameContent;

import godEngine.gameDependencies.GameException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public abstract class Actor 
{
    private static int staticActorID = 0;


    private World myWorld 		= null;
	private double x 			= 0;
	private double y 			= 0;
    private double width        = 0;
    private double height       = 0;
	private final int actorID;
    private GodImage image 		= null;

    public Actor()
    {
        actorID = staticActorID++;
    }


	protected abstract void addedToWorld() throws GameException;
	protected abstract void removedFromWorld() throws GameException;
	protected abstract void act() throws GameException;

    protected void addedToWorld(World world)  throws GameException
    {
        this.myWorld = world;

        addedToWorld();

        if(image == null)
            setImage(GodUtilities.DEFAULT_ACTOR_IMAGE);
    }
    protected void beingRemovedFromWorld()  throws GameException
    {
        myWorld = null;
        removedFromWorld();
    }

	public String toString()
	{
		return "Actor: " + getClass().getName() + "; X: " + getX() + "; Y: " + getY();
	}

	
	public void setLocation(double dx, double dy) throws GameException
	{
		this.x=dx;
		this.y=dy;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}

	public World getWorld()
	{
		return myWorld;
	}
	public GodImage getImage()
	{
		return image;
	}
	public Rectangle getRect()
	{
        return new Rectangle((int)x,(int)y,(int)width,(int)height);
    }
    public void setSize(double width, double height)
    {
        this.width = width;
        this.height = height;
    }

	public void setImage(String path) throws GameException
	{
		image = new GodImage(path);
	}

	public ArrayList<Actor> getIntersectingObjects(Class<? extends Actor> actorClass)
	{
        ArrayList<Actor> allActors =  getWorld().getObjectsInRect(getRect(), actorClass);
        allActors.remove(this);
        return allActors;
	}
	public List<Actor> getIntersectingObjects()
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
}
