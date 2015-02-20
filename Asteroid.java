import objectdraw.*;

import java.awt.*;

// Name: Sam Warren 
// Comments:

public class Asteroid extends ActiveObject {
	// constants
	// offsets used to draw the rock
	private static final double A = 2;
	private static final double B = .5;
	private static final double C = 1;
	private static final double DX = 1.5;
	private static final double DY = .5;
	private static final double EX = 2;
	private static final double EY = 1.3;
	private static final double FX = 1.4;
	private static final double FY = 3;

	// the amount which an asteroid should increase in speed when it has been
	// broken up into smaler asteroids
	private static final double INC = 1.5;

	// AMOUNT which a rock should grow when reincarnated
	private static final double SIZE_INC = 3.;

	// the number of lines in the rock outline
	private static final int NUM_LINES = 6;

	// THE PAUSE TIME
	private static final int PAUSE_TIME = 60;

	// the x and y speeds of the asteroid
	private double xSpeed;
	private double ySpeed;

	// the starting x and y location of the asteroid
	private Location locStart;

	// the ship
	private SpaceShip ship;

	// the asteroid is comprised of lines which are held in this array
	private Line[] outLine;

	// the width and height of the asteroid
	private double width;
	private double height;

	// the endpoints of the lines on the rock
	private Location a;
	private Location b;
	private Location c;
	private Location d;
	private Location e;
	private Location f;

	// the canvas
	private DrawingCanvas canvas;

	// the score needed when an asteroid is destroyed to increase the score
	private ScoreKeeper score;

	// constructor takes the x and y speeds and size of the asteroid as
	// parameters
	public Asteroid(double aXSpeed, double aYSpeed, Location aLocStart,
			double aWidth, DrawingCanvas aCanvas, ScoreKeeper aScore,
			SpaceShip aShip) {
		// save aXSpeed, aYSpeed, xLoc, yLoca, and acanvas parameters as
		// instance variables
		xSpeed = aXSpeed;
		ySpeed = aYSpeed;
		locStart = aLocStart;
		ship = aShip;
		canvas = aCanvas;
		score = aScore;

		// create the vertices of the asteroid outline
		Double x = aLocStart.getX();
		Double y = aLocStart.getY();
		a = new Location(x, (y + A * aWidth));
		b = new Location(x + (B * aWidth), y);
		c = new Location(x + (C * aWidth), y + aWidth);
		d = new Location(x + DX * aWidth, y + DY * aWidth);
		e = new Location(x + EX * aWidth, y + EY * aWidth);
		f = new Location(x + FX * aWidth, y + FY * aWidth);

		// set the width and height equal to the width and height of the
		// asteroid
		width = e.getX() - a.getX();
		height = f.getY() - b.getY();

		// create the outline array
		outLine = new Line[NUM_LINES];

		// line from A to B
		outLine[0] = new Line(a, b, canvas);
		// line from B to C
		outLine[1] = new Line(b, c, canvas);
		// line from C TO D
		outLine[2] = new Line(c, d, canvas);
		// line from D to E
		outLine[3] = new Line(d, e, canvas);
		// line from E to F
		outLine[4] = new Line(e, f, canvas);
		// line from F to A
		outLine[5] = new Line(f, a, canvas);

		makeWhite();

		start();
	}

	/*
	 * method to make the lines all white
	 */
	public void makeWhite() {
		// make all the lines white
		for (int i = 0; i < NUM_LINES; i++) {
			outLine[i].setColor(Color.white);
		}
	}

	// ACADEMIC HONESTY NOTE: I HAVE COPIED THIS CODE FROM LINE NINJA
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

	// ACADEMIC HONESTY NOTE: I HAVE COPIED some of THIS CODE FROM LINE NINJA
	// method to determine if two lines overlap
	// returns true if lineA and lineB overlap, and false otherwise
	public boolean linesIntersect() {
		while (true) {
			// for loop for the asteroid's lines
			for (int i = 0; i < NUM_LINES; i++) {
				// for loop for the ship's lines
				for (int j = 0; j < ship.shipOutLine().length; j++) {
					int o1 = orientation(outLine[i].getStart(),
							outLine[i].getEnd(),
							ship.shipOutLine()[j].getStart());
					int o2 = orientation(outLine[i].getStart(),
							outLine[i].getEnd(), ship.shipOutLine()[j].getEnd());
					int o3 = orientation(ship.shipOutLine()[j].getStart(),
							ship.shipOutLine()[j].getEnd(),
							outLine[i].getStart());
					int o4 = orientation(ship.shipOutLine()[j].getStart(),
							ship.shipOutLine()[j].getEnd(), outLine[i].getEnd());

					if (o1 != o2 && o3 != o4) {
						return true;
					}
				}
			}
			return false;
		}

	}

