package catchMeIfYouCan;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import godEngine.gameContent.God;
import godEngine.gameContent.World;
import godEngine.gameDependencies.GameException;

public class Map extends World 
{
	ArrayList<Direction> allMoves;
	ArrayList<Clone> allClones;
	Point startPosition = new Point(350, 350);
	
	public Map() throws GameException
	{
		this(700,700,1);
		God.setFPS(60);
		allMoves = new ArrayList<Direction>();
		allClones = new ArrayList<Clone>();
		setBackground("defaultBackground2.jpg");
	}
	
	private Map(int width, int height, int cellSize) throws GameException {
		super(width, height, cellSize);
	}

	
	protected void gameStopped() throws GameException 
	{
	}

	protected void preGameMode() throws GameException 
	{
	}

	protected void initializeWorld() throws GameException 
	{
		addObject(startPosition.x, startPosition.y, new Player());
		//addObject(getWidth()/2, getHeight()/2, new Obstacle());
	}
	protected void act() throws GameException 
	{
		if(God.isKeyDown(KeyEvent.VK_SPACE))
		{
			addObject(startPosition.x, startPosition.y, new Clone());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Direction> registerClone(Clone clone) 
	{
		allClones.add(clone);
		return allMoves;
	}
	public void addMove(Direction dir)
	{
		allMoves.add(dir);
	}
	

}
