import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/* Ball class represents the balls being bounced around by the paddles
 * 
 * Child of Rectangle because no circles smh
 * 
 * Trajectory is defined by a vector, position by two doubles.
 */

@SuppressWarnings("serial")
public class Ball extends Rectangle {

	// In order: angle of movement from the x-axis in radians, speed of Ball,
	// current x position, current y position
	// In order: initial diameter of any Ball object
	// In order: initial speed of any Ball object
	// in order: current diameter of specific Ball object, current mass of specific
	// Ball object (3 * diameter)
	private double angle, speed, currentX, currentY;
	private static final int initialDiameter = 20;
	private static final double initialSpeed = 5;
	private int diameter, mass;
	private static boolean randomised;

	// Constructor, accepts initial position of the Ball object and initializes the
	// private variables
	public Ball(int x, int y) {
		super(x, y, initialDiameter, initialDiameter);
		diameter = initialDiameter;
		currentX = x;
		currentY = y;
		speed = initialSpeed;
		mass = diameter * 3;
	}

	// Move function, updates each position of the Ball according to its current
	// trajectory
	public void move() {
		currentX += Math.cos(angle) * speed;
		currentY += -Math.sin(angle) * speed;
		x = (int) Math.round(currentX);
		y = (int) Math.round(currentY);
	}

	// Draw function, updates the graphics of the Ball
	public void draw(Graphics graphic) {
		if (!randomised)
			graphic.setColor(Color.white);
		else {
			int flag = (int) (Math.random() * 7);
			switch (flag) {
			case 0:
				graphic.setColor(Color.red);
				break;
			case 1:
				graphic.setColor(Color.orange);
				break;
			case 2:
				graphic.setColor(Color.yellow);
				break;
			case 3:
				graphic.setColor(Color.green);
				break;
			case 4:
				graphic.setColor(Color.blue);
				break;
			case 5:
				graphic.setColor(Color.darkGray);
				break;
			case 6:
				graphic.setColor(Color.magenta);
				break;
			}
		}
		graphic.fillOval(x, y, diameter, diameter);
	}

	// Setter function, sets the diameter to a new specified value
	public void setDiameter(int diameter) {
		this.diameter = diameter;
		this.mass = diameter * 3;
		this.width = diameter;
		this.height = diameter;
	}

	// Setter function, sets the mass to a new specified value
	public void setMass(int mass) {
		this.mass = mass;
		this.diameter = mass / 3;
		this.width = diameter;
		this.height = diameter;
	}

	// Setter function, sets the angle and speed of the new trajectory
	public void setTrajectory(double angle, double speed) {
		this.angle = angle;
		this.speed = speed;
	}

	// Setter function, sets if the game is random mode or not
	public void isRandom(boolean randomised) {
		Ball.randomised = randomised;
	}

	// Setter function, sets a new angle as specified
	public void setAngle(double angle) {
		this.angle = angle;
	}

	// Setter function, sets a new speed as specified
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	// Setter function, sets a new location as specified
	public void setLocation(int x, int y) {
		this.currentX = x;
		this.currentY = y;
		this.x = x;
		this.y = y;
	}

	// Getter function, returns a double[2] of form (currentX, currentY)
	public double[] getCoordinates() {
		return new double[] { currentX, currentY };
	}

	// Getter function, returns a double[2] of form (angle, speed);
	public double[] getTrajectory() {
		return new double[] { angle, speed };
	}

	// Getter function, returns an int - the diameter of the current Ball
	public int getDiameter() {
		return diameter;
	}

	// Getter function, returns an int - the default diameter of a Ball
	public static int getInitialDiameter() {
		return initialDiameter;
	}

	// Getter function, returns an int - the mass of the current Ball
	public int getMass() {
		return mass;
	}

}