package godEngine.gameDependencies;

import godEngine.gameContent.Actor;
import godEngine.gameContent.God;
import godEngine.gameContent.GodImage;
import godEngine.gameContent.SimpleImage;
import godEngine.gameContent.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;


public class GodGraphicsEngine implements GraphicEngine
{
	private Graphics2D graphic 	= null;
	private BufferedImage frame = null;
	private World world 		= null;
	private GamePanel gamePanel = null;


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
			paintImage(world.getBackground(), new Rectangle(0,0, frame.getWidth(), frame.getHeight()));
		}
	}
	
	private void paintActor(Actor chosenActor) 
	{
		paintImage(chosenActor.getImage(), chosenActor.getRect());
	}
	private void paintImage(GodImage chosenImage, Rectangle rect)
	{
		int width = rect.width;
		int height = rect.height;
		
		if(rect.x > frame.getWidth() || rect.y > frame.getHeight() || rect.x+width < 0 || rect.y+height < 0 ) return;
		
		graphic.drawImage(chosenImage.getImage().getBufferedImage(), rect.x, rect.y, width, height, null);
	}
}

