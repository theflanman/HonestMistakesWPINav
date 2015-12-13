package main.util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/** Playing Sounds
 * 
 * @author NathanGeorge
 *
 */
@SuppressWarnings("serial")
public class Speaker extends JFrame {
   
	String path;
	
   // Constructor
   public Speaker(String path) {
	   this.path = path;
   }
   
   public void play(){
	   try {
		   // Open an audio input stream.
		   File soundFile = new File(path);
		   AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

		   // Get a sound clip resource.
		   Clip clip = AudioSystem.getClip();

		   // Open audio clip and load samples from the audio input stream.
		   clip.open(audioIn);
		   clip.start();

	   } catch (UnsupportedAudioFileException e) {
		   e.printStackTrace();
	   } catch (IOException e) {
		   e.printStackTrace();
	   } catch (LineUnavailableException e) {
		   e.printStackTrace();
	   }
   }
   
}