import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/* Sound class stores and plays sound files
 * Uses javax.sound.sampled.Clip and has four functions:
 * Play, Loop, Stop, and Set Volume
 */

public class Sound {

	private Clip clip;

	// Initializes the Clip object with the corresponding file
	public Sound(String file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File soundFile = new File(file);
		AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
		clip = AudioSystem.getClip();
		clip.open(sound);
	}

	// Starts playing from the start
	public void play() {
		clip.setFramePosition(0);
		clip.start();
	}

	// Loops the sound forever
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	// Stops the sound
	public void stop() {
		clip.stop();
	}
	
	// Sets volume of the clip
	public void setVolume(float volume) {
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
	    gainControl.setValue(20f * (float) Math.log10(volume));
	}
}
