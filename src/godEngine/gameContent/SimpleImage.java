package godEngine.gameContent;

import godEngine.gameDependencies.Game;
import godEngine.gameDependencies.GameException;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class SimpleImage
{
	
	/**
	 * Creates a SimpleImage from a File.
	 * @param file The File that contains the Image.
	 * @return A new SimpleImage which represents the Image that has been loaded from the file.
	 * @throws GameException Exception is thrown if the image could not be loaded.
	 */
	protected static SimpleImage createSimpleImageFromFile(File file) throws GameException
	{
		return new SimpleImage(loadImageFromPath(file.getPath()), file.getPath());
	}
	
	/**
	 * Loads an Image from the specified path. 
	 * @param imagePath	The path the image is to be loaded from.
	 * @return 			A BufferedImage containing the loaded Image. 
	 * @throws GameException Exception is thrown if the image could not be loaded.
	 */
	protected static BufferedImage loadImageFromPath(String imagePath) throws GameException
	{
		String completePath = GodUtilities.getCompletePath(imagePath);
		File inputFile = new File(completePath);

        String a = inputFile.getName();
        String b = inputFile.getAbsolutePath();


		BufferedImage loadedImage = null;
		try { // TODO Check whats going on
			BufferedImage lImage = ImageIO.read(inputFile);
			loadedImage = new BufferedImage(lImage.getWidth(), lImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			loadedImage.getGraphics().drawImage(lImage, 0, 0, null);
		} catch (IOException e) {
			throw new GameException(GameException.ERROR_NOT_A_VALID_IMAGE);
		}

		if(Game.TEST_ACCELEATION) return createQuickImage(loadedImage);
		else return loadedImage;
	}
	protected static BufferedImage createQuickImage(BufferedImage oldImage)
	{
		// TODO: This is to test if it makes an actual difference if we create a "good" image
		BufferedImage quickImage = GodImage.graphicsConfiguration.createCompatibleImage(oldImage.getWidth(), oldImage.getHeight(), Transparency.TRANSLUCENT);
		quickImage.getGraphics().drawImage(oldImage, 0, 0, null);
		return quickImage;
	}
	public static BufferedImage createQuickImage(int width, int height)
	{
		// TODO: This is to test if it makes an actual difference if we create a "good" image
		BufferedImage quickImage = GodImage.graphicsConfiguration.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		return quickImage;
	}
	
	/**
	 * Copies a bufferedImage.
	 * @param bi 	Image that is supposed to be copied
	 * @return		A copy of the image.
	 */
	protected static BufferedImage copyBufferedImage(BufferedImage bi) 
	{
		 ColorModel cm 					= bi.getColorModel();
		 boolean isAlphaPremultiplied 	= cm.isAlphaPremultiplied();
		 WritableRaster raster 			= bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}	
	public static void initialize(int cellSize)
	{
		// TODO: Delete?
	}
	
	private BufferedImage myImage	=	null;
	private String imagePath		=	null;
	private int absoluteWidth		=	0;
	private int absoluteHeight		=	0;
	private int cellWidth			=	0;
	private int cellHeight			=	0;
	private int occupiedCells[][];
	
	public SimpleImage(BufferedImage image) throws GameException 
	{
		if(GodImage.worldCellSize==0)
			throw new GameException(GameException.ERROR_IMAGE_NOT_INITIALIZED);
		
		this.myImage		= image;
		this.absoluteHeight	= image.getHeight();
		this.absoluteWidth	= image.getWidth();
		
		calculateCellSize(absoluteWidth, absoluteHeight);
		
		calculateOccupiedCells();
	}
	public SimpleImage(BufferedImage image, String path) throws GameException 
	{
		this(image);
		imagePath = path;
	}
	public SimpleImage(String path) throws GameException 
	{
		this(loadImageFromPath(path));
		imagePath = path;
	}
	/**
	 * A private Constructor which is used for the doClone() method. It does not recalculate the occupied cells.
	 * @param image			The BufferedImage.
	 * @param path			The path that is supposed to be set to the image.
	 * @param cellSize		The cellSize of the World the image is going to be in.
	 * @param occupiedCells The cells the image occupies.
	 */
	private SimpleImage(BufferedImage image, String path, int[][] occupiedCells)
	{
		this.myImage		= image;
		this.imagePath		= path;
		this.absoluteHeight	= image.getHeight();
		this.absoluteWidth	= image.getWidth();
		calculateCellSize(absoluteWidth, absoluteHeight);
		this.occupiedCells	= occupiedCells;
	}

	private void calculateCellSize(int absoluteWidth, int absoluteHeight) 
	{
		if(absoluteWidth < GodImage.worldCellSize)
			this.cellWidth	= 1;
		else
			this.cellWidth 	= (int) Math.ceil(absoluteWidth / (double)GodImage.worldCellSize);
			
		if(absoluteHeight < GodImage.worldCellSize)
			this.cellHeight = 1;
		else
 			this.cellHeight	 = (int) Math.ceil(absoluteHeight / (double)GodImage.worldCellSize);
	}
	private void calculateOccupiedCells()
	{		
		occupiedCells	 	= new int[cellWidth][cellHeight];
		int imageColors[] 	= new int[absoluteWidth*absoluteHeight];
		myImage.getRGB(0, 0, absoluteWidth, absoluteHeight, imageColors, 0, absoluteWidth);
		
		for(int y = 0; y<absoluteHeight; y++)
		{
			for(int x = 0; x<absoluteWidth; x++)
			{
				// Falls die Zelle schon belegt ist, zur n�chsten Zelle gehen
				if(occupiedCells[x/GodImage.worldCellSize][y/GodImage.worldCellSize] != 0) continue;

				occupiedCells[x/GodImage.worldCellSize][y/GodImage.worldCellSize] = (imageColors[x+y*absoluteWidth] >> 24) & 0x000000FF;
			}
		}
	}
	
	/** 
	 * Clones a SimpleImage by creating a deep copy of the occupiedCell Matrix and a deep copy of the actual BufferedImage.
	 * @return A deep copy of the SimpleImage
	 */
	public SimpleImage doClone()
	{
		BufferedImage newImage 	= copyBufferedImage(myImage);
		int[][] copyOfOccupiedCells = new int[occupiedCells.length][occupiedCells[0].length];
		for(int i=0; i< copyOfOccupiedCells.length; i++)
			copyOfOccupiedCells[i] = Arrays.copyOf(occupiedCells[i], occupiedCells[i].length);
		
		SimpleImage newSI 		= new SimpleImage(newImage, imagePath, occupiedCells);
		return newSI;
	}
	public boolean isTransparentAt(int dx, int dy)
	{
		return occupiedCells[dx][dy] == 0;
	}
	public BufferedImage getBufferedImage()
	{
		return myImage;
	}
	public String getPath()
	{
		return imagePath;
	}
	public int getAbsoluteWidth()
	{
		return absoluteWidth;
	}
	public int getAbsoluteHeight()
	{
		return absoluteHeight;
	}
	public int getCellWidth()
	{
		return cellWidth;
	}
	public int getCellHeight()
	{
		return cellHeight;
	}
}
