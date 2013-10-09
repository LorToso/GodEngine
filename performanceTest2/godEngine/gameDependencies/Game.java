package godEngine.gameDependencies;

import godEngine.gameContent.God;

import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args) 
	{
		if(args.length != 2)
		{
			new GameException(4).printMessage();
			System.exit(1);
		}
		
		
		JFrame window 			= new JFrame(args[0]);
		God god 				= new God(args[1]);
		
		GamePanel gamePanel		= new GamePanel(god.getWorld().getWidth(), god.getWorld().getHeight(), god.getWorld().getCellSize());

		addGraphicEngine(god, gamePanel);
		addInputEngine(god, gamePanel);
		
		window.setContentPane(gamePanel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
		god.startGame();
	}

	private static void addGraphicEngine(God god, GamePanel gamePanel) 
	{
		GraphicEngine graphicEngine = new GraphicEngine(god.getWorld(), gamePanel);
		god.addGraphicEngine(graphicEngine);
	}
	private static void addInputEngine(God god, GamePanel gamePanel) 
	{
		InputEngine inputEngine= new InputEngine();
		gamePanel.addKeyListener(inputEngine);
		gamePanel.addMouseListener(inputEngine);
		gamePanel.addMouseMotionListener(inputEngine);
		gamePanel.addMouseWheelListener(inputEngine);	
		god.addInputEngine(inputEngine);			
	}
	
}
