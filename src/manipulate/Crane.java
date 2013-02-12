package manipulate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Crane implements IDisplayable {
	public boolean craneMove = true;
	public Vector<IDisplayable> dispList = new Vector<IDisplayable>();

	public class CraneBase implements IDisplayable {

		Random generator = new Random();

		private int[] xbody = { 90, 210, 210, 190, 110, 90 };

		private int[] ybody = { 380, 380, 320, 300, 300, 320 };
		private int[] xfulcrum = { 135, 150, 165 };
		private int[] yfulcrum = { 300, 280, 300 };
		private Polygon fulcrum;
		private Polygon body;
		private Rectangle tracks;

		private Stroke simpleStroke = new BasicStroke(1);
		private Stroke thickStroke = new BasicStroke(4);
		private Stroke dottedStrokeStationary = new BasicStroke(4.0f,
				BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 2.0f,
				new float[] { 8.0f, 6.0f }, 0.0f);
		private Stroke dottedStrokeMove = new BasicStroke(4.0f,
				BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 2.0f,
				new float[] { 8.0f, 6.0f }, Math.abs((float) ((generator
						.nextGaussian() + 1) * 10.0f)));

		public CraneBase() {
			super();

			body = new Polygon(xbody, ybody, xbody.length);
			fulcrum = new Polygon(xfulcrum, yfulcrum, xfulcrum.length);
			tracks = new Rectangle(70, 380, 160, 25);

		}

		@Override
		public void drawMe(Graphics g) {
			// TODO Auto-generated method stub
			Graphics2D g2 = (Graphics2D) g;
			// Draw the body of the crane
			g2.setColor(blueish);
			g2.fillPolygon(body);
			g2.fillPolygon(fulcrum);

			g2.setColor(grayish);
			g2.fillRoundRect(tracks.x, tracks.y, tracks.width, tracks.height,
					10, 10);

			g2.setColor(blackish);
			g2.drawPolygon(body);
			g2.drawPolygon(fulcrum);

			if (craneMove == true)
				g2.setStroke(dottedStrokeMove);
			else
				g2.setStroke(dottedStrokeStationary);
			g2.drawRoundRect(tracks.x - 1, tracks.y - 1, tracks.width + 2,
					tracks.height + 2, 10, 10);

			// Draw the base line
			g2.setStroke(thickStroke);
			g2.setColor(greenish);
			g2.drawLine(0, 410, 2000, 410);

		}
	}
	
	public class CraneArm implements IDisplayable {

		private AffineTransform[] transforms;
		private double[] angles;
		private Point2D[] anchorPoints;
		private Rectangle[] arms;

		private Point2D mousePoint;

		private Point2D[] mouseTransform;
		private Point2D[] anchorAxis;

		public CraneArm() {
			super();

			arms = new Rectangle[4];
			arms[0] = new Rectangle(150, 290, 70, 20);
			arms[1] = new Rectangle(arms[0].x + 50, arms[0].y, arms[0].width,
					arms[0].height);
			arms[2] = new Rectangle(arms[0].x + 100, arms[0].y, arms[0].width,
					arms[0].height);
			arms[3] = new Rectangle(arms[0].x + 150, arms[0].y, arms[0].width,
					arms[0].height);

			// initialize points and transform used in calculations 
			mousePoint = new Point2D.Double();

			anchorPoints = new Point2D[4];
			anchorPoints[0] = new Point2D.Double(arms[0].x + arms[0].height / 2,
					arms[0].y + arms[0].height / 2);
			anchorPoints[1] = new Point2D.Double(arms[1].x + arms[1].height / 2,
					arms[1].y + arms[1].height / 2);
			anchorPoints[2] = new Point2D.Double(arms[2].x + arms[2].height / 2,
					arms[2].y + arms[2].height / 2);
			anchorPoints[3] = new Point2D.Double(arms[3].x + arms[3].height / 2,
					arms[3].y + arms[3].height / 2);

			mouseTransform = new Point2D[4];
			mouseTransform[0] = new Point2D.Double();
			mouseTransform[1] = new Point2D.Double();
			mouseTransform[2] = new Point2D.Double();
			mouseTransform[3] = new Point2D.Double();

			anchorAxis = new Point2D[4];
			anchorAxis[0] = new Point2D.Double(arms[0].x + 50, arms[0].y
					+ arms[0].height / 2);
			anchorAxis[1] = new Point2D.Double(arms[1].x + 50, arms[1].y
					+ arms[1].height / 2);
			anchorAxis[2] = new Point2D.Double(arms[2].x + 50, arms[2].y
					+ arms[2].height / 2);
			anchorAxis[3] = new Point2D.Double(arms[3].x + 50, arms[3].y
					+ arms[3].height / 2);

			transforms = new AffineTransform[4];
			transforms[0] = new AffineTransform();
			transforms[1] = new AffineTransform();
			transforms[2] = new AffineTransform();
			transforms[3] = new AffineTransform();

			angles = new double[4];
			// perform initialization rotations for the arm 
			transforms[0].rotate(-Math.PI / 4.0, 150, 300);
			transforms[0].translate(5, -10);
			transforms[1].rotate(-Math.PI / 6.0, 185, 300);
			transforms[1].translate(10, -42);

			transforms[2].rotate(-Math.PI / 8.0, 220, 300);
			transforms[2].translate(-10, -65);
			transforms[3].rotate(-Math.PI / 10.0, 255, 300);
			transforms[3].translate(-14, -65);

			angles[0] += -Math.PI / 4.0;
			angles[1] += -Math.PI / 6.0;

		}

		@Override
		public void drawMe(Graphics g) {
			// TODO Auto-generated method stub
			Graphics2D g2 = (Graphics2D) g;
			 // set transform to relevant arm transform and paint 
			AffineTransform savedAT = g2.getTransform();

			for (int i = 0; i < 4; i++) {
					//System.out.println("transforms[" + i + "] = " + transforms[i] + "\n");
					System.out.println("transforms[0] = " + transforms[0]);

					g2.setTransform(transforms[i]);
					

					g2.setColor(blueish);
					g2.fillRoundRect(arms[i].x, arms[i].y, arms[i].width,
							arms[i].height, 6, 6);
					g2.setColor(blackish);
					g2.drawRoundRect(arms[i].x, arms[i].y, arms[i].width,
							arms[i].height, 6, 6);

					// Draw pivots
					g2.setColor(reddish);
					g2.fillOval(arms[i].x + arms[i].height / 2, arms[i].y
							+ arms[i].height / 2, 4, 4);
				}
				g2.setTransform(savedAT);
		}
	}

	public Crane() {
		dispList.add(0, new CraneBase());
		dispList.add(0, new CraneArm());
	}

	@Override
	public void drawMe(Graphics g) {
		// TODO Auto-generated method stub
		for (IDisplayable i : dispList) {
			System.out.println("Draw");
			//Graphics g = null;
			i.drawMe(g);
			g.translate(0, 0);
		}
	}
}
