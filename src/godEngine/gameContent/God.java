package godEngine.gameContent;

import godEngine.gameDependencies.GameException;
import godEngine.gameDependencies.GodMouseInfo;
import godEngine.gameDependencies.GraphicEngine;
import godEngine.gameDependencies.InputEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;


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
	
	public static void delay(long milliSeconds) throws GameException
	{
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			throw new GameException(GameException.ERROR_DURING_WAITTIME);
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
	
	public God(String worldPath) throws GameException
	{		
		myWorld = createWorld(worldPath);
		GodUtilities.setWorld(myWorld);
	}
	public void startGame() throws GameException
	{
		start();
		loadResources();
		myWorld.initialize();
		preGameMode();
		gameLoop();
	}
	private void preGameMode() throws GameException 
	{
		myWorld.preGameMode();
	}
	private void gameLoop() throws GameException 
	{
		long startTime;
		long elapsedTime;
		long averageTime = 0;
		long frameTime;

		while(gameRunning)			// This double loop is made to be able to restart the game after it stopped.
		{			
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
					currentFPS = (int)(1.0/(averageTime/(double)(timeMeasurement)));
				}
			
				if(elapsedTime < frameTime)					// Maximum Frames erzeugen
				{	
					try {
						Thread.sleep((frameTime-elapsedTime)/timeMeasurementToMillis);
					} catch (InterruptedException e) 
					{
						throw new GameException(GameException.ERROR_DURING_WAITTIME);
					}				
				}
			}
			myWorld.gameStopped();
		}
	}
	private void actorLoop() throws GameException
	{
        List<Actor> allActors = myWorld.getObjects(Actor.class);
        Vector<Actor> tempActors = new Vector<>(allActors);


        for (Actor chosenActor : tempActors) {
            if(chosenActor.getWorld() != null)
                chosenActor.act();
        }
	}
	public World getWorld()
	{
		return myWorld;
	}
	public void loadResources() throws GameException
	{
		GodSound.preLoadSounds();
	}
	
	@SuppressWarnings("unchecked")
	public World createWorld(String worldPath) throws GameException
	{
		Class<World> worldClass;
		World world;
		try {
			worldClass = (Class<World>) Class.forName(worldPath);
			world = worldClass.newInstance();
		} catch (ClassNotFoundException e) 
		{
			throw new GameException(GameException.ERROR_WORLD_NOT_FOUND);
		} catch (InstantiationException | IllegalAccessException e) 
		{
			throw new GameException(GameException.ERROR_NO_VALID_WORLD_CONSTUCTOR);
		}
		return world;	
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
	public void repaint() throws GameException
	{
		graphicEngine.repaint(myWorld);
	}
	
	// Input Engine Stuff
	public static boolean isKeyDown(int keyCode) throws GameException
	{
		if(inputEngine == null )
			throw new GameException(GameException.ERROR_USING_UNIMPLEMENTED_INPUT_ENGINE);
		
		return inputEngine.isKeyDown(keyCode);
	}
	public static HashMap<Integer, Boolean> getKeyState() throws GameException
	{
		if(inputEngine == null )
			throw new GameException(GameException.ERROR_USING_UNIMPLEMENTED_INPUT_ENGINE);
		
		return inputEngine.getKeyState();
	}
	public static GodMouseInfo getMouseInfo() throws GameException
	{
		if(inputEngine == null )
			throw new GameException(GameException.ERROR_USING_UNIMPLEMENTED_INPUT_ENGINE);
		
		return inputEngine.getMouseInfo();
	}
}
