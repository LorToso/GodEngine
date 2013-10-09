package godEngine.gameContent;

import godEngine.gameDependencies.GodImage;

public abstract class Actor 
{
	private World myWorld = null;
	private double x = 0;
	private double y = 0;
	private double rotation = 0;
	private GodImage image = null;
	private int actorID = 0;
	
	
	public Actor()
	{
	}
	protected void addedToWorld(World world)
	{
		this.myWorld = world;
		world.acquireActorID(this);
		addedToWorld();
	}
	private void addedToWorld()
	{
		
	}
	
	public void act()
	{
		
	}
	
	
	public void setLocation(double dx, double dy)
	{
		World world = getWorld();

		if(world != null)
		{
			if(dx < 0) 					dx = 0;
			if(dy < 0) 					dy = 0;
			if(dx > world.getWidth()-1)	dx = world.getWidth()-1;
			if(dy > world.getHeight()-1)dy = world.getHeight()-1;
			getWorld().recalculateOccupiedPixels(this);
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
	public int getWidth() {
		return getImage().getWidth();
	}
	public int getHeight() {
		return getImage().getHeight();
	}
	public double getRotation()
	{
		return rotation;
	}
	public void setRotation(double rotation)
	{
		this.rotation = rotation;
	}
	public World getWorld() {
		return myWorld;
	}
	public GodImage getImage()
	{
		return image;
	}
	public void setImage(String path)
	{
		if(image == null || !path.equals(image.getImagePath()))
		{
			image = new GodImage(path, getWorld().getCellSize());
		}
		getWorld().recalculateOccupiedPixels(this);
	}
	public void setImage(GodImage image)
	{
		this.image = image;		
	}
	public int getActorID() {
		return actorID;
	}
	public void setActorID(int actorID) {
		this.actorID = actorID;
	}
}
