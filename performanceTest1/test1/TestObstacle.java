package test1;

import godEngine.gameContent.Actor;

public class TestObstacle extends Actor 
{
	public TestObstacle()
	{
		super();
	}
	protected void addedToWorld()
	{
		setImage("/test1/testImages/Kreis.png");
	}

}
