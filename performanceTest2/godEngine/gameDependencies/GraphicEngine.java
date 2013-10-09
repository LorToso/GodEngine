package godEngine.gameDependencies;

import godEngine.gameContent.Actor;
import godEngine.gameContent.World;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;


public class GraphicEngine 
{
	private Graphics2D 		graphic = null;
	private BufferedImage 	frame = null;
	private World 			world = null;
	private GamePanel 		gamePanel = null;
	
	
	public GraphicEngine(World world, GamePanel gamePanel)
	{
		this.frame	= new BufferedImage
				(
						world.getWidth()*world.getCellSize(),
						world.getHeight()*world.getCellSize(), 
						BufferedImage.TYPE_INT_RGB
				);
		
		this.graphic 	= 	(Graphics2D)frame.getGraphics();
		this.world		=	world;
		this.gamePanel	=	gamePanel;
	}
	
	public void paintWorld(List<Actor> allActors)
	{
		createFrame(allActors);
		paintFrame(frame);
	}
	
	private void createFrame(List<Actor> allActors)
	{
		//clearScreen();
		paintBackground();
		
		GodImage chosenImage = null;
		for(Actor chosenActor : allActors)
		{
			chosenImage = chosenActor.getImage();
			paintImage(chosenImage, (int)chosenActor.getX(), (int)chosenActor.getY());
		}
	}
	private void paintFrame(BufferedImage frame)
	{
		gamePanel.drawFrame(frame);
	}
	
	
	@SuppressWarnings("unused")
	private void clearScreen()
	{
		graphic.clearRect(0, 0, world.getWidth()-1, world.getHeight()-1);
	}
	
	private void paintBackground()
	{
		if(world.getBackground() != null)
		{
			paintImage(world.getBackground(), 0, 0, world.getWidth()*world.getCellSize(), world.getHeight()*world.getCellSize());
		}
	}
	private void paintImage(GodImage chosenImage, int x, int y) {
		graphic.drawImage(chosenImage.getBufferedImage(), x*world.getCellSize(), y*world.getCellSize(), null);		
	}
	private void paintImage(GodImage chosenImage, int x, int y, int width, int height) {
		graphic.drawImage(chosenImage.getBufferedImage(), x*world.getCellSize(), y*world.getCellSize(), width, height, null);		
	}
}

