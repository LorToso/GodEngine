package godEngine.gameDependencies;

import javax.swing.JPanel;

import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private BufferedImage nextFrame = null;
	
	public GamePanel(int width, int height, int cellSize) {
		super();
		
		setPreferredSize(
				new Dimension(
						width * cellSize,
						height * cellSize)
				);
		setBackground(Color.WHITE);
		setFocusable(true);
		requestFocus();		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(nextFrame, 0, 0, getWidth(), getHeight(), null);
		
	}
	
	public void drawFrame(BufferedImage frame) 
	{
		nextFrame = frame;
		repaint();
	}
}
















