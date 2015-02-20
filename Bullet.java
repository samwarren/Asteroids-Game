import objectdraw.*;

import java.awt.*;

//Name:Sam Warren 
//Comments:

public class Bullet extends ActiveObject {

	private static final int PAUSE = 30;
	// size of bullet
	private static final int SIZE = 10;
	// the minimum width a rock can be to be broken up
	private static final int MIN_WIDTH = 10;

	// the array of asteroids
	private Asteroid[] theAsteroids;

	// the line which is the bullet
	private Line thisBullet;

	// the speeds the bullet will move at to be saved from parameters
	private double dxSpeed;
	private double dySpeed;

	// the ship which will be passed in, necesary for the starting loc of the
	// bullet
	private SpaceShip ship;

	private ScoreKeeper score;

	private DrawingCanvas canvas;

	public Bullet(Asteroid[] aAsteroids, double aDxSpeed, double aDySpeed,
			DrawingCanvas aCanvas, SpaceShip aShip, ScoreKeeper aScore) {
		// save parameters as instance variables
		theAsteroids = aAsteroids;
		score = aScore;

		dxSpeed = aDxSpeed;
		dySpeed = aDySpeed;
		canvas = aCanvas;
		ship = aShip;

		// start point
		Location start = new Location(aShip.theCenter().getX(), aShip
				.theCenter().getY());
		// the bullet
		thisBullet = new Line(start, aShip.shipOutLine()[0].getEnd(), canvas);

		// set color to red
		thisBullet.setColor(Color.RED);
		start();
	}

	// COPIED FROM LINE NINJA
	// Helper method used to determine if lines overlap
	public int orientation(Location p, Location q, Location r) {
		double ret = (q.getY() - p.getY()) * (r.getX() - q.getX())
				- (q.getX() - p.getX()) * (r.getY() - q.getY());
		if (ret == 0) {
			return 0;
		}

		if (ret > 0) {
			// clockwise
			return 1;
		} else {
			// counterclockwise
			return 2;
		}
	}

	/*
	 * WITH CODE COPIED FROM LINE NINJA method to determine if the bullet has
	 * intersetected with any of the lines in an asteroid in the array of
	 * asteroids
	 */
	public boolean linesIntersect() {
		while (true) {
			// for loop checks the number of live asteroids
			for (int i = 0; i < theAsteroids.length; i++) {
				// for loop checks the array of the lines that make up an
				// asteroid
				for (int j = 0; j < theAsteroids[i].rockLines().length; j++) {
					int o1 = orientation(
							theAsteroids[i].rockLines()[j].getStart(),
							theAsteroids[i].rockLines()[j].getEnd(),
							thisBullet.getStart());
					int o2 = orientation(
							theAsteroids[i].rockLines()[j].getStart(),
							theAsteroids[i].rockLines()[j].getEnd(),
							thisBullet.getEnd());
					int o3 = orientation(thisBullet.getStart(),
							thisBullet.getEnd(),
							theAsteroids[i].rockLines()[j].getStart());
					int o4 = orientation(thisBullet.getStart(),
							thisBullet.getEnd(),
							theAsteroids[i].rockLines()[j].getEnd());

					// the code below means that the bullet's line and the
					// asteroids outlines have intersected
					// thus the user has registered a hit
					if (o1 != o2 && o3 != o4) {
						score.plusTen(theAsteroids[i].rockWidth());
						// if the rock's width is greater than , break the rock
						// into two smaller ones
						if (theAsteroids[i].rockWidth() > MIN_WIDTH) {

							// break of the i'th asteroid into a smaller rock
							theAsteroids[i].smallerRocks();

							// add one to the number of live rocks
							ship.addRock();

							// create a new asteroid in the slot occupied by one
							// less than live rocks equivalent to the ith
							// asteroid, the parameters passed in come from the
							// parent asteroid
							theAsteroids[ship.numLiveRocks() - 1] = new Asteroid(
									theAsteroids[i].theXSpeed(),
									theAsteroids[i].theYSpeed(),
									theAsteroids[i].rockLines()[0].getStart(),
									theAsteroids[i].rockWidth(), canvas,
									theAsteroids[i].zScore(), ship);

							// reverse its speed so it moves in a different
							// direction than its brother asteroid
							theAsteroids[ship.numLiveRocks() - 1]
									.reverseSpeeds();

						} else {
							// else the asteroid is too small
							theAsteroids[i].destroyed();
						}

						return true;
					}
				}
			}
			return false;
		}

	}

	/*
	 * boolean to determine if the bullet has hit the rock
	 */
	public boolean hasHitRocks() {

		return linesIntersect();

	}

	public void run() {
		// while the bullet is within the screen, and hasn't hit an asteroid {
		while (thisBullet.getEnd().getX() < canvas.getWidth()
				&& (thisBullet.getEnd().getX() + SIZE) > 0
				&& thisBullet.getEnd().getY() < canvas.getHeight()
				&& thisBullet.getEnd().getY() + SIZE > 0 && !hasHitRocks()) {

			// move the bullet
			thisBullet.move(dxSpeed, dySpeed);
			pause(PAUSE);

		}
		// it must have hit a rock or gone off the screen
		// so remove it FromCanvas
		thisBullet.removeFromCanvas();
	}

}
