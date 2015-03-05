package catchMeIfYouCan;

import godEngine.gameContent.Actor;
import godEngine.gameDependencies.GameException;

public class Obstacle extends Actor {

	@Override
	protected void addedToWorld() throws GameException 
	{
        setImage("Laby.png");
	}

	@Override
	public void act() throws GameException {	}

	@Override
	protected void removedFromWorld() throws GameException {	}

}
