package godEngine.gameContent;


import godEngine.gameDependencies.GameException;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class GodUtilities 
{

    public static final String ROOT                     = ClassLoader.getSystemClassLoader().getResource("").getPath();
	public static final String IMAGE_FOLDER_NAME 		= File.separator + "images";
	public static final String SOUND_FOLDER_NAME 		= File.separator + "sounds";
	public static final String IMAGE_FOLDER_PATH 		= ROOT + File.separator + IMAGE_FOLDER_NAME;
	public static final String SOUND_FOLDER_PATH 		= ROOT + File.separator + SOUND_FOLDER_NAME;
	public static final String DEFAULT_ACTOR_IMAGE 		=  "defaultActor.jpg";
	public static final String DEFAULT_BACKGROUND_IMAGE =  "defaultBackground.jpg";
	private static World world							= null;
	
	public static ArrayList<File> getAllFilesInDirectory(File directory)
	{
		ArrayList<File> allFiles = new ArrayList<>();
		
		if(directory.isFile())
		{
			allFiles.add(directory);
			return allFiles;
		}

		File[] allSubDirectories = directory.listFiles();
        if(allSubDirectories == null)
            return allFiles;

		for(File subFile : allSubDirectories)
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
		String completePath;

		if(new File(path).isAbsolute())
		{
			return path;
		}
		else
		{
			completePath 	= GodUtilities.IMAGE_FOLDER_PATH + File.separator + path;
		}
		
		return completePath;
	}
}
