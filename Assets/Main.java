import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/* Main class
 * Only purpose is to run the GameFrame constructor
*/

class Main {
	public static GameFrame game;
	public static GameFrame start;

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		start = new GameFrame(false);
	}
}