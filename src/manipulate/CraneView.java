package manipulate;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

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
	private Point2D[] mouseTransform;
	private AffineTransform[] transforms;

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
		mousePoint = new Point2D.Double();
		mouseTransform = new Point2D[1];
		transforms = new AffineTransform[1];

		for (int i = 0 ; i < 1 ; ++i) {
			mouseTransform[i] = new Point2D.Double();
			transforms[i] = new AffineTransform();
		}
		MouseMotionListener hungryRat = new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				
				// grab mouse points
				mousePoint.setLocation(e.getX(), e.getY());

				// transform the mouse points
				for (int i = 0; i < 1; ++i)
					try {
						mouseTransform[i] = transforms[i].inverseTransform(mousePoint, null);
					} catch (NoninvertibleTransformException e1) {
						e1.printStackTrace();
					}

				// if the mouse is in the lower part of the crane
				if (c.body.contains(e.getX(), e.getY())
						|| c.tracks.contains(e.getX(),
								e.getY())
						|| c.fulcrum.contains(e.getX(),
								e.getY())) {

					// move the crane horizontally
//					c.translateBody(e.getX(), 1);
//					
//					c.translateBody(e.getX(), 0);
					//System.out.println("shift1 " + c.shift);
					c.shift = e.getX() - c.shift;
				//	System.out.println("shift2 " + c.shift);
//					c.body.translate((int) c.shift, 0);
//					c.tracks.translate((int) c.shift, 0);
//					c.fulcrum.translate((int) c.shift, 0);
					c.shift = e.getX();
			//		System.out.println("shift3 " + c.shift);
					System.out.println("body dims "+ c.body.getBounds());

					//System.out.println("Inside body " + e.getX() + " " + e.getY() + " -- " + c.body.getBounds());
					
				} else {
					c.craneMove = false;
				}

				repaint();
			}

			public void mouseMoved(MouseEvent e) {

			}
		};

		MouseListener fullRat = new MouseListener() {

			public void mousePressed(MouseEvent e) {
//				c.translateBody(e.getX(), 0);
				c.shift = e.getX();
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
//		mouseTransform = new ArrayList <Point2D.Double>();
	
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
		c.updateFrameDims(getWidth(), getHeight());
		c.drawMe(g);
	}	
} 