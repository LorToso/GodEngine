package test1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import godEngine.gameContent.God;
import godEngine.gameContent.World;
import godEngine.gameDependencies.GodMouseInfo;

public class TestWelt extends World {

	FileWriter fstream = null;
	BufferedWriter writer = null;
	
	public TestWelt()
	{
		super(50, 50, 10);
		God.setFPS(60);
		God.calcFPS(true);
		try {
			fstream = new FileWriter("test1.txt");
			writer = new BufferedWriter(fstream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	public void act()
	{
		int x = God.getRandomNumber(getWidth());
		int y = God.getRandomNumber(getHeight());
		while(getObjectsAt(x,y).size()>0)
		{
			x=God.getRandomNumber(getWidth());
			y=God.getRandomNumber(getHeight());
		}
		addObject(x, y, new TestActor());
		
		try {
			writer.write(God.getFPS() + ";" + getNumberOfObjects() + ";\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("FPS: " + God.getFPS() + "; Number of Actors: " + getNumberOfObjects());
	}
}
