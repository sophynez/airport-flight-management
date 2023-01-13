package airport_flight_management;
/* To change th choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.PriorityQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author ABED-REBAI
 */
public class radar extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * The scalar value for the outer circle.  The minLength will be multiplied by this when the outer
     * circle is drawn.  Is also used to determine the length of the leading line, as well as the size of
     * the arc's radius.
     */
    private final double OUTER_CIRCLE_SCALAR = 0.9;
    // scalar value for middle circle
    private final double MID_CIRCLE_SCALAR = 0.6;
    // scalar value for inner circle
    private final double INNER_CIRCLE_SCALAR = 0.3;
    // The angle by which the line and arc are rotated each time step (in degrees)
    private final double LINE_ANGLE_INCREMENT = 0.5;
    Color backgroundGreen = new Color(0, 50, 0);
    Color vibrantGreen = new Color(63, 255, 0);
    Color fadedGreen = new Color(0.6f, 0.8f, 0f, 0.3f);
    GradientPaint arcPaint;
    JPanel panel;
    JFrame frame;
    Graphics2D g2;
    AffineTransform defaultTransform;
    double curLineAngle = 0;
    //smallest value of the frame's x and y lengths
    int minLength;
    Timer tim;
    Point spinLineEndPoint;
    Point origin = new Point(0, 0);
    Line2D spinLine;
    Arc2D spinArc;
    Point centrePoint;
    Ellipse2D outsideCircle;
    int noDots = 20;
    Point2D[] dotArray = new Point2D[noDots];
    ArrayList<Shape> detectedShapes = new ArrayList<Shape>();
    boolean firstRun = true;
    
    static //---------------------------
    Position positionAvionCourant = new Position();
    
    //---------------------------
    Image Tcontrole;
    Image Airport;
    Image Background;
    Point MovingP = new Point(0, 0);
    Point Arrive = new Point(-184, -34);
 
    
    
    public static void main(String[] args)throws IOException, ClassNotFoundException {
        new radar();
        
		final float CAPACITE_ACCEUIL_MAX = 10;
		final float DISTANCE_MIN = 10; 
		final float SEUIL = 10;
		
		ArrayList<Vol> annuaireVols = new ArrayList<Vol>();
		ArrayList<AvionInfo> annuaireAvion = new ArrayList<AvionInfo>();
		ArrayList<Station> annuaireStations = new ArrayList<Station>();
		
		
		
		Date dateDepart = new Date();
		Date dateArrive = new Date();
		Position lieuDepart = new Position(0,0,0);
		Position lieuArrive = new Position(-184, -34,0);
		ArrayList<Station> escale = new ArrayList<Station>();
		
		Vol V = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA45", "ki7854",Type.DIRECT, (float) 1, Etat.NORMAL);
		annuaireVols.add(V);
		
		PriorityQueue<Routing> pq = new PriorityQueue<Routing>(5, new RoutageComparator());
		
		System.out.println("Server init");
		
		//annuaireStations.add(new Station("id1", 4, 0, new Position(1, 1, 0)));
		//annuaireStations.add(new Station("id2", 4, 0, new Position(10, 1, 0)));
		//annuaireStations.add(new Station("id3", 10, 1000, new Position(1, 10, 0)));
		annuaireStations.add(new Station("id4", 4, 100, new Position(-80, -20, 0)));
		annuaireAvion.add(new AvionInfo("ki7854"));
		annuaireAvion.add(new AvionInfo(new Position(5.0f, 5.0f,0.0f), "dummy"));
		
		//annuaireVols.add(new)
		
		 		
		// mise en ecoute + acceptation requete
    	ServerSocket ss = new ServerSocket(5555); 
    	Socket s = ss.accept();
    	ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
    	ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		
		
        while(true) {
        	
        	// reception du numero de reference
        	System.out.println("Getting avion ref");
        	String numRef = "";
        	numRef = (String) ois.readObject();

        	
	        //réception de la position de l'avion connecté
        	System.out.println("Getting avion pos");
	        positionAvionCourant = (Position) ois.readObject();
			System.out.println(positionAvionCourant);
			
			for (int i = 0 ; i<annuaireAvion.size() ; i++) {
				AvionInfo avionInfo = annuaireAvion.get(i);
				if (avionInfo.getNumRef().equals(numRef)) {
					annuaireAvion.get(i).setPosition(positionAvionCourant);
					break;
				}
			}
			
			
			
			System.out.println("Getting avion carburant");
			float reservoir;
			reservoir = (float) ois.readObject();
			
			Etat etat;
			// on suppose que l'avion a assez de carburant et n'est pas en risque de collision
			etat = Etat.NORMAL;
			
			
			//Detection Collision
			for (int i = 0; i<=annuaireAvion.size(); i++) {
				Position positionOtherAvions = annuaireAvion.get(i).getPosition();
				String NumRefOtherAvion = annuaireAvion.get(i).getNumRef();
				// on ne cherche les collision que  pour les avions qui sont a la même altitude
				if (positionOtherAvions.getZ() == positionAvionCourant.getZ()){
					float dis = positionAvionCourant.calculerDistance(positionOtherAvions.getX(), positionOtherAvions.getY());
					if (dis <= DISTANCE_MIN) {
						positionAvionCourant.setZ((positionAvionCourant.getZ() % 5) +1);
						break;
					}
				}
			}
			
			//renvoie de l'altitude vers l'avion pour la mise ajours et evitement de la collision
			System.out.println("Sending Z to Avion");
			oos.writeObject(positionAvionCourant.getZ());
			
			if (reservoir < SEUIL) {
				/*
				 * si la quantité de carburant est inférieur à un seuil, 
				 * on considère qu'il lui manque du carburant 
				 */
				etat = Etat.MANQUE_CARBURANT;
			}
			
			for (int i = 0; i < annuaireVols.size(); i++) {
				if(annuaireVols.get(i).getNumAvion().equals(numRef)) {
					// On met a jours l'etat du vol dansl'annuaire
					annuaireVols.get(i).setEtat(etat);
				}
			}
			
			//send etat to avion
			System.out.println("sending new etat to avion");
			oos.writeObject(etat);
			
			System.out.println(annuaireVols.get(0).getEtat());
			//Cas ou carburant insuffisant ---> routage
			if(Etat.MANQUE_CARBURANT ==  etat) {
				float min = 10000000;
				int statIndex = -1;
				System.out.println("deteminer station");
				
				//Mettre toutes les station dans la PQ				
				for (int i = 0; i <annuaireStations.size(); i++) {
					//on prend la position de la station i
					Position stats = annuaireStations.get(i).getPosition();
					//on calcule la distence entre l'avion et la station
					float dis = positionAvionCourant.calculerDistanceManhattan(stats.getX(), stats.getY());
					//on sauvegarde ça dans une pile a priorité en fonction de la distence
					pq.add(new Routing(annuaireStations.get(i), dis));
				}
				//Used to keep position of the closest station with empty space regardless of fioul
				boolean  closestFreeStation  = false;
				//if true alors l'avion va crash anyways
				boolean crash = false;
				//used to tell that we found a good station
				boolean reFiouled = false;
				Position positionUrgence = new Position();
				Position positionRoutage = new Position();
				
				while(!pq.isEmpty()){
					Routing routage = pq.poll();
					Station stationRoutage = routage.getStation();
					float distanceRoutage = routage.getDistence();
					
					if (reservoir < distanceRoutage) {
						crash = true;
						break;
					}
					
					System.out.println(stationRoutage.getAvailableSlots());
					
					if (stationRoutage.getAvailableSlots() < CAPACITE_ACCEUIL_MAX && !closestFreeStation ){
						/*
						 * on save la position de la station la plus proche 
						 * ou l'avion peut atterrir
						 */
						positionUrgence = stationRoutage.getPosition();
						closestFreeStation = true;
					}
					if (stationRoutage.getQuantiteCarburant() > (300 - reservoir) 
							&& stationRoutage.getAvailableSlots() < CAPACITE_ACCEUIL_MAX) {
						/*
						 * on récupère la position de la statio la plus proche avec assez de place 
						 * ET de carburant
						 */
						System.out.println("sending new etat to avion");
						etat = Etat.MANQUE_CARBURANT;
						oos.writeObject(etat);
						positionRoutage = stationRoutage.getPosition();
						oos.writeObject(positionRoutage);
						reFiouled = true;
						break;
					}	
				}
				
				
				

				if (!reFiouled && closestFreeStation) {
					System.out.println("sending routage no fioul");
					System.out.println("sending new etat to avion");
					etat = Etat.MANQUE_CARBURANT;
					oos.writeObject(etat);
					oos.writeObject(positionUrgence);
				}else {
					System.out.println("u ded no station to save u");
					System.out.println("sending new etat to avion");
					etat = Etat.FIN_CARBURANT;
					oos.writeObject(etat);

					//envoie
				}
				
	
			}
			/*oos.close();
			ois.close();
			ois.close();
			ss.close();
		    s.close();*/
        }

    }

    /**
     * Basic constructor for the radar class.  Creates a new panel for use,
     * and then puts that panel into a frame.
     */
    public radar() {
        panel = new JPanel();
        setBackground(Color.black);
        createFrame();
        setVariables();
        initImages();
        createTimer();
    }

    /**
     * Called whenever the panel needs to be redrawn.  Calls various methods to update locations
     * of things on the radar and redraws the background, as well as updating some variables.
     * @param _g*/
    
    @Override
    protected void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        g2 = (Graphics2D) _g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        updateVariables();
        defaultGraphics();
        drawBackground();
        if (firstRun == true) {
            initDots();
            firstRun = false;
        }
        populateRadar();
        updateMovingObjects();
        drawMovingObjects();
        checkLineIntersects();
        updateDetected();
    }

    /**
     * Initialises some variables for use during runtime.
     * - smallest length of the width and height of the frame
     * - the end point of the spinning line
     * - the spin line
     * - the arc
     */
    
    private void initImages() {
		// TODO Auto-generated method stub
    	//Tcontrole = new ImageIcon(radar.class.getResource("/images/satellite-dish (1).png")).getImage(); 
    	//Airport = new ImageIcon(radar.class.getResource("/images/hangar.png")).getImage(); 
    	//Background = new ImageIcon(radar.class.getResource("/images/radar-background.jpg")).getImage(); 

    }
    
    private void setVariables() {
        minLength = Math.min(frame.getWidth(), frame.getHeight());
        spinLineEndPoint = new Point((int) (minLength / 2 * OUTER_CIRCLE_SCALAR), 0);
        spinLine = new Line2D.Double(origin, spinLineEndPoint);
        initArc();
    }

    /**
     * Creates a timer which is used to update the spin line, and call repaint at every time step.
     */
    private void createTimer() {
        tim = new Timer(40, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateSpinLine();
                repaint();
            }
        });
        tim.start();
    }

    /**
     * updates variables to new values based on the size of the frame.
     */
    private void updateVariables() {
        minLength = Math.min(frame.getWidth(), frame.getHeight());
        centrePoint = new Point(frame.getHeight() / 2, frame.getWidth() / 2);
        origin = new Point(0, 0);
    }

    /**
     * Updates the spinline end point to its new location, and recreates the spinline as a new object.
     */
    private void updateSpinLine() {
        updateLineEndPoint();
        spinLine = new Line2D.Double(origin, spinLineEndPoint);
    }

    /**
     * Draws the spin line
     */
    private void drawSpinLine() {
        g2.setColor(Color.white);
        g2.draw(spinLine);
    }

    /**
     * Updates positions of moving objects
     */
    private void updateMovingObjects() {
        updateSpinLine();
        MovingPoint();
    }
    
    private void MovingPoint() 
    {
    	g2.setColor(Color.red);
    	
    	
     g2.fill(new Ellipse2D.Double(positionAvionCourant.getX(), positionAvionCourant.getY(), 10, 10));


    }
    

    /**
     * Draws the moving objects on the radar.
     */
    private void drawMovingObjects() {
        drawSpinLine();
        drawArc();
    }

    /**
     * populates the radar with planes, dots and raster images.
     */
    private void populateRadar() {
        drawDots();
        drawImages();
    }
    
    private void drawImages() {
   	 g2.drawImage(Tcontrole, -16, -16, this);
   	 g2.drawImage(Airport, -200, -50, this);
   	 g2.drawImage(Airport, -96, -36, this);
     g2.setTransform(defaultTransform);
   }
   
    
    /**
     * Updates the line end point to a new value which will allow the line to be drawn
     * LINE_ANGLE_INCREMENT degrees further along the spinning path.
     */
    private void updateLineEndPoint() {
        double xVal = 0 + minLength / 2 * Math.cos(Math.toRadians(curLineAngle) + Math.toRadians(LINE_ANGLE_INCREMENT));
        double yVal = 0 + minLength / 2 * Math.sin(Math.toRadians(curLineAngle) + Math.toRadians(LINE_ANGLE_INCREMENT));
        spinLineEndPoint = new Point((int) (xVal * OUTER_CIRCLE_SCALAR), (int) (yVal * OUTER_CIRCLE_SCALAR));
        // curLineAngle = curLineAngle >= 360 ? 0 : curLineAngle + LINE_ANGLE_INCREMENT;
        if (curLineAngle>=360) {
        	curLineAngle = 0;
		}
        else {
        	curLineAngle = curLineAngle + LINE_ANGLE_INCREMENT;
        }
//        System.out.println("new end point is (" + xVal + ", " + yVal + ")");
//        System.out.println(curLineAngle);
//        System.out.println("length of line is " + Math.sqrt(Math.pow(yVal, 2) + Math.pow(xVal, 2)));
    }

    /**
     * Draws the background of the radar - the concentric circles, background circle, the triangle on the second concentric circle,
     * and lines.
     */
    private void drawBackground() {
        // Creates the circles to be used as the background, and some other tasks - the outsideCircle is used to check if points are on the radar.
        Shape mainCircle = new Ellipse2D.Double(0, 0, minLength * OUTER_CIRCLE_SCALAR, minLength * OUTER_CIRCLE_SCALAR); //should be filled to create main radar BG
        Shape outerCircle = mainCircle;  // do this drawn, or simply redraw the mainCircle object
        outsideCircle = new Ellipse2D.Double(- (frame.getWidth()/2) * OUTER_CIRCLE_SCALAR, - (frame.getHeight()/2) * OUTER_CIRCLE_SCALAR, minLength * OUTER_CIRCLE_SCALAR, minLength * OUTER_CIRCLE_SCALAR);
        Shape midCircle = new Ellipse2D.Double(0, 0, minLength * MID_CIRCLE_SCALAR, minLength * MID_CIRCLE_SCALAR);
        Shape innerCircle = new Ellipse2D.Double(0, 0, minLength * INNER_CIRCLE_SCALAR, minLength * INNER_CIRCLE_SCALAR);
        // Puts circles into an array for easier processing
        Shape[] circles = new Shape[]{outerCircle, midCircle, innerCircle};
        //g2.setColor(backgroundGreen);
        //centreCircle(mainCircle, true);
        g2.drawImage(Background, -350, -350, this);
        g2.setColor(vibrantGreen);
        for (Shape shape : circles) {
            centreCircle(shape, false);
        }
        
        

    }

    /**
     * Initialises an array of points within the bounds of the outer circle.  Won't populate very well if window is made resizable, as it uses
     * a static value as a limit for random point generation (i.e. points will be between (0,0) and (500,500).  Also,
     * outsideCircle is defined when the class is created, and will not change should the window be resized, so
     * the dots present will be concentrated in a specific area.
     */
    private void initDots() {
        Point2D randPoint;
       // g2.translate(0, 0);
        for (int i = 0; i < noDots; i++) {
            boolean pointChosen = false;
            while (!pointChosen) {
                // Generates a point with coordinates between -600 and 600.
                randPoint = new Point2D.Double((-1 + Math.random() * 2) * frame.getWidth() * OUTER_CIRCLE_SCALAR, (-1 + Math.random() * 2) * frame.getHeight() * OUTER_CIRCLE_SCALAR);
                //System.out.println(randPoint);
                //Checks if the outside circle contains the point, and if it does, adds that point to the dotArray, and starts finding a new point.
                if (outsideCircle.contains(randPoint)) {
                    dotArray[i] = randPoint;
                    pointChosen = true;
                }
            }
        }
    }

    /**
     * Fills circles from the point array.
     */
    private void drawDots() {
        g2.setColor(fadedGreen);
      //  g2.translate(0, 0);
        // Draws circles from the points in the dotArray
        for (Point2D point : dotArray) {
            g2.fill(new Ellipse2D.Double(point.getX(), point.getY(), 10, 10));
//            g2.draw(new Line2D.Double(origin, point));
        }
        g2.setTransform(defaultTransform);
    }

    /**
     * Checks if a point is less than 5 units away from the spinning line, and if it is,
     * draws a box around the point.
     */
    private void checkLineIntersects() {
        g2.setColor(vibrantGreen);
        for (Point2D point : dotArray) {
            // Checks distance from point to line is < 5
            if (spinLine.ptSegDist(point) < 5) {
                // Creates a bounding square for the circle and adds it to the detected shapes array.
                Shape boundSquare = new Rectangle2D.Double(point.getX(), point.getY(), 10, 10);
                // draws a line from the origin to the point.
                g2.draw(new Line2D.Double(origin, point));
                // Adds the bounding rectangle to the detected shapes array if it is not there already
                if (!detectedShapes.contains(boundSquare)) {
                    detectedShapes.add(boundSquare);
                }
                // Draws the bounding square
                //g2.draw(boundSquare);
            }
        }
    }

    /**
     * Updates the arraylist of detected shapes.  If the size of the arraylist is larger than a certain percentage of the
     * total number of dots on the radar, it gets rid of a percentage of these dots, giving a rudimentary radar sweep.
     */
    private void updateDetected() {
        if (detectedShapes.size() > noDots * 0.2) {
            for (int i = 0; i < noDots * 0.05; i++) {
                detectedShapes.remove(0);
            }
        }
        // Draws each shape in the detectedShapes array (these are bounding boxes for the shapes that have been detected.
        for (Shape s : detectedShapes) {
            g2.draw(s);
        }
    }


    /**
     * Draws the arc with a rotation of a certain number of degrees.  This is based on
     * how many timesteps there have been since starting the program.
     */
    private void drawArc() {
        AffineTransform arcTransform = new AffineTransform();
        arcTransform.translate(centrePoint.x, centrePoint.y);
        arcTransform.rotate(Math.toRadians(curLineAngle));
        g2.setTransform(arcTransform);
        g2.setPaint(arcPaint);
        g2.fill(spinArc);
        g2.setTransform(defaultTransform);
    }

    /**
     * Initialises the arc with some initial values.  Also makes a gradient paint with which to paint the arc.
     */
    private void initArc() {
        spinArc = new Arc2D.Double();
        spinArc.setArcByCenter(0, 0, minLength / 2 * OUTER_CIRCLE_SCALAR, 0, 30, Arc2D.PIE);
//        arcPaint = new GradientPaint((int) (minLength / 2 * 0.9), 0, vibrantGreen, (int) (minLength / 2 * 0.6), (int) (-minLength / 2 * 0.3), new Color(0f, 0f, 0f, 0f));
        arcPaint = new GradientPaint(spinArc.getStartPoint(), vibrantGreen, spinArc.getEndPoint(), new Color(0f, 0f, 0f, 0f));
    }

    /**
     *  Moves a circle shape from its standard creation position (to the right and below the origin), so that the
     * circle's centre is on the origin.
     * @param _circle The circle to centre.
     * @param _fill Whether the circle is to be filled or not.
     */
    private void centreCircle(Shape _circle, boolean _fill) {
        AffineTransform circleTransform = new AffineTransform();
        circleTransform.translate(-_circle.getBounds().height / 2, -_circle.getBounds().width / 2);
        g2.transform(circleTransform);
        if (_fill == true) {
            g2.fill(_circle);
        } else {
            g2.draw(_circle);
        }
       g2.setTransform(defaultTransform);
    }

    /**
     * Translates the origin to the centre of the frame, and sets this as a transform to be used to
     * reset the graphics if a different transform is applied.
     */
    private void defaultGraphics() {
        g2.translate(frame.getWidth() / 2, frame.getHeight() / 2);
        defaultTransform = g2.getTransform();
    }

    /**
     * Creates a frame to enclose the panel object.  Its initial location is based on the screen
     * size.
     */
    private void createFrame() {
        frame = new JFrame("Radar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}