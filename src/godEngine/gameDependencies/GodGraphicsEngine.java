package godEngine.gameDependencies;

import godEngine.gameContent.Actor;
import godEngine.gameContent.God;
import godEngine.gameContent.GodImage;
import godEngine.gameContent.SimpleImage;
import godEngine.gameContent.World;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;


public class GodGraphicsEngine implements GraphicEngine
{
	private Graphics2D graphic 	= null;
	private BufferedImage frame = null;
	private World world 		= null;
	private GamePanel gamePanel = null;
	
	public GodGraphicsEngine() throws GameException
	{
	}

	public void initialize(GamePanel panel, God god) throws GameException 
	{
		this.world		=	god.getWorld();
		
		initializeImages();
		loadResources();
		
		if(Game.TEST_ACCELERATION)
		{
			this.frame		=	SimpleImage.createQuickImage(this.world.getWidth()  * this.world.getCellSize(), this.world.getHeight()  * this.world.getCellSize());
		}
		else
		{
			this.frame		=	new BufferedImage
					(
							this.world.getWidth()  * this.world.getCellSize(),
							this.world.getHeight() * this.world.getCellSize(), 
							BufferedImage.TYPE_INT_ARGB
					);
		}
		this.graphic 	= 	(Graphics2D)frame.getGraphics();
		this.gamePanel	=	panel;		
	}

	public void loadResources() throws GameException 
	{
		GodImage.preLoadImages();
	}
	public synchronized void repaint(World world) throws GameException 
	{
		createFrame(world.getObjects(Actor.class));
		paintFrame(frame);
	}
	
	
	
	private void initializeImages()
	{
		GodImage.initialize(world.getCellSize());
		SimpleImage.initialize(world.getCellSize());
	}
	
	private void createFrame(List<Actor> allActors)
	{
		paintBackground();
		
		for(Actor chosenActor : allActors)
		{
			paintActor(chosenActor);
		}
	}
	private void paintFrame(BufferedImage frame) throws GameException
	{
		gamePanel.drawFrame(frame);
	}
	
	private void paintBackground()
	{
		if(world.getBackground() != null)
		{
			paintImage(world.getBackground(), 0, 0);
		}
	}
	
	private void paintActor(Actor chosenActor) 
	{
		int x = (int) (chosenActor.getX()*world.getCellSize()-chosenActor.getAbsoluteWidth()/2);
		int y = (int) (chosenActor.getY()*world.getCellSize()-chosenActor.getAbsoluteHeight()/2);
		
		paintImage(chosenActor.getImage(), x, y);
	}
	private void paintImage(GodImage chosenImage, int x, int y) 
	{
		int width = chosenImage.getAbsoluteWidth();
		int height = chosenImage.getAbsoluteHeight();
		
		if(x > frame.getWidth() || y > frame.getHeight() || x+width < 0 || y+height < 0 ) return;
		
		graphic.drawImage(chosenImage.getImage().getBufferedImage(), x, y, chosenImage.getAbsoluteWidth(), chosenImage.getAbsoluteHeight(), null);		
	}
}

