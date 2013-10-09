package godEngine.gameDependencies;

import java.util.ArrayList;

import godEngine.gameContent.Actor;
import godEngine.gameContent.God;
import godEngine.gameContent.World;

public class GodTextGraphics implements GraphicEngine
{
	char worldToPaint[][];
	World world;
	
	
	@Override
	public void repaint(World world) throws GameException 
	{
		createFrame();
		for(int y = 0; y < worldToPaint[0].length; y++)
		{
			for(int x = 0; x < worldToPaint.length; x++)
			{
				if(worldToPaint[x][y] == '\u0000') 
					worldToPaint[x][y] = ' ';
				System.out.print(worldToPaint[x][y]);
			}
			System.out.println("");
		}
	}
	
	private void createFrame()
	{
		ArrayList<Actor> allActors = (ArrayList<Actor>) world.getObjects(Actor.class);
		for(Actor chosenActor : allActors)
		{
			paintActor(chosenActor);
		}
	}

	private void paintActor(Actor chosenActor) 
	{
		int dx = (int) (chosenActor.getX()-chosenActor.getWidth()/2);
		int dy = (int) (chosenActor.getY()-chosenActor.getHeight()/2);

		char actorName = chosenActor.getClass().getName().charAt(0);
		
		for(int y = dy; y < dy + chosenActor.getHeight(); y++)
		{
			for(int x = dx; x < dx + chosenActor.getWidth(); x++)
			{
				if(!chosenActor.getImage().isTransparentAt(x-dx, y-dy))
				{
					if(x<0 || x >= worldToPaint.length || y<0 || y >= worldToPaint[0].length) 
						continue;
						
					worldToPaint[x][y] = actorName;
				}
			}
		}
		
	}
	
	
	@Override
	public void initialize(GamePanel panel, God god) throws GameException 
	{	
		this.world = god.getWorld();
		worldToPaint = new char[god.getWorld().getWidth()][god.getWorld().getHeight()];
	}

	@Override
	public void loadResources() throws GameException {		
	}

}
