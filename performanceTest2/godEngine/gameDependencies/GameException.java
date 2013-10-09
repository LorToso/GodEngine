package godEngine.gameDependencies;

@SuppressWarnings("serial")
public class GameException extends Exception {
	final static String NO_ERROR = "No Error occured.";
	final static String ERROR_OBJECT_NOT_IN_WORLD = "The Object has not been placed in the World yet.";
	final static String ERROR_WORLD_NOT_EXISTENT = "The selected World caused an Error. The second Run-parameter may be wrong.";
	final static String ERROR_NOT_A_VALID_IMAGE = "The selected image could not be loaded.";
	final static String ERROR_INVALID_NUMBER_OF_ARGUMENTS = "Wrong number of arguments.";
	final static String ERROR_CREATING_OBJECT = "The Object could not be added to the World.";
	final static String ERROR_DURING_WAITTIME = "An Error occured during the Waittime of the Act-Loop. No idea how this could happen.";
	final static String ERROR_NO_VALID_ACTOR_ID = "No Actor ID could be acquired.";
	final static String ERROR_INVALID_WORLD_DIMENSIONS = "No Actor ID could be acquired.";
	final static String ERROR_OUT_OF_BOUNDS = "The selected Coordinates are outside the World.";
	final static String ERROR_INVALID_COORDINATES = "The selected Coordinates invalid.";
	
	
	private String message = "";
	
	public GameException()
	{}
	public GameException(String customMessage)
	{
		message = customMessage;
	}
	public GameException(int exceptionNumber)
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
			this.message = ERROR_INVALID_NUMBER_OF_ARGUMENTS;
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
		}
		
	}
	public void printMessage()
	{
		System.out.println(message);
	}
}
