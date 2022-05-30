import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

/* GamePanel class acts as the main "game loop" - continuously runs the game and calls whatever needs to be called
 * Child of JPanel because JPanel contains methods for drawing to the screen
 * Implements KeyListener interface to listen for keyboard input
 * Implements Runnable interface to use "threading" - let the game do two things at once
 */

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions of window
	public static final int GAME_WIDTH = 300;
	public static final int GAME_HEIGHT = 300;

	// friction
	private static double friction = 0.01;

	// GUI
	private Thread gameThread;
	private Image image;
	private Graphics graphics;

	// Objects in game
	private Ball ball;
	private Paddle rightPaddle, leftPaddle;
	private Score rightScore, leftScore;

	// Constants
	private int paddleBorder, won;
	private boolean randomise, disable, stuck, newRound;
	
	// Sound effects
	private Sound background, hitWall, hitPaddle, scored, winningMusic, randomisedMusic;

	public GamePanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		// Initialize constants
		paddleBorder = 45;
		randomise = false;
		disable = false;
		won = 0;
		stuck = false;
		newRound = false;
		
		// Initialize sounds
		background = new Sound("GameBackground.wav");
		hitWall = new Sound("PuckHitWall.wav");
		hitWall.setVolume(0.1f);
		hitPaddle = new Sound("PuckHitPaddle.wav");
		scored = new Sound("Scored.wav");
		winningMusic = new Sound("SomeoneWon.wav");
		randomisedMusic = new Sound("RandomMusic.wav");

		// create a player controlled ball, randomize starting side, angle
		boolean flag = Math.random() < 0.5;
		ball = new Ball(flag ? 30 : GAME_WIDTH - 30 - Ball.getInitialDiameter() / 2,
				GAME_HEIGHT / 2 - Ball.getInitialDiameter());
		ball.setAngle(flag ? convertAngle((Math.random() - 0.5) * Math.PI / 2.0)
				: convertAngle((Math.random() - 0.5) * Math.PI / 2.0 + Math.PI));

		// create the two player paddles
		rightPaddle = new Paddle(true);
		leftPaddle = new Paddle(false);

		// create the two score counts
		rightScore = new Score(true);
		leftScore = new Score(false);

		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input
		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

		// make this class run at the same time as other classes (without this each
		// class would "pause" while another class runs). By using threading we can
		// remove lag, and also allows us to do features like display timers in real
		// time!
		gameThread = new Thread(this);
		gameThread.start();
		background.loop();
	}

	// paint is a method in java.awt library that we are overriding. It is a special
	// method - it is called automatically in the background in order to update what
	// appears in the window. You NEVER call paint() yourself
	public void paint(Graphics g) {
		// we are using "double buffering here" - if we draw images directly onto the
		// screen, it takes time and the human eye can actually notice flashes of lag as
		// each pixel on the screen is drawn one at a time. Instead, we are going to
		// draw images OFF the screen, then simply move the image on screen as needed.
		image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
		graphics = image.getGraphics();

		if (won == 0) {
			draw(graphics); // update the positions of everything on the screen

			// marks borders
			graphics.setColor(Color.white);
			graphics.drawLine(paddleBorder, 0, paddleBorder, GAME_HEIGHT);
			graphics.drawLine(GAME_WIDTH - paddleBorder, 0, GAME_WIDTH - paddleBorder, GAME_HEIGHT);

			// what happens if it is stuck
			if (stuck) {
				graphics.setFont(new Font("Consolas", Font.PLAIN, 10));
				graphics.drawString("Ball stuck! Press n to move on.", (int) (GAME_WIDTH * 0.2), GAME_HEIGHT / 2);
			}
			// what happens when it's a new round
			else if (newRound) {
				graphics.setFont(new Font("Consolas", Font.PLAIN, 10));
				graphics.drawString("Press n to start the next round.", (int) (GAME_WIDTH * 0.2), GAME_HEIGHT / 2);
			}
			// what happens normally
			else
				graphics.drawLine(GAME_WIDTH / 2, 0, GAME_WIDTH / 2, GAME_HEIGHT);
		} else {
			// what happens when a player wins
			draw(graphics);
			graphics.setFont(new Font("Consolas", Font.PLAIN, 15));
			graphics.drawString(
					(won == 1 ? "Player 2 " : "Player 1 ") + "won " + leftScore.value + " : " + rightScore.value + "!",
					(int) (GAME_WIDTH * 0.25), GAME_HEIGHT / 2);
			graphics.drawString("Press n to start a new game.", (int) (GAME_WIDTH * 0.15), (int) (GAME_HEIGHT * 0.55));
		}
		g.drawImage(image, 0, 0, this); // move the image on the screen

	}

	// resets objects to default settings
	public void reset() {

		paddleBorder = 45;

		boolean flag = Math.random() < 0.5;
		ball = new Ball(flag ? 30 : GAME_WIDTH - 30 - Ball.getInitialDiameter() / 2,
				GAME_HEIGHT / 2 - Ball.getInitialDiameter());
		ball.setAngle(flag ? convertAngle((Math.random() - 0.5) * Math.PI / 2.0)
				: convertAngle((Math.random() - 0.5) * Math.PI / 2.0 + Math.PI));
		ball.isRandom(randomise);

		rightPaddle = new Paddle(true);
		leftPaddle = new Paddle(false);

		if (randomise)
			randomise();

	}

	// randomises different aspects of the game
	public void randomise() {
		paddleBorder = (int) (Math.random() * 75) + 20;
		ball.setSpeed(Math.random() * 6 + 2.5);
		ball.setDiameter((int) (Math.random() * 30) + 10);
		rightPaddle.setLength((int) (Math.random() * 30) + 15);
		leftPaddle.setLength((int) (Math.random() * 30) + 15);
	}

	// call the draw methods in each class to update positions as things move
	public void draw(Graphics g) {
		if (won == 0) {
			if (!stuck) {
				ball.draw(g);
			}
			rightScore.draw(g);
			leftScore.draw(g);
		}
		if (won == 0 || won == 1)
			rightPaddle.draw(g);
		if (won == 0 || won == -1)
			leftPaddle.draw(g);
	}

	// call the move methods in other classes to update positions
	// this method is constantly called from run(). By doing this, movements appear
	// fluid and natural. If we take this out the movements appear sluggish and
	// laggy
	public void move() {
		ball.move();
		rightPaddle.move();
		leftPaddle.move();
	}

	// handles all collision detection and responds accordingly
	public void checkCollision() {
		// elastic collision formulas to calculate resulting velocity of the ball
		if (ball.intersects(rightPaddle)) {
			ball.setAngle(convertAngle(Math.PI - ball.getTrajectory()[0]));
			ball.setLocation(rightPaddle.x - ball.getDiameter(), ball.y);
			if (rightPaddle.getDirections()[1] == -1)
				ball.setSpeed(ball.getTrajectory()[1] / 2 + (rightPaddle.getSpeeds()[0] * 2 * rightPaddle.getMass()
						/ (rightPaddle.getMass() + ball.getMass())
						+ ball.getTrajectory()[1] * (rightPaddle.getMass() - ball.getMass())
								/ (rightPaddle.getMass() + ball.getMass()))
						* 2);
			hitPaddle.play();
			rightPaddle.setDirection(0, 0);
			leftPaddle.setDirection(0, 0);
			rightPaddle.setHorizontalSpeed(0);
			rightPaddle.setVerticalSpeed(0);
			leftPaddle.setHorizontalSpeed(0);
			leftPaddle.setVerticalSpeed(0);
		}
		if (ball.intersects(leftPaddle)) {
			ball.setAngle(convertAngle(Math.PI - ball.getTrajectory()[0]));
			ball.setLocation(leftPaddle.x + 2, ball.y);
			if (leftPaddle.getDirections()[1] == 1)
				ball.setSpeed(ball.getTrajectory()[1] / 2 + (leftPaddle.getSpeeds()[0] * 2 * leftPaddle.getMass()
						/ (leftPaddle.getMass() + ball.getMass())
						+ ball.getTrajectory()[1] * (leftPaddle.getMass() - ball.getMass())
								/ (leftPaddle.getMass() + ball.getMass()))
						* 2);
			hitPaddle.play();
			rightPaddle.setDirection(0, 0);
			leftPaddle.setDirection(0, 0);
			rightPaddle.setHorizontalSpeed(0);
			rightPaddle.setVerticalSpeed(0);
			leftPaddle.setHorizontalSpeed(0);
			leftPaddle.setVerticalSpeed(0);
		}

		// ball & border collisions
		if (ball.y < 0) {
			ball.y = 0;
			ball.setAngle(convertAngle(-ball.getTrajectory()[0]));
			ball.setLocation((int) ball.getCoordinates()[0], 0);
			hitWall.play();
		}
		if (ball.y > GAME_HEIGHT - ball.getDiameter()) {
			ball.y = GAME_HEIGHT - ball.getDiameter();
			ball.setAngle(convertAngle(-ball.getTrajectory()[0]));
			ball.setLocation((int) ball.getCoordinates()[0], GAME_HEIGHT - ball.getDiameter());
			hitWall.play();
		}
		if (ball.x < -2) {
			ball.x = 0;
			ball.setLocation(0, (int) ball.getCoordinates()[1]);
			ball.setSpeed(0);
			rightScore.value++;
			if (rightScore.value > 9) {
				disable = true;
				won = 1;
				winningMusic.play();
			} else {
				disable = true;
				newRound = true;
				scored.play();
			}
		}
		if (ball.x + ball.getDiameter() > GAME_WIDTH + 2) {
			ball.x = GAME_WIDTH - ball.getDiameter();
			ball.setLocation(GAME_WIDTH - ball.getDiameter(), (int) ball.getCoordinates()[1]);
			ball.setSpeed(0);
			leftScore.value++;
			if (leftScore.value > 9) {
				disable = true;
				won = -1;
				winningMusic.play();
			} else {
				disable = true;
				newRound = true;
				scored.play();
			}
		}

		// right paddle and border collisions
		if (rightPaddle.getCoords()[0][0] <= GAME_WIDTH - paddleBorder) {
			rightPaddle.setHorizontalPosition(GAME_WIDTH - paddleBorder);
		}
		if (rightPaddle.getCoords()[1][0] >= GAME_WIDTH) {
			rightPaddle.setHorizontalPosition(GAME_WIDTH - 2);
		}
		if (rightPaddle.getCoords()[3][1] >= GAME_HEIGHT) {
			rightPaddle.setVerticalPosition(GAME_HEIGHT - rightPaddle.getLength());
		}
		if (rightPaddle.getCoords()[0][1] <= 0) {
			rightPaddle.setVerticalPosition(0);
		}

		// left paddle and border collisions
		if (leftPaddle.getCoords()[0][0] <= 0) {
			leftPaddle.setHorizontalPosition(0);
		}
		if (leftPaddle.getCoords()[1][0] >= paddleBorder) {
			leftPaddle.setHorizontalPosition(paddleBorder - 2);
		}
		if (leftPaddle.getCoords()[3][1] >= GAME_HEIGHT) {
			leftPaddle.setVerticalPosition(GAME_HEIGHT - leftPaddle.getLength());
		}
		if (leftPaddle.getCoords()[0][1] <= 0) {
			leftPaddle.setVerticalPosition(0);
		}
	}

	// returns angles in [0, 2pi] radian format
	public double convertAngle(double angle) {
		return (angle + 2 * Math.PI) % (2 * Math.PI);
	}

	// run() method is what makes the game continue running without end. It calls
	// other methods to move objects, check for collision, and update the screen
	public void run() {
		// the CPU runs our game code too quickly - we need to slow it down! The
		// following lines of code "force" the computer to get stuck in a loop for short
		// intervals between calling other methods to update the screen.
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) { // this is the infinite game loop
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;

			// only move objects around and update screen if enough time has passed
			if (delta >= 1) {
				if (won == 0) {
					move();
					checkCollision();
				}
				repaint();
				delta--;

				// constant friction
				if (ball.getTrajectory()[1] - friction >= 0)
					ball.setSpeed(ball.getTrajectory()[1] - friction);

				// if the ball is still, then make it a tie and reset
				else {
					ball.setSpeed(0);
					if (ball.x > paddleBorder && ball.x + ball.getDiameter() < GAME_WIDTH - paddleBorder) {
						stuck = true;
						disable = true;
					}
				}
			}
		}
	}

	// keypresses get sent here, WASD for left paddle, arrow keys for right paddle,
	// r to toggle randomisation, n for next round
	public void keyPressed(KeyEvent e) {
		if (won != 0 && e.getKeyChar() == 'n') {
			background.stop();
			if (randomise) randomisedMusic.stop();
			Main.game.dispose();
			try {
				Main.game = new GameFrame(true);
			} catch (Exception e1) {
				System.out.println(
						"Sorry, something happened (honestly I'd be surprised if you got this message unless you deleted a file somewhere)!");
			}
			reset();
			randomise = false;
			won = 0;
			disable = false;
			stuck = false;
		}

		if (e.getKeyChar() == 'r') {
			randomise = !randomise;
			if (randomise) randomisedMusic.loop();
			else randomisedMusic.stop();
			return;
		}

		if (e.getKeyChar() == 'n') {
			reset();
			stuck = false;
			disable = false;
			newRound = false;
			return;
		}

		if (!disable) {
			Set<Character> temp = new HashSet<>(Arrays.asList(new Character[] { 'w', 'a', 's', 'd' }));
			if (temp.contains(e.getKeyChar()))
				leftPaddle.keyPressed(e);
			else
				rightPaddle.keyPressed(e);
		}
	}

	// keyreleases get sent here
	public void keyReleased(KeyEvent e) {
		Set<Character> temp = new HashSet<>(Arrays.asList(new Character[] { 'w', 'a', 's', 'd' }));
		if (temp.contains(e.getKeyChar()))
			leftPaddle.keyReleased(e);
		else
			rightPaddle.keyReleased(e);
	}

	// left empty because we don't need it; must be here because it is required to
	// be overridded by the KeyListener interface
	public void keyTyped(KeyEvent e) {
	}
}