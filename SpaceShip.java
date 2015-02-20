import objectdraw.*;
import java.awt.*;

//Name:Sam Warren 
//Comments:

public class SpaceShip extends ActiveObject {

	// the width of the body of the ship
	private static final int BODY_WIDTH = 20;
	// pause time for movement of ship
	private static final int PAUSE_TIME = 30;
	// lenth of the ship
	private static final int GUN_LENGTH = 7;
	// the number of lines in the array which is the ship
	private static final int NUM_LINES = 6;

	private int theNumberOfLiveRocks;

	// the outline of the ship
	private Line[] shipLines;

	// boolean for whether the ship is alive
	private Boolean isAlive;

	private DrawingCanvas canvas;

	// the central point of the ship, on which it will rotate
	private Location center;

	// the total amount which the ship has rotated
	private double totalRot;

	// the accleration/speed of the ship
	private double accelerator;

	// the x and y ship speeds
	private double xShipSpeed;
	private double yShipSpeed;

	// takes parameter the canvas
	public SpaceShip(DrawingCanvas acanvas, int startRocks) {
		theNumberOfLiveRocks = startRocks;
		// set alive to true
		isAlive = true;
		// save the parameters to instance variables
		canvas = acanvas;

		// create the array
		shipLines = new Line[NUM_LINES];
		// the ship has not yet rotates so totalRot is 0
		totalRot = 0;

		// create the first line starting at the center of the screen
		shipLines[0] = new Line(canvas.getWidth() / 2, canvas.getHeight() / 2,
				canvas.getWidth() / 2 + BODY_WIDTH / 2 + GUN_LENGTH,
				canvas.getHeight() / 2, canvas);

		// the location of the remaining lines does not matter because rotate
		// method will put them in correct place
		// they just need to be constructed
		for (int i = 1; i < NUM_LINES; i++) {
			shipLines[i] = new Line(0, 0, 0, 0, canvas);
		}

		// set the center
		center = new Location(shipLines[0].getStart());

		// set the colors
		shipLines[0].setColor(Color.GREEN);
		shipLines[1].setColor(Color.CYAN);
		shipLines[2].setColor(Color.BLUE);
		shipLines[3].setColor(Color.GREEN);
		shipLines[4].setColor(Color.CYAN);
		shipLines[5].setColor(Color.BLUE);

		// rotate to put them in the right place
		this.rotate(0.);

		// ship should not move at begining so accelerator should be zero
		accelerator = 0.;

		start();
	}

	/*
	 * run method to move ship (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (isAlive) {

			// if the ship goes off the right side of the screen
			if (shipLines[3].getEnd().getX() > canvas.getWidth()
					&& shipLines[4].getEnd().getX() > canvas.getWidth()
					&& shipLines[5].getEnd().getX() > canvas.getWidth()) {
				for (int i = 0; i < NUM_LINES; i++) {
					shipLines[i].move(-canvas.getWidth(), 0);
				}
			}

			// if the ship goes off of the left side of the screen
			if (shipLines[3].getEnd().getX() < 0
					&& shipLines[4].getEnd().getX() < 0
					&& shipLines[5].getEnd().getX() < 0) {
				for (int i = 0; i < NUM_LINES; i++) {
					shipLines[i].move(canvas.getWidth(), 0);
				}
			}

			// if the ship goes off of the bottom of the screen
			if (shipLines[3].getEnd().getY() > canvas.getHeight()
					&& shipLines[4].getEnd().getY() > canvas.getHeight()
					&& shipLines[5].getEnd().getY() > canvas.getHeight()) {
				for (int i = 0; i < NUM_LINES; i++) {
					shipLines[i].move(0, -canvas.getHeight());
				}
			}

			// if the ship goes off of the top of the screen
			if (shipLines[3].getEnd().getY() < 0
					&& shipLines[4].getEnd().getY() < 0
					&& shipLines[5].getEnd().getY() < 0) {
				for (int i = 0; i < NUM_LINES; i++) {
					shipLines[i].move(0, canvas.getHeight());
				}
			}

			// move all the lines
			for (int i = 0; i < NUM_LINES; i++) {
				shipLines[i].move(xShipSpeed * accelerator, yShipSpeed
						* accelerator);
			}
			// update center
			center = shipLines[0].getStart();
			// pause
			pause(PAUSE_TIME);
		}

	}

	/*
	 * method to determine if the ship is alive
	 */
	public boolean isnotDead() {

		return isAlive;
	}

