import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/* Paddle class represents the paddles used by the players
 * Child of Rectangle for obvious reasons lol
 * Deals with any keypresses using awt.event.KeyEvent
 */

@SuppressWarnings("serial")
public class Paddle extends Rectangle {
	
	// In order: initial speed of any Paddle, initial length, final width, and initial mass
	// In order: current length, vertical movement direction, horizontal movement direction, current mass of current Paddle
	// In order: horizontal speed, vertical speed, vertical position, horizontal position of current Paddle
	// In order: whether it's the paddle on the right or not
	// In order: a set containing the keys currently pressed
	private static final int initialSpeed = 10, initialLength = 30, width = 2, initialMass = 42;
	private int length, verticalDirection, horizontalDirection, mass;
	private double horizontalSpeed, verticalSpeed, verticalPosition, horizontalPosition;
	private boolean rightPlayer;
	private Set<Character> keyPressed = new HashSet<>();

	// Constructor
	public Paddle(boolean rightPlayer) {
		super(rightPlayer ? GamePanel.GAME_WIDTH - 6 : 4, GamePanel.GAME_HEIGHT / 2 - initialLength + 5, width,
				initialLength);
		horizontalSpeed = 0;
		verticalSpeed = 0;
		length = initialLength;
		verticalPosition = GamePanel.GAME_HEIGHT / 2 - initialLength + 5;
		horizontalPosition = rightPlayer ? GamePanel.GAME_WIDTH - 6 : 4;
		verticalDirection = 1;
		horizontalDirection = 1;
		this.rightPlayer = rightPlayer;
		mass = 42;
	}

	// Changes the double position variables based on the direction and speed
	// Also changes the actual x and y coordinates of the object
	public void move() {
		verticalPosition += verticalDirection == 1 ? verticalSpeed : verticalDirection == -1 ? -verticalSpeed : 0;
		horizontalPosition += horizontalDirection == 1 ? horizontalSpeed : horizontalDirection == -1 ? -horizontalSpeed : 0;
		y = (int) Math.round(verticalPosition);
		x = (int) Math.round(horizontalPosition);
	}

	// Called continuously, updates the position of the paddle
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect((int) Math.round(horizontalPosition), (int) Math.round(verticalPosition), width, length);
	}

	// Setter function, sets the length of the paddle (and the mass, accordingly)
	public void setLength(int length) {
		this.mass = (int) ((double) (initialMass / initialLength) * (this.length - length)) + this.mass;
		this.length = length;
		this.height = length;
	}

	// Setter function, sets the horizontal speed of the paddle
	public void setHorizontalSpeed(double horizontalSpeed) {
		this.horizontalSpeed = horizontalSpeed;
	}

	// Setter function, sets the vertical speed of the paddle
	public void setVerticalSpeed(double verticalSpeed) {
		this.verticalSpeed = verticalSpeed;
	}

	// Setter function, sets the vertical position of the paddle
	public void setVerticalPosition(double verticalPosition) {
		this.verticalPosition = verticalPosition;
		this.y = (int) Math.round(verticalPosition);
	}

	// Setter function, sets the horizontal position of the paddle
	public void setHorizontalPosition(double horizontalPosition) {
		this.horizontalPosition = horizontalPosition;
		this.x = (int) Math.round(horizontalPosition);
	}

	// Setter function, sets the point where the paddle is
	public void setPosition(double verticalPosition, double horizontalPosition) {
		this.verticalPosition = verticalPosition;
		this.horizontalPosition = horizontalPosition;
		this.x = (int) Math.round(horizontalPosition);
		this.y = (int) Math.round(verticalPosition);
	}

	// Setter function, sets the direction the paddle is moving
	public void setDirection(int verticalDirection, int horizontalDirection) {
		this.verticalDirection = verticalDirection;
		this.horizontalDirection = horizontalDirection;
	}

	// Getter function, retrieves a double[] of the speeds
	public double[] getSpeeds() {
		return new double[] { horizontalSpeed, verticalSpeed };
	}

	// Getter function, retrieves an int[] of the direction travelled
	public int[] getDirections() {
		return new int[] { verticalDirection, horizontalDirection };
	}

	// Getter function, retrieves a double[][] of the current position of the paddle
	public double[][] getCoords() {
		return new double[][] { 
			    { horizontalPosition, verticalPosition }, 
			    { horizontalPosition + 2, verticalPosition },
				{ horizontalPosition + 2, verticalPosition + length },
				{ horizontalPosition, verticalPosition + length } };
	}

	// Getter function, retrieves the length of the paddle
	public int getLength() {
		return length;
	}

	// Getter function, retrieves the mass of the paddle
	public int getMass() {
		return mass;
	}

	// Handles keypresses by updating movement and speed
	public void keyPressed(KeyEvent e) {
		if (!rightPlayer) {
			switch (e.getKeyChar()) {
			case 'd':
				horizontalDirection = 1;
				horizontalSpeed = initialSpeed / 2;
				keyPressed.add(e.getKeyChar());
				break;
			case 'a':
				horizontalDirection = -1;
				horizontalSpeed = initialSpeed / 2;
				keyPressed.add(e.getKeyChar());
				break;
			case 'w':
				verticalDirection = -1;
				verticalSpeed = initialSpeed;
				keyPressed.add(e.getKeyChar());
				break;
			case 's':
				verticalDirection = 1;
				verticalSpeed = initialSpeed;
				keyPressed.add(e.getKeyChar());
				break;
			default:
				break;
			}
		} else
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				horizontalDirection = 1;
				horizontalSpeed = initialSpeed / 2;
				keyPressed.add('z');
				break;
			case KeyEvent.VK_LEFT:
				horizontalDirection = -1;
				horizontalSpeed = initialSpeed / 2;
				keyPressed.add('x');
				break;
			case KeyEvent.VK_UP:
				verticalDirection = -1;
				verticalSpeed = initialSpeed;
				keyPressed.add('c');
				break;
			case KeyEvent.VK_DOWN:
				verticalDirection = 1;
				verticalSpeed = initialSpeed;
				keyPressed.add('v');
				break;
			default:
				break;
			}
	}

	// Handles keyreleases by removing directions and removing speed
	public void keyReleased(KeyEvent e) {
		if (!rightPlayer) {
			switch (e.getKeyChar()) {
			case 'd':
				keyPressed.remove(e.getKeyChar());
				break;
			case 'a':
				keyPressed.remove(e.getKeyChar());
				break;
			case 'w':
				keyPressed.remove(e.getKeyChar());
				break;
			case 's':
				keyPressed.remove(e.getKeyChar());
				break;
			default:
				break;
			}
			if (!keyPressed.contains('w') && !keyPressed.contains('s')) {
				verticalSpeed = 0;
				verticalDirection = 0;
			}
			if (!keyPressed.contains('d') && !keyPressed.contains('a')) {
				horizontalSpeed = 0;
				horizontalDirection = 0;
			}
		} else {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				keyPressed.remove('z');
				break;
			case KeyEvent.VK_LEFT:
				keyPressed.remove('x');
				break;
			case KeyEvent.VK_UP:
				keyPressed.remove('c');
				break;
			case KeyEvent.VK_DOWN:
				keyPressed.remove('v');
				break;
			default:
				break;
			}
			if (!keyPressed.contains('c') && !keyPressed.contains('v')) {
				verticalSpeed = 0;
				verticalDirection = 0;
			}
			if (!keyPressed.contains('z') && !keyPressed.contains('x')) {
				horizontalSpeed = 0;
				horizontalDirection = 0;
			}
		}
	}
}