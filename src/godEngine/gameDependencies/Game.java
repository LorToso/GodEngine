package godEngine.gameDependencies;


import godEngine.gameContent.God;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game {
	public static final int NO_INPUT			= 0x0;
	public static final int KEYBOARD_INPUT		= 0x1;
	public static final int MOUSE_INPUT 		= 0x2;
	public static final int MOUSE_WHEEL_INPUT 	= 0x4;

	private static final String DEFAULT_GRAPHICS= "godEngine.gameDependencies.GodGraphicsEngine";
	private static final String TEXT_GRAPHICS	= "godEngine.gameDependencies.GodTextGraphics";
	
	private static String title					= "Title not set.";
	private static String world					= null;
	private static String graphics				= DEFAULT_GRAPHICS;
	private static int input					= NO_INPUT;
	
	public static boolean TEST_ACCELEATION		= false;
	
	public static void main(String[] args) 
	{
		if(TEST_ACCELEATION) System.setProperty("sun.java2d.opengl", "True"); // TODO: Check if this speeds up anything
		try
		{
			parseArguments(args);
		
			if(world == null)
			{
				throw new GameException(GameException.ERROR_NO_WORLD_SPECIFIED);
			}

			JFrame window 				= new JFrame(title);
			GamePanel gamePanel			= new GamePanel();
		
			GraphicEngine graphicEngine = createGraphicEngine(graphics);
			InputEngine inputEngine 	= createInputEngine(input);
			
			
			God god 					= new God(world);
		
			gamePanel.initialize(god);
			graphicEngine.initialize(gamePanel, god);
			inputEngine.initialize(gamePanel, god);

			god.addGraphicEngine(graphicEngine);
			god.addInputEngine(inputEngine);
		
			prepareWindow(window, gamePanel);
			
			god.startGame();
		}
		catch(GameException e)
		{
			GameException.crashAndBurn(e);
		}
	}
	private static void parseArguments(String[] args) 
	{
		for(int i = 0; i<args.length; i++)
		{
			String argument = args[i];
			if(argument.startsWith("world="))
			{
				world = argument.substring(6);
			}
			else if(argument.startsWith("title="))
			{
				title = argument.substring(6);
			}
			else if(argument.startsWith("graphics="))
			{
				argument = argument.substring(9);
				if(argument.equals("default"))
					graphics = DEFAULT_GRAPHICS;
				else if(argument.equals("text"))
					graphics = TEXT_GRAPHICS;
				else
					graphics = argument;
			}
			else if(argument.startsWith("input="))
			{
				argument = argument.substring(6);
				if(argument.equals("default"))
					input = NO_INPUT;
				else
				{
					if(argument.contains("keyboard"))
						input |= KEYBOARD_INPUT;
					if(argument.contains("mouse"))
						input |= MOUSE_INPUT;
					if(argument.contains("mousewheel"))
						input |= MOUSE_WHEEL_INPUT;
				}
			}
		}
	}
	private static void prepareWindow(JFrame window, JPanel gamePanel)
	{
		window.setContentPane(gamePanel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		window.setLocationRelativeTo(null);
	}
	
	@SuppressWarnings("unchecked")
	private static GraphicEngine createGraphicEngine(String newGraphics) throws GameException
	{	
		Class<GraphicEngine> graphicEngineClass = null;
		GraphicEngine ge 						= null;
		
		try {
			graphicEngineClass 					= (Class<GraphicEngine>) Class.forName(newGraphics);
			ge 									= (GraphicEngine) graphicEngineClass.newInstance();
		} catch (ClassNotFoundException e) 
		{
			throw new GameException(GameException.ERROR_GRAPHIC_ENGINE_NOT_FOUND);
		} catch (InstantiationException | IllegalAccessException e) 
		{
			throw new GameException(GameException.ERROR_NO_VALID_GRAPHIC_ENGINE_CONSTUCTOR);
		}
		return ge;
	}
	private static InputEngine createInputEngine(int inputParameters) 
	{
		return new InputEngine(inputParameters);
	}
	
	
}