	/*
	 * method which causes the ship to move
	 */
	public void moveShip() {
		// the direction of the speeds is equivalent to that of a bullet
		xShipSpeed = bulletXSpeed();
		yShipSpeed = bulletYSpeed();
		// increase accelerator to increase speed
		accelerator += .1;
		// pause
		pause(PAUSE_TIME);

	}

	/*
	 * method to slow the ship down
	 */
	public void slowDown() {
		// as long as the acclerator is larger than zero
		if (accelerator > 0) {
			// halve it
			accelerator = .5 * accelerator;
			pause(PAUSE_TIME);
		}

	}

	/*
	 * return the array of lines which comprise the ship
	 */
	public Line[] shipOutLine() {
		return shipLines;
	}

	/*
	 * method to return the center of the ship used in bullet to determine its
	 * starting loc
	 */
	public Location theCenter() {
		return center;
	}

	/*
	 * method to calculate a bullet's x speed
	 */
	public double bulletXSpeed() {
		// return the gun's EndPoint's x coordinate minus the center
		return (shipLines[0].getEnd().getX() - center.getX());
	}

	/*
	 * a method to calculate the bullet's y speed
	 */
	public double bulletYSpeed() {
		// return the gun's EndPoint's y coordinate minus the center
		return (shipLines[0].getEnd().getY() - center.getY());
	}

	/*
	 * a method to rotate the ship
	 */
	public void rotate(Double rotation) {
		// keep track of total rotation
		totalRot += rotation;

		// the bows are the three points which form the triangle of the ship

		Location bow = new Location(center.getX()
				+ (BODY_WIDTH / 2 + GUN_LENGTH) * Math.cos(totalRot),
				center.getY() + (BODY_WIDTH / 2 + GUN_LENGTH)
						* Math.sin(totalRot));

		Location bow2 = new Location(center.getX()
				+ (BODY_WIDTH / 2 + GUN_LENGTH)
				* Math.cos(totalRot + 2.61799387799), center.getY()
				+ (BODY_WIDTH / 2 + GUN_LENGTH)
				* Math.sin(totalRot + (2.61799387799)));

		Location bow3 = new Location(center.getX()
				+ (BODY_WIDTH / 2 + GUN_LENGTH)
				* Math.cos(totalRot - 2.61799387799), center.getY()
				+ (BODY_WIDTH / 2 + GUN_LENGTH)
				* Math.sin(totalRot - (2.61799387799)));

		// set the points of the lines such that the shape of the ship is
		// preserved
		shipLines[0].setEnd(bow);
		shipLines[1].setEnd(bow2);
		shipLines[1].setStart(shipLines[0].getStart());
		shipLines[2].setEndPoints(shipLines[0].getStart(), bow3);
		// the last three lines form the outside of the ship and are just formed
		// by connecting the other lines' endpoints
		shipLines[3].setEndPoints(shipLines[0].getEnd(), shipLines[1].getEnd());
		shipLines[4].setEndPoints(shipLines[0].getEnd(), shipLines[2].getEnd());
		shipLines[5].setEndPoints(shipLines[1].getEnd(), shipLines[2].getEnd());

	}

	/*
	 * a method for when the ship dies
	 */
	public void die() {
		// save the central location of the ship
		Location blast = center;
		// set isAlive to false
		isAlive = false;
		// explode the ship

		while (shipLines[2].getEnd().getX() < canvas.getWidth()
				&& shipLines[2].getEnd().getX() > 0) {
			for (int i = 0; i < NUM_LINES; i++) {
				shipLines[i].setColor(Color.LIGHT_GRAY);
				// move each line away from where the center of the ship was
				shipLines[i].move(shipLines[i].getEnd().getX() - blast.getX(),
						shipLines[i].getEnd().getY() - blast.getY());
				pause(PAUSE_TIME);

			}
		}
		// once the lines are off of the screen, remove them
		for (int i = 0; i < NUM_LINES; i++) {
			shipLines[i].hide();
		}

	}

	/*
	 * because the ship is the only object on the screen which will not be
	 * deleted use it to keep track of the number of rocks
	 */
	public int numLiveRocks() {
		return theNumberOfLiveRocks;
	}

	/*
	 * method to add one to the number of rocks
	 */
	public void addRock() {
		theNumberOfLiveRocks += 1;
	}
}
