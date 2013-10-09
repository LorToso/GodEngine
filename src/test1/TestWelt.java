package test1;

import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import godEngine.gameContent.God;
import godEngine.gameContent.World;
import godEngine.gameDependencies.CellMap;
import godEngine.gameDependencies.GameException;

public class TestWelt extends World {
	
	FileWriter fstream = null;
	BufferedWriter writer = null;
	TestActor to = null;
	public TestWelt() throws GameException
	{
		super(500, 500, 1);
		God.setFPS(60);
		God.calcFPS(true);
		
		try {
			fstream = new FileWriter("test1.txt");
			writer = new BufferedWriter(fstream);
		} catch (IOException e) {
			
			e.printStackTrace();
			System.exit(1);
		}
		to = new TestActor();
		//addObject(getWidth()/2-30, getHeight()/2+35, new TestObstacle());
		//to.setRotation(30);
		//TestActor ta = new TestActor();
		//addObject(getWidth()/2+26, getHeight()/2, ta);
		//ta.setRotation(45);
		//addObject(getWidth()/2, 2, new TestActor() );
		//addObject(getWidth()/2+5, 0, new TestActor() );
	//	addObject(getWidth()/2+5, 5, new TestActor() );
		
		
		//addObject(getWidth()/2, getHeight()/2, new TestObstacle() );
		//addObject(getWidth()-1, getHeight()-1, new TestObstacle() );
		//t();
	}
	public void t() throws GameException
	{
		//for(int y = 0; y <20; y++){
			for(int x=0; x <= getWidth(); x++)
			{
				addObject(x, 0, new SmallActor());
			}
		//}
	}
	public void t2() throws GameException
	{
		for(int i=0; i <= getWidth(); i++)
		{
			addObject(i, 0, new SmallActor());
		}
	}
	public void printWorld()
	{
		CellMap cm = getCellMap();
		int out = 0;
		ArrayList<Integer> l = null;
		for(int y=0; y<getHeight(); y++)
		{
			for(int x=0; x<getWidth(); x++)
			{
				l = cm.getObjectsAt(x, y);
				if(l== null) out = 0;
				else out = l.get(0);	
				System.out.print(out);
			}
			System.out.print("\n");
		}
	}
	public void act() throws GameException
	{		
		/*if(God.isKeyDown(KeyEvent.VK_P)){
			printWorld();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		if(God.isKeyDown(KeyEvent.VK_SPACE))
			t();
		if(God.isKeyDown(KeyEvent.VK_ENTER)){
			removeObject(to);
			God.delay(100);
		}
		/*
		if(God.isKeyDown(KeyEvent.VK_UP)){
			to.rescale(1.1,1.1);
		}
		if(God.isKeyDown(KeyEvent.VK_DOWN)){
			to.rescale(0.9,0.9);
		}
		if(God.isKeyDown(KeyEvent.VK_RIGHT)){
			to.rotate(5);
		}
		if(God.isKeyDown(KeyEvent.VK_LEFT)){
			to.rotate(-5);
		}*/
		
		//addObject(God.getRandomNumber(getWidth()), 0, new TestActor());

		//System.out.println("FPS: " + God.getFPS() + " Actors: " + getObjects(Actor.class).size());
		
		/*
		try {
			writer.write(getNumberOfObjects() + ";" + God.getFPS() + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	@Override
	public void gameStopped() 
	{	
	}
	@Override
	protected void preGameMode() throws GameException {
		// TODO Auto-generated method stub
		
	}
	protected void initializeWorld() throws GameException 
	{

		addObject(0, 0, to);
		//addObject(getWidth()/2, getHeight()/2, to);
	}
}
