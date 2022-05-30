import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/* Score class creates a score 
 * child of rectangle because easy formatting
 */

@SuppressWarnings("serial")
public class Score extends Rectangle {

	// value of the score, flag to check which player
	public int value;
	private boolean rightPlayer;

	// constructor
	public Score(boolean rightPlayer) {
		value = 0;
		this.rightPlayer = rightPlayer;
	}

	// called continuously, draws the score onto the panel
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Consolas", Font.PLAIN, 30));
		g.drawString(String.valueOf(value),
				(int) (rightPlayer ? GamePanel.GAME_WIDTH * 0.52 : GamePanel.GAME_WIDTH * 0.43),
				(int) (GamePanel.GAME_HEIGHT * 0.1));
	}

}
