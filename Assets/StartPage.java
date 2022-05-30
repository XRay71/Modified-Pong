import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

/* StartPage class is the first screen that loads (only once)
 * It introduces the game and controls to the player
 * Child of JPanel for graphics, implements KeyListener for keyboard input
 */

@SuppressWarnings("serial")
public class StartPage extends JPanel implements KeyListener {

	private Image image;
	private Graphics graphics;
	private Sound background;

	public StartPage() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		background = new Sound("StartBackground.wav");
		background.loop();
		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input
		this.setPreferredSize(new Dimension(GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT));
	}

	// paint is a method in java.awt library that we are overriding. It is a special
	// method - it is called automatically in the background in order to update what
	// appears in the window. You NEVER call paint() yourself
	public void paint(Graphics g) {
		image = createImage(GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT); // draw off screen
		graphics = image.getGraphics();
		graphics.setColor(Color.white);
		graphics.setFont(new Font("Consolas", Font.PLAIN, 15));
		graphics.drawString("Welcome to Modified Pong!", (int) (GamePanel.GAME_WIDTH * 0.15),
				(int) (GamePanel.GAME_HEIGHT * 0.2));
		graphics.drawString("Controls:", (int) (GamePanel.GAME_WIDTH * 0.15), (int) (GamePanel.GAME_HEIGHT * 0.3));
		graphics.drawString("Player 1: WASD", (int) (GamePanel.GAME_WIDTH * 0.15), (int) (GamePanel.GAME_HEIGHT * 0.4));
		graphics.drawString("Player 2: Arrow keys", (int) (GamePanel.GAME_WIDTH * 0.15),
				(int) (GamePanel.GAME_HEIGHT * 0.5));
		graphics.drawString("Toggle randomisation: r", (int) (GamePanel.GAME_WIDTH * 0.15),
				(int) (GamePanel.GAME_HEIGHT * 0.6));
		graphics.drawString("Skip round: n", (int) (GamePanel.GAME_WIDTH * 0.15), (int) (GamePanel.GAME_HEIGHT * 0.7));
		graphics.drawString("Press any key to begin!", (int) (GamePanel.GAME_WIDTH * 0.15),
				(int) (GamePanel.GAME_HEIGHT * 0.8));
		g.drawImage(image, 0, 0, this); // move the image on the screen

	}

	// starts the game, destroys this object
	public void keyPressed(KeyEvent e) {
		background.stop();
		try {
			Main.start.start();
		} catch (Exception e1) {
			System.out.println(
					"Sorry, something happened (honestly I'd be surprised if you got this message unless you deleted a file somewhere)!");
		}
	}

	// unused override
	public void keyReleased(KeyEvent e) {
	}

	// unused override
	public void keyTyped(KeyEvent e) {
	}
}