import objectdraw.*;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

//Name:SAM WARREN 
//Comments:Extra Credit implemented: the asteroid is made of lines not a gif to enable it to have no background, the ship moves and then drifts,
//the user can only fire a bullet after waiting for a certain amount of time, the ship is made up of an array of lines, ship "explodes" when killed, the asteroids break
//into smaller asteroids, and the points gained are equal to the size of the asteroid destroyed 

public class AsteroidsGame extends WindowController implements KeyListener {

	// the waiting time between shots fired
	private static final int WAIT = 250;

	// the size which all asteroids will be larger than
	private static final double MIN_SIZE = 5;

	private static final int NUM_SIDES = 3;

	// the size which all asteroids will be larger than
	private static final double MAX_SIZE = 25;

	// the minimum and maximum speeds which an asteroid can have
	private static final double MIN_SPEED = -2;
	private static final double MAX_SPEED = 2;

	// an array of asteroids
	private Asteroid[] theRocks;

	// the space ship
	private SpaceShip theShip;

	// the scoreKeeper
	private ScoreKeeper theScore;

	// the random int and double generators to determine the starting speed,
	// size, side, and location where the asteroid will go
	private RandomDoubleGenerator speedGen;
	private RandomDoubleGenerator sizeGen;
	private RandomIntGenerator sideGen;
	private RandomIntGenerator locXGen;
	private RandomIntGenerator locYGen;

	// the amount the gun rotates each time the player presses the left or right
	// key
	private static final double ROTATE_STEP = Math.PI / 12;

	// the maximum number of asteroids which can be on the screen
	private static final int MAX_ROCKS = 100;

	// the starting number of rocks on the screen
	private static final int START_ROCKS = 5;

	// where each asteroid will be placed
	private Location rockStart;

	// time when a bullet is fired
	private Long fired;

	public void begin() {

		fired = System.currentTimeMillis();
		// create speedGen with values between 0 and 50
		speedGen = new RandomDoubleGenerator(MIN_SPEED, MAX_SPEED);

		// create sizeGen with values between 0 and 50
		sizeGen = new RandomDoubleGenerator(MIN_SIZE, MAX_SIZE);

		// create locXGen with values between 0 and the canvas's width
		locXGen = new RandomIntGenerator(0, canvas.getWidth());

		// create locYgen with values between 0 and the canvas's height
		locYGen = new RandomIntGenerator(0, canvas.getHeight());

		// create sideGen with values between 0 and 3
		sideGen = new RandomIntGenerator(0, NUM_SIDES);

		// create the scoreboard
		theScore = new ScoreKeeper(canvas);
		// create the ship
		theShip = new SpaceShip(canvas, START_ROCKS);

		// create the aray of asteroids
		theRocks = new Asteroid[MAX_ROCKS];
		// create the array of asteroids
		for (int i = 0; i < START_ROCKS; i++) {
			// all of them will have size = sizeAndSpeedGen's next value, both x
			// and yspeed = sizeAndSpeedGen's next values,

			// if sideGen's next value equals 0, place the new asteroid on the
			// left border of the screen, x=0 y=locYGen.next value

			if (sideGen.nextValue() == 0) {
				rockStart = new Location(0, locYGen.nextValue());
			}

			// if sideGen's next value equals 1, place the new
			// asteroid on the bottom border of the screen, x=locXGen.next value
			// y=canvas.getHieght

			else if (sideGen.nextValue() == 1) {
				rockStart = new Location(locXGen.nextValue(),
						canvas.getHeight());
			}

			// if its 2, place it on the top, x=locXGen.next value y=0
			else if (sideGen.nextValue() == 2) {
				rockStart = new Location(locXGen.nextValue(), 0);
			}

			// else place it on the right x=canvas's width y=locYGen. next value
			else {
				rockStart = new Location(canvas.getWidth(), locYGen.nextValue());
			}

			// create the asteroid passing in the necessary parameters
			theRocks[i] = new Asteroid(speedGen.nextValue(),
					speedGen.nextValue(), rockStart, sizeGen.nextValue(),
					canvas, theScore, theShip);

		}

		// create the hidden asteroids which will be made into new asteroids
		// when necesary
		for (int k = START_ROCKS; k < MAX_ROCKS; k++) {
			Location deadLoc = new Location(0, 0);
			theRocks[k] = new Asteroid(0, 0, deadLoc, 0, canvas, theScore,
					theShip);
			theRocks[k].hideShip();
		}

		// make the background
		FilledRect background = new FilledRect(0, 0, canvas.getWidth(),
				canvas.getHeight(), canvas);
		background.setColor(Color.BLACK);
		background.sendToBack();

		// getting ready to respond to user's key presses
		canvas.addKeyListener(this);
	}

	// Methods required by KeyListener interface.
	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {

			// when the user has released the up key, the ship will slow down
			// slightly although not entirely
			// because its space and there is technically no friction
			theShip.slowDown();

		}
	}

	public void keyPressed(KeyEvent e) { // check that theShip is alive, by
											// calling the isAlive check on
											// theShip and making sure its true,

		if (theShip.isnotDead() && !theScore.hasWon()) {
			// and the game has not been won, by calling hasWon on theScore and
			// making sure its false
			if (e.getKeyCode() == KeyEvent.VK_SPACE
					&& ((System.currentTimeMillis() - fired) > WAIT)) {
				fired = System.currentTimeMillis();
				// create the bullet when user presses space
				new Bullet(theRocks, theShip.bulletXSpeed(),
						theShip.bulletYSpeed(), canvas, theShip, theScore);

			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

				// call method rotate on theShip pass in parameter -rotateStep
				theShip.rotate(ROTATE_STEP);

			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {

				// call method rotate on theShip pass in parameter rotateStep
				theShip.rotate(-ROTATE_STEP);

			} else if (e.getKeyCode() == KeyEvent.VK_UP) {

				// call method rotate on theShip pass in parameter rotateStep
				theShip.moveShip();

			}
		}
	}

}
