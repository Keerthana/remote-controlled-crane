package manipulate;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Vector;

public class Crane implements IDisplayable {
	public boolean craneMove = true;
	public Vector<IDisplayable> dispList = new Vector<IDisplayable>();
	public int frameWidth = 800, frameHeight = 600;
	private int terrainLevel, truckLevel;
	private static int truckStart = 100;
	private int truckWidth, truckHeight, truckMaxHeight;
	
	private static int fulcrumHeight = 20;
	
	//Initialize truck dimensions
	private int[] xbody = new int[6];
	private int[] ybody = new int[6];
 
	private int[] xfulcrum = new int[3];
	private int[] yfulcrum = new int[3];
	private Polygon fulcrum;
	private Polygon body;
	private Rectangle tracks;
	
	public class CraneBase implements IDisplayable {

		public CraneBase() {
			super();	
		}

		@Override
		public void drawMe(Graphics g) {

			Graphics2D g2 = (Graphics2D) g;
			body = new Polygon(xbody, ybody, xbody.length);
			fulcrum = new Polygon(xfulcrum, yfulcrum, xfulcrum.length);
			tracks = new Rectangle(truckStart - 20, truckLevel, truckWidth + 40, terrainLevel - truckLevel);

			// Draw the body of the crane
			g2.setColor(blueish);
			g2.setStroke(simpleStroke);
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

		}
	}
	
	public class CraneArm implements IDisplayable {

		private AffineTransform[] transforms;
		private double[] angles;
		private Point2D[] anchorPoints;
		private Rectangle[] arms;

		private Point2D mousePoint;

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

			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(simpleStroke);
			
			// set transform to relevant arm transform and paint 
			AffineTransform savedAT = g2.getTransform();

			for (int i = 0; i < 4; i++) {
					//System.out.println("transforms[" + i + "] = " + transforms[i] + "\n");
					//System.out.println("transforms[0] = " + transforms[0]);

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

	public class Terrain implements IDisplayable {

		@Override
		public void drawMe(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			// Draw the road/ terrain
			g2.setColor(brownish);
			g2.setStroke(new BasicStroke(frameHeight / 9));			
			g2.drawLine(0, (int)(1.07f * terrainLevel), 2000, (int)(1.07f * terrainLevel));
			
			g2.setColor(greenish);
			g2.setStroke(thickStroke);		
			System.out.println("terrainlevel 6 " + terrainLevel);
			g2.drawLine(0, (int)(1.00f * terrainLevel), 2000, (int)(1.00f * terrainLevel));
			
		}
		
	}
	public Crane() {
		dispList.add(0, new Terrain());
		dispList.add(1, new CraneBase());
		dispList.add(2, new CraneArm());

	}

	@Override
	public void drawMe(Graphics g) {
		for (IDisplayable i : dispList) {
			//System.out.println("Draw");
			i.drawMe(g);
			g.translate(0, 0);
		}
	}

	public void updateFrameDims(int width, int height) {
		frameWidth = width;
		frameHeight = height;
		terrainLevel=  (int)(0.9f * frameHeight);
		truckLevel = (int)(0.875f * frameHeight);
		System.out.println("terrainlevel 1 " + terrainLevel);


		truckHeight = frameWidth / 20;
		truckWidth = truckHeight * 2;
		truckMaxHeight = (int) (truckHeight * 1.33f);
		xbody = new int[] { truckStart, truckStart + truckWidth,
				truckStart + truckWidth, truckStart + truckWidth - 20,
				truckStart + 20, truckStart};
		ybody = new int[] { truckLevel, truckLevel, truckLevel - truckHeight,
				truckLevel - truckMaxHeight, truckLevel - truckMaxHeight,
				truckLevel - truckHeight};
		
		xfulcrum = new int[] { truckStart + truckWidth/3, truckStart + 2*truckWidth/3, truckStart + truckWidth/2};
		yfulcrum = new int[] { truckLevel - truckMaxHeight,
				truckLevel - truckMaxHeight,
				truckLevel - truckMaxHeight - fulcrumHeight };
	}
}
