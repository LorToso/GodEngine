package godEngine.gameDependencies;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class GodImage
{
	private String imagePath;
	private BufferedImage image;
	private int occupiedCells[][];
	private int cellSize = 0;

	public GodImage(String imagePath, BufferedImage image, int cellSize)
	{
		this.imagePath = imagePath;
		this.image = image;
		this.cellSize = cellSize;
		calculateUsedPixels();
	}
	public GodImage(String imagePath, int cellSize)
	{
		this.imagePath = imagePath;
		this.cellSize = cellSize;
		loadImage(imagePath);
	}
		
	
	private void loadImage(String imagePath)
	{
		try {
			image = ImageIO.read(
					getClass().getResourceAsStream(imagePath)
			);
		}
		catch(Exception e) {
			new GameException(3).printMessage();
			e.printStackTrace();
			System.exit(1);
		}
		calculateUsedPixels();
	}
	private void calculateUsedPixels()
	{
		int width = getWidth();
		int height = getHeight();

		occupiedCells = new int[getWidth()/cellSize][getHeight()/cellSize];
		int imageColors[] = new int[width*height];
		image.getRGB(0, 0, width, height, imageColors, 0, width);
		
		for(int y = 0; y<height; y++)
		{
			for(int x = 0; x<width; x++)
			{
				// Falls die Zelle schon belegt ist, zur nächsten Zelle gehen
				if(occupiedCells[x/cellSize][y/cellSize] != 0) continue;

				occupiedCells[x/cellSize][y/cellSize] = (imageColors[x+y*width] >> 24) & 0x000000FF;
			}
		}
	}
	

	public BufferedImage getBufferedImage()
	{
		return image;
	}
	public String getImagePath()
	{
		return imagePath;
	}
	public int getWidth()
	{
		return image.getWidth();
	}
	public int getHeight()
	{
		return image.getHeight();
	}
	public int[][] getOccupiedCells()
	{
		return occupiedCells;
	}
	public boolean isTransparentAt(int dx, int dy)
	{
		return getOccupiedCells()[dx][dy] == 0;		
	}
	
}
