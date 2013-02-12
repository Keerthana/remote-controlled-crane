package manipulate;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.JComponent;
import javax.swing.JFrame;
public class CraneView extends JComponent{
	Crane c;
	
	public static void main(String[] args) {
		CraneView canvas = new CraneView();
		JFrame f = new JFrame("Crane View V 1");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(650, 450);
		f.setContentPane(canvas);
		f.setVisible(true);
	}
	
	public CraneView() {
	c = new Crane();	
	repaint();
	
	System.out.println(c.dispList);
	System.out.println(c.dispList);

	}
	public void paintComponent(Graphics g) {

		c.drawMe(g);
		System.out.println("done drawing");

	}
} 