package godEngine.gameContent;


import godEngine.gameDependencies.Game;
import godEngine.gameDependencies.GameException;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GodImage
{
	protected static int 					worldCellSize			= 1;
	protected static GraphicsEnvironment 	graphicsEnvironment 	= null;
	protected static GraphicsDevice 		graphicsDevice 			= null;
	protected static GraphicsConfiguration 	graphicsConfiguration 	= null;
	
	private SimpleImage 	correctImage	= null;
	private SimpleImage		originalImage 	= null;
	
	private double			rotation		= 0;
	private double			xScale			= 1;
	private double			yScale			= 1;
	private double			transparency	= 0;

	private Point			originalSize	= null;
	private Point			scaledSize		= null;
	private Point			rotatedSize		= null;
	
	
	/////////////////////////////////////////////////////////////////
	///////////////////////// STATICS ///////////////////////////////
	/////////////////////////////////////////////////////////////////	
	
	private static HashMap<String, SimpleImage> preLoadedImages = null;
	
	public static void preLoadImages() throws GameException
	{
		preLoadedImages = new HashMap<String, SimpleImage>();
		
		ArrayList<File> allFiles = GodUtilities.getAllFilesInDirectory(new File(GodUtilities.IMAGE_FOLDER_PATH));
		for(File file : allFiles)
		{
			preLoadedImages.put(file.getPath(), SimpleImage.createSimpleImageFromFile(file));
		}
	}
	private static SimpleImage loadImage(String imagePath) throws GameException
	{
		imagePath = GodUtilities.getCompletePath(imagePath);
		if(preLoadedImages!= null && preLoadedImages.containsKey(imagePath))
		{
			return preLoadedImages.get(imagePath);
		}
		return new SimpleImage(imagePath);
	}
	public static void initialize(int cellSize)
	{
		GodImage.worldCellSize = cellSize;
		graphicsEnvironment 		= GraphicsEnvironment.getLocalGraphicsEnvironment();
		graphicsDevice 				= graphicsEnvironment.getDefaultScreenDevice();
		graphicsConfiguration 		= graphicsDevice.getDefaultConfiguration();
	}
	
	/////////////////////////////////////////////////////////////////
	///////////////////////// CLASS /////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	
	public GodImage(BufferedImage image, String imagePath) throws GameException
	{
		this.correctImage	= new SimpleImage(image, imagePath);
		recalculateDimensions();
	}
	public GodImage(String imagePath) throws GameException
	{
		this.correctImage	= loadImage(imagePath);
		recalculateDimensions();
	}
	

	public void resetImage() throws GameException
	{
		this.rotation 		= 0;
		this.xScale			= 1;
		this.yScale			= 1;
		this.transparency	= 0;
		manipulateImage();
	}
	public void setRotation(double angle) throws GameException
	{
		// Correct angle to be between 0 and 360
		while(angle<0) 		angle+=360;
		while(angle>=360) 	angle-=360;
		
		this.rotation = angle;
		manipulateImage();
	}
	public void setSize(int width, int height) throws GameException
	{
		if(width<=0 || height<=0)
			throw new GameException(GameException.ERROR_INVALID_DIMENSIONS);

		this.xScale = width  / (double) originalSize.x;
		this.yScale = height / (double) originalSize.y;
		manipulateImage();
	}
	public void setTransparency(double transparency) throws GameException
	{
		if(transparency < 0) 	transparency = 0;
		if(transparency > 100)	transparency = 100;
		
		this.transparency = transparency;
		manipulateImage();
	}
	private void manipulateImage() throws GameException
	{
		// If not manipulation has been made yet, the originalImage is created
		if(originalImage == null)		
			originalImage = correctImage.clone();
		correctImage = new SimpleImage(doManipulation(originalImage.getBufferedImage()), "modified");
	}
	private BufferedImage doManipulation(BufferedImage input)
	{
		recalculateDimensions();
				
		double radianAngle		=	Math.toRadians(rotation);
				
		BufferedImage newImage	= 	null;
		
		if(Game.TEST_ACCELEATION) 	newImage = SimpleImage.createQuickImage(rotatedSize.x, rotatedSize.y);
		else						newImage = new BufferedImage(rotatedSize.x, rotatedSize.y, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g 			=	newImage.createGraphics();        
		if(!Game.TEST_ACCELEATION) 	g.setBackground(new Color(255, 255, 255, 0));  
        
        if(isRotated())
        	g.rotate(radianAngle, rotatedSize.x/2, rotatedSize.y/2);
        
        g.drawImage(input, (int)(rotatedSize.x - scaledSize.x)/2, (int)(rotatedSize.y - scaledSize.y)/2, scaledSize.x, scaledSize.y,	null);        

        if(isTransparencyChanged())
        	newImage = applyTransparency(newImage, transparency);
        
        return newImage;
	}
	private BufferedImage applyTransparency(BufferedImage image, double transparency)
	{   
		BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		
		//WritableRaster wr = image.getAlphaRaster();
		Graphics2D g = bi.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (100-transparency)/100));
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bi;
		/*
		int iArray[] = new int[image.getWidth()*image.getHeight()];
		
	    for (int cy=0; cy<image.getHeight(); cy++) 
	    {          
	        for (int cx=0; cx<image.getWidth(); cx++) 
	        {
	        	System.out.print(iArray[cx+cy*image.getWidth()]);
	        	iArray[cx+cy*cx]=255;
	        }
	        System.out.println("");
	    }
	    wr.setPixels(0, 0, image.getWidth(), image.getHeight(), iArray);
	    */
	    /*
	        	int oldRGB		= image.getRGB(cx, cy);
	            int oldAlpha 	= (oldRGB >> 24) & 0xFF;
	            int newAlpha 	= (int) (oldAlpha - (oldAlpha * transparency/100));
	            int newRGB		= (oldRGB & (0xFF << 24)) | (newAlpha << 24);
	            image.setRGB	  (cx, cy, newRGB);  
	            
	            System.out.print("old Alpha: " + oldAlpha + "; ");
	            System.out.print("new Alpha: " + newAlpha + "; ");
	            System.out.print("old RGB: " + oldRGB + "; ");
	            System.out.print("new RGB: " + newRGB + "; ");
	            System.out.print("actual RGB: " + image.getRGB(cx, cy) + "; ");
	            System.out.println("");
	        }
	    }*/
	}
	
	
	/**
	 * Recalculates the dimensions originalSize, scaledSize and rotatedSize.
	 */
	private void recalculateDimensions() 
	{
		if(originalImage!=null)
			originalSize 	= new Point(originalImage.getAbsoluteWidth(), originalImage.getAbsoluteHeight());
		else
			originalSize 	= new Point(correctImage.getAbsoluteWidth(), correctImage.getAbsoluteHeight());
			
		scaledSize 		= new Point(originalSize);
		if(isScaled())
		{
			scaledSize.setLocation(originalSize.x*xScale, originalSize.y*yScale);
		}
		rotatedSize 	= new Point(scaledSize);
		if(isRotated())
		{
			double radianAngle	= Math.toRadians(rotation);
			double sine 		= Math.sin(radianAngle);
			double cose 		= Math.cos(radianAngle);
			if (sine < 0) sine 	= -sine;
			if (cose < 0) cose 	= -cose;
			rotatedSize.setLocation(scaledSize.x*cose + scaledSize.y*sine, scaledSize.x*sine + scaledSize.y*cose);
		}
	}
	

	protected int getScaledWidth()
	{
		return scaledSize.x;
	}
	protected int getScaledHeight()
	{
		return scaledSize.y;
	}
	
	public boolean isRotated()
	{
		return rotation!=0;
	}
	public boolean isScaled()
	{
		return !(xScale==1 && yScale==1);
	}
	public boolean isTransparencyChanged()
	{
		return transparency!=0;
	}
	public double getRotation()
	{
		return rotation;
	}
	public double getXScale()
	{
		return xScale;
	}
	public double getYScale()
	{
		return yScale;
	}
	public Rectangle getRect()
	{
		return new Rectangle(0, 0, getWidth(), getHeight());
	}
	public SimpleImage getImage()
	{
		return correctImage;
	}
	public String getImagePath()
	{
		return originalImage.getPath();
	}
	public int getAbsoluteWidth()
	{
		return correctImage.getAbsoluteWidth();
	}
	public int getAbsoluteHeight()
	{
		return correctImage.getAbsoluteHeight();
	}
	public int getWidth()
	{
		return correctImage.getCellWidth();
	}
	public int getHeight()
	{
		return correctImage.getCellHeight();
	}
	public boolean isTransparentAt(int dx, int dy)
	{
		return correctImage.isTransparentAt(dx, dy);		
	}
	public double getTransparency()
	{
		return transparency;
	}
	public String toString()
	{
		return "Image: " + originalImage.getPath() + " Rotation: " + rotation + " X-Scale: " + xScale + " Y-Scale " + yScale;
	}
}
