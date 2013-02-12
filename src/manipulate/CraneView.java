package manipulate;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
public class CraneView extends JComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Crane c;
	private static Image backgroundImg;
	private static AlphaComposite compositeHalf, compositeOne;
	private Point2D mousePoint;
	private ArrayList <Point2D.Double> mouseTransform;

	public static void main(String[] args) throws IOException{
		CraneView canvas = new CraneView();
		JFrame f = new JFrame("Crane View V 1");
		backgroundImg = ImageIO.read(new File("splash_cloud.jpg"));
		compositeHalf = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		compositeOne = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		f.setContentPane(canvas);
		f.setVisible(true);
	}
	
	public CraneView() { 
		c = new Crane();
		repaint();
		mouseTransform = new ArrayList <Point2D.Double>();
	
	}
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setComposite(compositeHalf);
		int iw = backgroundImg.getWidth(this);
        int ih = backgroundImg.getHeight(this);
        if (iw > 0 && ih > 0) {
            for (int x = 0; x < getWidth(); x += iw) {
                for (int y = 0; y < getHeight(); y += ih) {
                    g2.drawImage(backgroundImg, x, y, iw, ih, this);
                }
            }
        }
        
		g2.setComposite(compositeOne);
		//g2.drawLine(0, getHeight() - 100 , 2000, getHeight() - 100);
		System.out.println("updated dimensions");
		c.updateFrameDims(getWidth(), getHeight());
		c.drawMe(g);
	}
	
	
	/*public void craneManipulate() {
		mousePoint = new Point2D.Double();
	
		// create two mouse listeners to deal with mouse events 
		MouseMotionListener hungryRat = new MouseMotionListener() {
	
			public void mouseDragged(MouseEvent e) {
	
				//	 grab mouse points 
				mousePoint.setLocation(e.getX(), e.getY());
				// transform the mouse points 
				try {
					for (int i = 0; i < 4; ++i)
						mouseTransform[i] = transforms[i].inverseTransform(
										mousePoint, null);
						} catch (NoninvertibleTransformException k) {
							k.printStackTrace();
						}

						// if the mouse is in the lower part of the crane 
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
//							 if the mouse is in the arm 1 
//							if (arms[0].contains(mouseTransform[0])) {
//								determineArmRotation(0, mousePoint, mouseTransform[0],
//										anchorPoints[0], anchorAxis[0]);
//							}
//
//							else if (arms[1].contains(mouseTransform[1])) {
//								determineArmRotation(1, mousePoint, mouseTransform[1],
//										anchorPoints[1], anchorAxis[1]);
//							}
//
//							else if (arms[2].contains(mouseTransform[2])) {
//								determineArmRotation(2, mousePoint, mouseTransform[2],
//										anchorPoints[2], anchorAxis[2]);
//							}
//
//							else if (arms[3].contains(mouseTransform[3])) {
//								determineArmRotation(3, mousePoint, mouseTransform[3],
//										anchorPoints[3], anchorAxis[3]);
//							}
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
	*/
} 