	/*
	 * method to see if this asteroid has hit the ship
	 */
	public boolean hasHitShip() {
		// see if the lines of the ship have intersected with the lines of the
		// asteroid
		return linesIntersect();

	}

	/*
	 * a method to make the asteroid into a smaller rock
	 */
	public void smallerRocks() {
		// make the asteroid smaller
		// by dividing the width by SIZE_INC
		width = width / SIZE_INC;

		// set the end points with new location being the first line's start
		makeEndPoints(width, outLine[0].getStart().getX(), outLine[0]
				.getStart().getY());

		// and make it a little faster
		xSpeed = INC * xSpeed;
		ySpeed = INC * ySpeed;

	}

	/*
	 * a method to reverse the speeds
	 */
	public void reverseSpeeds() {
		xSpeed = -xSpeed;
		ySpeed = -ySpeed;
	}

	/*
	 * the following methods are used to give the necesary info to create a new
	 * asteroid when it's parent asteroid has been destroyed
	 */
	public double theXSpeed() {
		return xSpeed;
	}

	public double theYSpeed() {
		return ySpeed;
	}

	public ScoreKeeper zScore() {
		return score;
	}

	/*
	 * a method to set the endPoints of the ship and construct the lines, used
	 * when an asteroid has been destroyed
	 */
	public void makeEndPoints(Double aWidth, Double x, Double y) {

		// create the end points based on locStart location and width
		a = new Location(x, (y + A * aWidth));
		b = new Location(x + (B * aWidth), y);
		c = new Location(x + (C * aWidth), y + aWidth);
		d = new Location(x + DX * aWidth, y + DY * aWidth);
		e = new Location(x + EX * aWidth, y + EY * aWidth);
		f = new Location(x + FX * aWidth, y + FY * aWidth);

		// and setting the lines endpoints to the new vertices
		// line from A to B
		outLine[0].setEndPoints(a, b);
		// line from B to C
		outLine[1].setEndPoints(b, c);
		// line from C TO D
		outLine[2].setEndPoints(c, d);
		// line from D to E
		outLine[3].setEndPoints(d, e);
		// line from E to F
		outLine[4].setEndPoints(e, f);
		// line from F to A
		outLine[5].setEndPoints(f, a);

		// make the lines white
		makeWhite();
	}

	public void destroyed() {
		// make the rock into a new, larger, rock
		width = SIZE_INC * width;
		// make the end points at the new location
		this.makeEndPoints(width, (0 - (xSpeed * width)),
				(0 - (ySpeed * width)));

	}

	public void hideShip() {
		// hide the lines
		for (int i = 0; i < NUM_LINES; i++) {
			outLine[i].hide();
		}
	}

	public void showShip() {
		// hide the lines
		for (int i = 0; i < NUM_LINES; i++) {
			outLine[i].show();
		}
	}

	/*
	 * method to return the rock's width
	 */
	public Double rockWidth() {
		return width;
	}

	/*
	 * method to return the outline of the rock
	 */
	public Line[] rockLines() {
		return outLine;
	}

	public void run() {
		// while true

		while (true) {

			// wrap
			// if it goes off of the left edge

			if (outLine[3].getEnd().getX() < 0) {
				for (int i = 0; i < NUM_LINES; i++) {
					outLine[i].move(canvas.getWidth() + width, 0);
				}
			}

			// if it goes off the right edge of the screen

			if (outLine[0].getStart().getX() > canvas.getWidth()) {
				for (int i = 0; i < NUM_LINES; i++) {
					outLine[i].move(-(canvas.getWidth() + width), 0);
				}
			}

			// if it goes off the bottom of the screen
			if (outLine[0].getEnd().getY() > canvas.getHeight()) {
				for (int i = 0; i < NUM_LINES; i++) {
					outLine[i].move(0, -(canvas.getHeight() + height));
				}
			}

			// if thisRock's y coordinate + thisRock's height is less than 0

			if (outLine[4].getEnd().getY() < 0) {
				for (int i = 0; i < NUM_LINES; i++) {
					outLine[i].move(0, (canvas.getHeight() + height));
				}
			}

			// if it hasn't gone off of the screen
			for (int i = 0; i < NUM_LINES; i++) {
				// move it in the necessary direction
				outLine[i].move(xSpeed, ySpeed);
			}
			//if it has hit the ship 
			if (this.hasHitShip()) {
				//set the score to game over
				score.gameOver();
				//kill the ship 
				ship.die();
			}

			pause(PAUSE_TIME);

		}
	}

}
