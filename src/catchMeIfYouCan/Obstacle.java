package catchMeIfYouCan;

import godEngine.gameContent.Actor;
import godEngine.gameDependencies.GameException;

public class Obstacle extends Actor {

	public Obstacle()
	{
		setStartImage("Laby.png");
	}
	@Override
	protected void addedToWorld() throws GameException 
	{
	}

	@Override
	public void act() throws GameException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void removedFromWorld() throws GameException {
		// TODO Auto-generated method stub
		
	}

}
