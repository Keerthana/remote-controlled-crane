package manipulate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.util.Random;

public interface IDisplayable {

	public Color grayish = new Color(85, 85, 85);
	public Color blueish = new Color(51, 102, 184);
	public Color reddish = new Color(251, 52, 1);
	public Color blackish = new Color(25, 25, 25);
	public Color greenish = new Color(51, 182, 4);
	public Color brownish = new Color(102, 0, 0);
	
	Random generator = new Random();
	public Stroke simpleStroke = new BasicStroke(1);
	public Stroke thickStroke = new BasicStroke(15);
	public Stroke dottedStrokeStationary = new BasicStroke(4.0f,
			BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 2.0f,
			new float[] { 8.0f, 6.0f }, 0.0f);
	public Stroke dottedStrokeMove = new BasicStroke(4.0f,
			BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 2.0f,
			new float[] { 8.0f, 6.0f }, Math.abs((float) ((generator
					.nextGaussian() + 1) * 10.0f)));
	
	// Member functions
	public void drawMe(Graphics g);

	public void updateLocation(double shift);
}
