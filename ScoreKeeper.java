import objectdraw.*;
import java.awt.*;

import javax.swing.JLabel;

//Name: Sam Warren 
//Comments:

//class 
public class ScoreKeeper {

	// constants
	// height of grey box at the bottom
	private static final int BOX_HEIGHT = 30;
	// text offset from center
	private static final int OFFSET = 30;

	// score user must reach to win
	private static final int GOAL = 200;

	private DrawingCanvas canvas;
	// the score
	private int score;
	// the text which will display the score
	private Text scoreText;
	// the filledRect for the border
	private FilledRect border;

	public ScoreKeeper(DrawingCanvas acanvas) {
		// save canvas and set score to zero
		canvas = acanvas;
		score = 0;

		// create the border and set its color
		border = new FilledRect(0, canvas.getHeight() - BOX_HEIGHT,
				canvas.getWidth(), BOX_HEIGHT, canvas);
		border.setColor(Color.LIGHT_GRAY);

		// create the text
		scoreText = new Text("Score " + score, canvas.getWidth() / 2 - OFFSET,
				canvas.getHeight() - BOX_HEIGHT, canvas);

	}

	// method to add points to the score
	public void plusTen(Double increase) {
		// increase score by increase (which is the width of the destroyed
		// asteroid)
		score += increase;
		// update the scoreText to read "score: " + score; i.e "score: 30"
		scoreText.setText("score: " + score);
		// check to see if the score > 200
		if (score > GOAL) {
			win();
		}

	}

	// method to make game over text
	public void gameOver() {
		if (!hasWon()) {
			// setText of scoreText to "game over"
			scoreText.setText("Game over...");
		}
	}

	
	// method to make win text
	public void win() {

		// setText of scoreText to "You Win "
		scoreText.setText("You Win");
	}

	public boolean hasWon() {
		// return true if the text of scoreText is "You Win"
		return scoreText.getText().equals("You Win");
	}

}