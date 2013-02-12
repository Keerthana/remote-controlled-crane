package manipulate;

import java.awt.Color;
import java.awt.Graphics;

public interface IDisplayable {

	public Color grayish = new Color(85, 85, 85);
	public Color blueish = new Color(51, 102, 184);
	public Color reddish = new Color(251, 52, 1);
	public Color blackish = new Color(25, 25, 25);
	public Color greenish = new Color(51, 182, 4);

	public void drawMe(Graphics g);

}
