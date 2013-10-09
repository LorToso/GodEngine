package godEngine.gameDependencies;

@SuppressWarnings("serial")
public class GameException extends Exception {
	
	public final static String NO_ERROR = "No Error occured.";
	public final static String ERROR_OBJECT_NOT_IN_WORLD 				= "The Object has not been placed in the World yet.";
	public final static String ERROR_WORLD_NOT_FOUND 					= "The selected World could not be found.";
	public final static String ERROR_NO_VALID_WORLD_CONSTUCTOR 			= "The selected World caused an error. It may not have a nullary constructor.";
	public final static String ERROR_NOT_A_VALID_IMAGE 					= "The selected image could not be loaded.";
	public final static String ERROR_NO_WORLD_SPECIFIED 				= "One of the arguments in the Run-Configuration has to specifiy the World that is supposed to be loaded. Use \"world=\" and specify the full path to the world that is supposed to be loaded.";
	public final static String ERROR_CREATING_OBJECT	 				= "The Object could not be added to the World. Maybe it has no default Constructor? Create is yourself and use addObject(int x, int y, Actor a).";
	public final static String ERROR_DURING_WAITTIME 					= "An Error occured during the Waittime of the Act-Loop. No idea how this could happen.";
	public final static String ERROR_NO_VALID_ACTOR_ID 					= "No Actor ID could be acquired. Do you really have more than 4 billion actors?";
	public final static String ERROR_INVALID_WORLD_DIMENSIONS 			= "No Actor ID could be acquired.";
	public final static String ERROR_OUT_OF_BOUNDS 						= "The selected Coordinates are outside the World.";
	public final static String ERROR_INVALID_COORDINATES 				= "The selected Coordinates are invalid.";
	public final static String ERROR_CELLSIZE_NOT_SET 					= "The worlds cellSize hasn't been set to the image yet. Either use a different constuctor, or set it manually!";
	public final static String ERROR_USING_SETIMAGE 					= "The Method setImage(String) can only be used AFTER the Actor has been added to the World. If you want to set an image to the Actor in the constuctor, please use setStartingImage(String) isntead.";
	public final static String ERROR_USING_UNIMPLEMENTED_INPUT_ENGINE 	= "You tried to use the inputEngine without including it. It has not been initialized.";
	public final static String ERROR_VALUE_OUT_OF_RANGE			 		= "The entered value is out of range.";
	public final static String ERROR_NOT_A_VALID_SOUNDFILE 				= "The selected soundfile could not be loaded.";
	public final static String ERROR_SOUNDFILE_IN_USE 					= "The selected soundfile is not available. Is it already in use?";
	public final static String ERROR_NOT_A_VALID_SOUNDFILE_FORMAT 		= "The selected soundfile could not be loaded. It has to be a .wav - file.";
	public final static String ERROR_PRELOADING_IMAGES 					= "There was an error loading images in the default folder. Maybe there is a non-image file in the image folder?";
	public final static String ERROR_PRELOADING_SOUNDS 					= "There was an error loading sounds in the default folder. Maybe there is a non-sound file in the sounds folder?";
	public final static String ERROR_ACTOR_HAS_NO_IMAGE 				= "You cannot get the Dimensions of an Actor, that has no image yet.";
	public final static String ERROR_WORLD_NOT_SET 						= "You cannot use methods of the GodUtilities Class if it has no World connected to it.";
	public final static String ERROR_GRAPHIC_ENGINE_NOT_FOUND 			= "The specified graphic engine was not found or caused an error.";
	public final static String ERROR_PANEL_NOT_INITIALIZED 				= "The gamepanel has not been initialized yet. Its trying to draw a frame though.";
	public final static String ERROR_NO_VALID_GRAPHIC_ENGINE_CONSTUCTOR = "The selected World caused an error. It may not have a nullary constructor.";
	public final static String ERROR_WORLD_WAS_NOT_INITIALIZED_YET		= "The world constructor caused an error. You cannot place Actors in the constructor. Use the \"initializeWorld()\" function instead.";
	public final static String ERROR_IMAGE_NOT_INITIALIZED				= "The image has not been initialized yet.";
	public final static String ERROR_INVALID_DIMENSIONS					= "The specified dimensions are invalid.";
	public final static String ERROR_INVALID_CONSTRUCTOR_CALL			= "You cannot call this method from the constructor. Use the initialize or the addedToWorld method instead.";
	
	private String message = "";
	
	public GameException()
	{}
	public GameException(String customMessage)
	{
		message = customMessage;
	}
/*	public GameException(int exceptionNumber)
	{
		switch(exceptionNumber)
		{
		case 0:
			this.message = NO_ERROR;
			break;
		case 1:
			this.message = ERROR_OBJECT_NOT_IN_WORLD;
			break;
		case 2:
			this.message = ERROR_WORLD_NOT_EXISTENT;
			break;	
		case 3:
			this.message = ERROR_NOT_A_VALID_IMAGE;
			break;	
		case 4:
			this.message = ERROR_NO_WORLD_SPECIFIED;
			break;		
		case 5:
			this.message = ERROR_CREATING_OBJECT;
			break;		
		case 6:
			this.message = ERROR_DURING_WAITTIME;
			break;			
		case 7:
			this.message = ERROR_NO_VALID_ACTOR_ID;
			break;				
		case 8:
			this.message = ERROR_INVALID_WORLD_DIMENSIONS;
			break;					
		case 9:
			this.message = ERROR_OUT_OF_BOUNDS;
			break;						
		case 10:
			this.message = ERROR_INVALID_COORDINATES;
			break;							
		case 11:
			this.message = ERROR_CELLSIZE_NOT_SET;
			break;								
		case 12:
			this.message = ERROR_USING_SETIMAGE;
			break;									
		case 13:
			this.message = ERROR_USING_UNIMPLEMENTED_INPUT_ENGINE;
			break;										
		case 14:
			this.message = ERROR_VALUE_OUT_OF_RANGE;
			break;											
		case 15:
			this.message = ERROR_NOT_A_VALID_SOUNDFILE;
			break;												
		case 16:
			this.message = ERROR_NOT_A_VALID_SOUNDFILE_FORMAT;
			break;												
		case 17:
			this.message = ERROR_PRELOADING_IMAGES;
			break;												
		case 18:
			this.message = ERROR_PRELOADING_SOUNDS;
			break;													
		case 19:
			this.message = ERROR_ACTOR_HAS_NO_IMAGE;
			break;														
		case 20:
			this.message = ERROR_WORLD_NOT_SET;
			break;															
		case 21:
			this.message = ERROR_GRAPHIC_ENGINE_NOT_FOUND;
			break;	
			
		}
	}*/
	public void printMessage()
	{
		System.err.println(message);
	}
	public static void crashAndBurn(GameException e)
	{
		e.printMessage();
		e.printStackTrace();
		System.exit(1);
	}
}
