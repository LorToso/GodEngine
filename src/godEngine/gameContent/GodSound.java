package godEngine.gameContent;


import godEngine.gameDependencies.GameException;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GodSound 
{
	private Clip myClip 									= null;
	private static HashMap<String, Clip> preLoadedSounds 	= null;
	

	public static void preLoadSounds() throws GameException
	{
		preLoadedSounds = new HashMap<>();
		
		ArrayList<File> allFiles = GodUtilities.getAllFilesInDirectory(new File(GodUtilities.SOUND_FOLDER_PATH));
		for(File file : allFiles)
		{
			Clip loadedClip = getClipFromFile(file);
	
			preLoadedSounds.put(file.getPath(), loadedClip);
		}
	}
	private static Clip getClipFromFile(File inputFile) throws GameException 
	{
	//	UnsupportedAudioFileException, IOException, LineUnavailableException
		
		if(preLoadedSounds!= null && preLoadedSounds.containsKey(inputFile.getPath()))
		{
			return preLoadedSounds.get(inputFile.getPath());
		}
		
		FileInputStream fis					= null;
		InputStream bufferedIn				= null;
		AudioInputStream audioInputStream 	= null;
		DataLine.Info info					= null;
		Clip newClip						= null;
		
		try{
			fis 				= new FileInputStream(inputFile);
			bufferedIn 			= new BufferedInputStream(fis);
			audioInputStream 	= AudioSystem.getAudioInputStream(bufferedIn);
    		info 				= new DataLine.Info(Clip.class, audioInputStream.getFormat());
    		newClip 			= (Clip) AudioSystem.getLine(info);
    		newClip.open(audioInputStream);
		}
		catch(UnsupportedAudioFileException e)
		{
			throw new GameException(GameException.ERROR_NOT_A_VALID_SOUNDFILE_FORMAT);	
		}
		catch(IOException e)
		{
			throw new GameException(GameException.ERROR_NOT_A_VALID_SOUNDFILE);	
		} 
		catch(LineUnavailableException e)
		{
			throw new GameException(GameException.ERROR_SOUNDFILE_IN_USE);	
		}
		return newClip;
	}
	
	public GodSound(String path, boolean loop) throws GameException
	{
		String completePath = null;

		if(new File(path).isAbsolute())
		{
			completePath = path;
		}
		else
		{
			completePath = GodUtilities.SOUND_FOLDER_PATH + "\\" + path;
		}
		
		File f =  new File(completePath);
		
		myClip = getClipFromFile(f);
			
        if(loop)
        	loop(-1);
	}
	public GodSound(String path) throws GameException
	{
		this(path, false);
	}
	
	public void loop(int count)
	{
		if(count == -1)
		{
			myClip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else
		{
			myClip.loop(count);
		}
	}
	public void setVolume(float percent) throws GameException
	{
		if(percent > 100 || percent < 0)
			throw new GameException(GameException.ERROR_VALUE_OUT_OF_RANGE);
				
		FloatControl volumeCtrl = (FloatControl) myClip.getControl(FloatControl.Type.MASTER_GAIN);
		float range = volumeCtrl.getMaximum()-volumeCtrl.getMinimum();
		
		volumeCtrl.setValue(percent/100 * range + volumeCtrl.getMinimum());
		System.out.println("Volume: " + percent);
	}
	public float getVolume()
	{
		FloatControl volumeCtrl = (FloatControl) myClip.getControl(FloatControl.Type.MASTER_GAIN);
		float value = volumeCtrl.getValue()-volumeCtrl.getMinimum();
		float range = volumeCtrl.getMaximum()-volumeCtrl.getMinimum();
        return value/range * 100;
	}
	public void pause()
	{
		myClip.stop();
	}
	public void play()
	{
		if(!myClip.isRunning() && myClip.getFramePosition()!=0)
			myClip.setFramePosition(0);
		
		myClip.start();
	}
	public void stop()
	{
		myClip.stop();
		myClip.setFramePosition(0);
	}
}
