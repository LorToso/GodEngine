package godEngine.gameContent;
import godEngine.gameDependencies.GodMouseInfo;
import godEngine.gameDependencies.GraphicEngine;
import godEngine.gameDependencies.GameException;
import godEngine.gameDependencies.InputEngine;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class God
{
	private static final int 	timeMeasurement			=	1000000000;
	private static final int 	timeMeasurementToMillis	=	1000000;
	private static boolean 		gameRunning 			= 	false;
	private static int			maximumFPS				=	60;
	
	private static boolean		calcFPS					=	false;
	private static int			currentFPS				=	0;
	
	private static GraphicEngine graphicEngine			=	null;
	private static InputEngine 	inputEngine				=	null;
	
	
	
	// Some General Stuff
	
	public static void delay(long milliSeconds)
	{
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			new GameException(6).printMessage();
			e.printStackTrace();
			System.exit(1);
		}
	}
	public static int getRandomNumber(int max)
	{
		return (int) (Math.random()*max);
	}
	public static void setFPS(int maxFPS)
	{
		maximumFPS = maxFPS;
	}
	public static void start()
	{
		gameRunning = true;
	}
	public static void stop()
	{
		gameRunning = false;
	}
	public static void calcFPS(boolean doCalc)
	{
		calcFPS=doCalc;
	}
	public static int getFPS()
	{
		return currentFPS;
	}
	
	
	
	private World myWorld = null;
	
	@SuppressWarnings("unchecked")
	public God(String worldToCreate)
	{		
		Class<World> worldClass = null;
		World world = null;
		
		try {
			worldClass = (Class<World>) Class.forName(worldToCreate);
			world = (World)worldClass.newInstance();
		} catch (Exception e) {
			GameException ge = new GameException(2);
			ge.printMessage();
			e.printStackTrace();
			System.exit(1);
		}
		myWorld = world;		
	}
	public void startGame()
	{
		start();
		gameLoop();
	}
	private void gameLoop()
	{
		long startTime		= 	0;
		long elapsedTime	= 	0;		
		long averageTime	=	0;
		long frameTime		=	0;
		
		while(gameRunning)
		{			
			startTime		=	System.nanoTime();
			
			repaint();
			myWorld.act();
			actorLoop();
			
			elapsedTime		=	System.nanoTime()-startTime;
			frameTime		=	timeMeasurement/maximumFPS;
			
			if(calcFPS)
			{
				averageTime = (9*averageTime+elapsedTime)/10;
				currentFPS = (int)(1.0/(averageTime/(double)(1000000000)));
			}
			
			if(elapsedTime < frameTime)					// Maximum Frames erzeugen
			{	
				try {
					Thread.sleep((frameTime-elapsedTime)/timeMeasurementToMillis);
				} catch (InterruptedException e) {
					new GameException(6).printMessage();
					System.exit(1);
				}				
			}
		
		}
		myWorld.gameStopped();
	}
	private void actorLoop()
	{
		ArrayList<Actor> placedActors = (ArrayList<Actor>) myWorld.getObjects(Actor.class);
		Actor chosenActor = null;
		
		for(int i=0; i<placedActors.size(); i++ )
		{
			chosenActor = placedActors.get(i);
			chosenActor.act();
		}
	}
	public World getWorld()
	{
		return myWorld;
	}
	

	// General Engine Stuff
	public void addGraphicEngine(GraphicEngine ge)
	{
		graphicEngine = ge;
	}
	public void addInputEngine(InputEngine ie)
	{
		inputEngine = ie;
	}	
	
	
	
	
	// Graphic Engine Stuff
	public void repaint()
	{
		repaint(myWorld.getObjects(Actor.class));
	}
	public static void repaint(List<Actor> allActors)
	{
		graphicEngine.paintWorld(allActors);
	}
	
	// Input Engine Stuff
	public static boolean isKeyDown(int keyCode)
	{
		return inputEngine.isKeyDown(keyCode);
	}
	public static HashMap<Integer, Boolean> getKeyState()
	{
		return inputEngine.getKeyState();
	}
	public static GodMouseInfo getMouseInfo()
	{
		return inputEngine.getMouseInfo();
	}

}
