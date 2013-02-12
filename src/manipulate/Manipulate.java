/*package manipulate;

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

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Manipulate extends JComponent {

	*//**
	 * 
	 *//*
	private static final long serialVersionUID = 1L;
	private AffineTransform[] transforms;
	private double[] angles;
	private Point2D[] anchorPoints;
	private Rectangle[] arms;

	private Point2D mousePoint;

	private Point2D[] mouseTransform;
	private Point2D[] anchorAxis;

	private int[] xbody = { 90, 210, 210, 190, 110, 90 };
	private int[] ybody = { 380, 380, 320, 300, 300, 320 };
	private int[] xfulcrum = { 135, 150, 165 };
	private int[] yfulcrum = { 300, 280, 300 };
	private Polygon body;
	private Rectangle tracks = new Rectangle(70, 380, 160, 25);;
	private Polygon fulcrum;

	private double shift;
	private boolean craneMove = false;

	public static void main(String[] args) {
		Manipulate canvas = new Manipulate();
		JFrame f = new JFrame("Rotate Triangle V 1");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(650, 450);
		f.setContentPane(canvas);
		f.setVisible(true);
	}

	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Random generator = new Random();
		Stroke simpleStroke = new BasicStroke(1);
		Stroke thickStroke = new BasicStroke(4);
		Stroke dottedStrokeStationary = new BasicStroke(4.0f,
				BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 2.0f,
				new float[] { 8.0f, 6.0f }, 0.0f);
		Stroke dottedStrokeMove = new BasicStroke(4.0f, BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_MITER, 2.0f, new float[] { 8.0f, 6.0f },
				Math.abs((float) ((generator.nextGaussian() + 1) * 10.0f)));

		Color grayish = new Color(85, 85, 85);
		Color blueish = new Color(51, 102, 184);
		Color reddish = new Color(251, 52, 1);
		Color blackish = new Color(25, 25, 25);
		Color greenish = new Color(51, 182, 4);
		AffineTransform savedAT = g2.getTransform();

		// Draw the body of the crane
		g2.setColor(blueish);
		g2.fillPolygon(body);
		g2.fillPolygon(fulcrum);

		g2.setColor(grayish);
		g2.fillRoundRect(tracks.x, tracks.y, tracks.width, tracks.height, 10,
				10);

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

		g2.setStroke(simpleStroke);

		 set transform to relevant arm transform and paint 
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

	public Manipulate() {

		 initialize crane components 
		super();

		body = new Polygon(xbody, ybody, xbody.length);
		fulcrum = new Polygon(xfulcrum, yfulcrum, xfulcrum.length);

		arms = new Rectangle[4];
		arms[0] = new Rectangle(150, 290, 70, 20);
		arms[1] = new Rectangle(arms[0].x + 50, arms[0].y, arms[0].width,
				arms[0].height);
		arms[2] = new Rectangle(arms[0].x + 100, arms[0].y, arms[0].width,
				arms[0].height);
		arms[3] = new Rectangle(arms[0].x + 150, arms[0].y, arms[0].width,
				arms[0].height);

		 initialize points and transform used in calculations 
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
		 perform initialization rotations for the arm 
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

		 create two mouse listeners to deal with mouse events 
		MouseMotionListener hungryRat = new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {

				 grab mouse points 
				mousePoint.setLocation(e.getX(), e.getY());
				 transform the mouse points 
				try {
					for (int i = 0; i < 4; ++i)
						mouseTransform[i] = transforms[i].inverseTransform(
								mousePoint, null);
				} catch (NoninvertibleTransformException k) {
					k.printStackTrace();
				}

				 if the mouse is in the lower part of the crane 
				if (body.contains(mousePoint.getX(), mousePoint.getY()) ||
				    tracks.contains(mousePoint.getX(), mousePoint.getY()) ||
				    fulcrum.contains(mousePoint.getX(), mousePoint.getY())) {

					 move the crane horizontally 

					 body 
					shift = mousePoint.getX() - shift;
					craneMove = true;
					body.translate((int) shift, 0);
					tracks.translate((int) shift, 0);
					fulcrum.translate((int) shift, 0);

					 arms 
					shiftArm(transforms[0]);
					shiftArm(transforms[1]);
					shiftArm(transforms[2]);
					shiftArm(transforms[3]);

					shift = mousePoint.getX();
				} else {
					craneMove = false;
					 if the mouse is in the arm 1 
					if (arms[0].contains(mouseTransform[0])) {
						determineArmRotation(0, mousePoint, mouseTransform[0],
								anchorPoints[0], anchorAxis[0]);
					}

					else if (arms[1].contains(mouseTransform[1])) {
						determineArmRotation(1, mousePoint, mouseTransform[1],
								anchorPoints[1], anchorAxis[1]);
					}

					else if (arms[2].contains(mouseTransform[2])) {
						determineArmRotation(2, mousePoint, mouseTransform[2],
								anchorPoints[2], anchorAxis[2]);
					}

					else if (arms[3].contains(mouseTransform[3])) {
						determineArmRotation(3, mousePoint, mouseTransform[3],
								anchorPoints[3], anchorAxis[3]);
					}
				}

				repaint();
			}

			public void mouseMoved(MouseEvent e) {

			}
		};

		MouseListener fullRat = new MouseListener() {

			public void mousePressed(MouseEvent e) {
				shift = e.getX();
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		};
		addMouseMotionListener(hungryRat);
		addMouseListener(fullRat);
	}

	public void determineArmRotation(int index, Point2D mPoint, Point2D mTrans,
			Point2D anch, Point2D axis) {

		if (!(body.contains(mPoint.getX(), mPoint.getY())
				|| tracks.contains(mPoint.getX(), mPoint.getY()) || fulcrum
					.contains(mPoint.getX(), mPoint.getY()))) {

			
			 * create a vector along the rotation axis and a vector to the mouse
			 * point
			 
			Point2D vector1 = new Point2D.Double(mTrans.getX() - anch.getX(),
					mTrans.getY() - anch.getY());

			Point2D vector2 = new Point2D.Double(axis.getX() - anch.getX(),
					axis.getY() - anch.getY());

			 calculate dot product, magnitude 
			double dotProduct = (vector1.getX() * vector2.getX())
					+ (vector1.getY() * vector2.getY());

			double magAnchorAxis = Math.sqrt(Math.pow(vector2.getX(), 2)
					+ Math.pow(vector2.getY(), 2));

			double magMousePoint = Math.sqrt(Math.pow(vector1.getX(), 2)
					+ Math.pow(vector1.getY(), 2));

			 determine the rotation value 
			double cosArg = dotProduct / (magAnchorAxis * magMousePoint);

			double theta = Math.acos(cosArg);
			if (mTrans.getY() < axis.getY()) {
				theta = -1 * theta;
			}

			 grab angles of relevant arms 
			double angle1 = angles[index];
			double angle2 = angles[index + 1];

			 compute difference between angles 
			double angleDifference = Math.abs(angle1 - angle2);
			double negDifference = -1 * angleDifference;
//			System.out.println("angle[0] =" + angles[index]);
//			System.out.println("angle[1] =" + angles[index + 1]);

			if (angle1 > angle2) {

				 rotate the child to same angle as parent 
				transforms[index + 1].rotate(angleDifference,
						anchorPoints[index + 1].getX(),
						anchorPoints[index + 1].getY());

				
				 * rotate arm as requested by user, moving all arms connected to
				 * fulcrum arm as a consequence
				 
				transforms[index].rotate(theta, anchorPoints[index].getX(),
						anchorPoints[index].getY());

				angles[index] += theta;

				transforms[index + 1].rotate(theta, anchorPoints[index].getX(),
						anchorPoints[index].getY());

				 rotate child arm back to original angle 
				transforms[index + 1].rotate(negDifference,
						anchorPoints[index + 1].getX(),
						anchorPoints[index + 1].getY());
				angle2 += theta;
				return;
			}

			transforms[index + 1].rotate(negDifference,
					anchorPoints[index + 1].getX(),
					anchorPoints[index + 1].getY());

			transforms[index].rotate(theta, anchorPoints[index].getX(),
					anchorPoints[index].getY());

			angle1 += theta;

			transforms[index + 1].rotate(theta, anchorPoints[index].getX(),
					anchorPoints[index].getY());

			transforms[index + 1].rotate(angleDifference,
					anchorPoints[index + 1].getX(),
					anchorPoints[index + 1].getY());
			angle2 += theta;
			return;
		}
	}

	public void shiftArm(AffineTransform at) {

		AffineTransform inverse = new AffineTransform();
		AffineTransform copy = new AffineTransform(at);
		try {
			inverse = at.createInverse();
		} catch (NoninvertibleTransformException k) {
			k.printStackTrace();
		}

		at.concatenate(inverse);
		at.translate(shift, 0);
		at.concatenate(copy);
	}
}*/