import java.awt.Color;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/* GameFrame class
 * Creates the window for the program.
 * Runs the StartPage and GamePanel constructors.
 * Child of JFrame
 */

@SuppressWarnings("serial")
public class GameFrame extends JFrame {

	GamePanel panel;
	StartPage start;

	public GameFrame(boolean flag) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		if (!flag) {
			start = new StartPage();
			this.add(start);
		} else {
			panel = new GamePanel();
			this.add(panel);
		}
		this.setTitle("Modified Pong!"); // set title for frame
		this.setResizable(false); // frame can't change size
		this.setBackground(Color.black);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X button will stop program execution
		this.pack(); // makes components fit in window - don't need to set JFrame size, as it will
						// adjust accordingly
		this.setVisible(true); // makes window visible to user
		this.setLocationRelativeTo(null);// set window in middle of screen
	}

	// Closes StartPage and boots up the main game
	public void start() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		Main.game = new GameFrame(true);
		this.dispose();
	}
}