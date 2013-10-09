package godEngine.gameDependencies;

import godEngine.gameContent.God;
import godEngine.gameContent.World;

public interface GraphicEngine 
{
	public void repaint(World world) throws GameException;
	public void initialize(GamePanel panel, God god) throws GameException;
	public void loadResources() throws GameException;
}
