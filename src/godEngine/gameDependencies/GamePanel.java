package godEngine.gameDependencies;

import godEngine.gameContent.God;
import godEngine.gameContent.World;

import javax.swing.JPanel;

import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private BufferedImage nextFrame = null;
	private boolean isInitialized	= false;
	
	public GamePanel() {
		super();
		setBackground(Color.WHITE);
		setFocusable(true);
		requestFocus();		
	}
	public void initialize(God god)
	{
		World w = god.getWorld();
		setPreferredSize(
				new Dimension(
						w.getWidth()  * w.getCellSize(),
						w.getHeight() * w.getCellSize())
				);
		isInitialized = true;
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(nextFrame, 0, 0, getWidth(), getHeight(), null);
		
	}
	public void drawFrame(BufferedImage frame) throws GameException
	{
		if(!isInitialized)
			throw new GameException(GameException.ERROR_PANEL_NOT_INITIALIZED);
		nextFrame = frame;
		repaint();
	}
}