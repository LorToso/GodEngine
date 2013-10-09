package godEngine.gameContent;


import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

import godEngine.gameDependencies.GameException;

public class GodUtilities 
{

	public static final String IMAGE_FOLDER_NAME 		= "/images";
	public static final String SOUND_FOLDER_NAME 		= "/sounds";
	public static final String IMAGE_FOLDER_PATH 		= new Object(){}.getClass().getEnclosingClass().getResource(IMAGE_FOLDER_NAME).getPath().substring(1).replace('/', '\\');
	public static final String SOUND_FOLDER_PATH 		= new Object(){}.getClass().getEnclosingClass().getResource(SOUND_FOLDER_NAME).getPath().substring(1).replace('/', '\\');
	public static final String DEFAULT_ACTOR_IMAGE 		=  "defaultActor.jpg";
	public static final String DEFAULT_BACKGROUND_IMAGE =  "defaultBackground.jpg";
	private static World world							= null;
	
	public static ArrayList<File> getAllFilesInDirectory(File directory)
	{
		ArrayList<File> allFiles = new ArrayList<File>();
		
		if(directory.isFile())
		{
			allFiles.add(directory);
			return allFiles;
		}
			
		for(File subFile : directory.listFiles())
		{
			allFiles.addAll(getAllFilesInDirectory(subFile));
		}
		return allFiles;
	}
	protected static void setWorld(World world)
	{
		GodUtilities.world = world;
	}
	public static Rectangle calcCorrectRect(int x, int y, int width, int height) throws GameException
	{
		if(world == null)
			throw new GameException(GameException.ERROR_WORLD_NOT_SET);
		
		Rectangle r = new Rectangle(x-width/2, y-height/2, width, height);
		Rectangle w = new Rectangle(0, 0, world.getWidth(), world.getHeight());
		return r.intersection(w);
	}
	public static String getCompletePath(String path)
	{
		String completePath = null;

		if(new File(path).isAbsolute())
		{
			return path;
		}
		else
		{
			completePath 	= GodUtilities.IMAGE_FOLDER_PATH + "\\" + path;
		}
		completePath.replaceAll("/", "\\");
		
		return completePath;
	}
}
