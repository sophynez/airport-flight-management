/* To change th choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

/**
 *
 * @author ABED-REBAI
 */
public class MainFrame extends JPanel {

	private static final long serialVersionUID = 1L;
    private final double OUTER_CIRCLE_SCALAR = 0.9;
  
    private final double MID_CIRCLE_SCALAR = 0.6;
    private final double INNER_CIRCLE_SCALAR = 0.3;
    private final double LINE_ANGLE_INCREMENT = 0.5;
    Color backgroundGreen = new Color(0, 50, 0);
    Color vibrantGreen = new Color(63, 255, 0);
    Color fadedGreen = new Color(0.6f, 0.8f, 0f, 0.3f);
    GradientPaint arcPaint;
    JPanel  Mainpanel;
    JFrame  Mainframe;
    JPanel Radarpanel;
    JFrame Radarframe;

    Graphics2D g2;
    AffineTransform defaultTransform;
    double curLineAngle = 0;
    int minLength;
    Timer tim;
    Point spinLineEndPoint;
    Point origin = new Point(0, 0);
    Line2D spinLine;
    Arc2D spinArc;
    Point centrePoint;
    Ellipse2D outsideCircle;
        
    //---------------------------
    Image Tcontrole;
    Image Airport;
    Image Background;
    Point MovingP = new Point(0, 0);
    Point Arrivé = new Point(-184, -34);
    private JTable tableAvions;
    private JTable tableStations;
    private JTable tableVols;
    // Vector to store active clients
 	static Vector<ClientHandler> ar = new Vector<>();
 	static ArrayList<Vol> annuaireVols = new ArrayList<Vol>();
 	static ArrayList<AvionInfo> annuaireAvion = new ArrayList<AvionInfo>();
 	static ArrayList<Station> annuaireStations = new ArrayList<Station>();
 	static PriorityQueue<Routing> pq = new PriorityQueue<Routing>(5, new RoutageComparator());
 	
 	// counter for clients
 	static int i = 0;
 	
 	final static float CAPACITE_ACCEUIL_MAX = 10;
 	final static float DISTANCE_MIN = 10; 
 	final static float SEUIL = 10;
 
    
    public static void main(String[] args)throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException {
    	System.out.println("IN");
    	FlatLightLaf.install();	
		UIManager.setLookAndFeel(new FlatLightLaf() );
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		Station station1 = new Station("id4", 4, 500, new Position(-180, -20, 0));
		annuaireStations.add(station1);
		Station station2 = new Station("id5", 4, 600, new Position(180,60, 0));
		annuaireStations.add(station2);
		Station station3 = new Station("id6", 4, 1000, new Position(-90, -28, 0));
		annuaireStations.add(station3);
		
		Date dateDepart = new Date();
		Date dateArrive = new Date();
		Position lieuDepart = new Position(0,0,0);
		Position lieuArrive = new Position(-180, -20,0);
		ArrayList<Station> escale = new ArrayList<Station>();
		escale.add(station1);
		
		Position lieuArrive2 = new Position(-90, -28,0);
		ArrayList<Station> escale2 = new ArrayList<Station>();
		escale2.add(station3);
		
		Position lieuArrive3 = new Position(180,60,0);
		ArrayList<Station> escale3 = new ArrayList<Station>();
		escale3.add(station2);
		
		Vol V1 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA45", "Avion 1",Type.DIRECT, (float) 1, Etat.NORMAL);
		
		annuaireVols.add(V1);
		Vol V2 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive2,escale2,"CA46", "Avion 2",Type.DIRECT, (float) 1, Etat.NORMAL);
		annuaireVols.add(V2);
		Vol V3 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive3,escale3,"CA47", "Avion 3",Type.DIRECT, (float) 1, Etat.NORMAL);
		annuaireVols.add(V3);
		Vol V4 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA48", "Avion 4",Type.DIRECT, (float) 1, Etat.NORMAL);
		annuaireVols.add(V4);
		Vol V5 = new Vol(dateDepart,dateArrive,lieuDepart,lieuArrive,escale,"CA49", "Avion 5",Type.DIRECT, (float) 1, Etat.NORMAL);
		annuaireVols.add(V5);
		//annuaireAvion.add(new AvionInfo("Avion 1", State.NEW));
		//annuaireAvion.add(new AvionInfo("Avion 2", State.NEW));
		//annuaireAvion.add(new AvionInfo("Avion 3", State.NEW));
		//annuaireAvion.add(new AvionInfo("Avion 4", State.NEW));
		//annuaireAvion.add(new AvionInfo("Avion 5", State.NEW));
		//server is listening on port 1234
		ServerSocket ss = new ServerSocket(5555);
		
		Socket s;
		
		// running infinite loop for getting
		// client request
		while (true)
		{
			
			// Accept the incoming request
			s = ss.accept();
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
	    	ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

			System.out.println("New Plane : ");
			
			System.out.println("Creating a new handler for this client...");

			// Create a new handler object for handling this request.
			ClientHandler mtch = new ClientHandler(s,"avions " + i, ois, oos);

			// Create a new Thread with this object.
			Thread t = new Thread(mtch);
			
			System.out.println("Adding this client to active client list");

			// add this client to active clients list
			ar.add(mtch);

			// start the thread.
			t.start();
			// increment i for new client.
			// i is used for naming only, and can be replaced
			// by any naming scheme
			i++;

		}
		
        
		
    }

   
    public void radar() {
    	Radarpanel = new JPanel();
        setBackground(Color.black);
        createFrame();
        setVariables();
        initImages();
        createTimer();
    }
    
    //constructor
	public MainFrame() {
    	
    	Mainframe = new JFrame();
    	Mainframe.setVisible(true);
    	//Mainframe.setUndecorated(true);
    	
    	Mainframe.setResizable(false);
    	Mainframe.setIconImage(Toolkit.getDefaultToolkit().getImage(Start.class.getResource("/Menu/radar.png")));
    	Mainframe.setTitle("Monitoring");
    	Mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	Mainframe.setBounds(0, 0, 800, 620);
	//	Mainframe.setShape(new RoundRectangle2D.Double(0d, 0d, 800d, 500d, 25d, 25d));
		Mainframe.setLocation(300,80);    
	    Mainpanel = new JPanel();
		Mainframe.setContentPane(Mainpanel);
		Mainpanel.setLayout(null);
		
		JButton btnShowRadar = new JButton("");
		btnShowRadar.setToolTipText("Afficher Radar");
		btnShowRadar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btnShowRadar.setIcon(new ImageIcon(MainFrame.class.getResource("/Monitoring/radar btn selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnShowRadar.setIcon(new ImageIcon(MainFrame.class.getResource("/Monitoring/radar btn.png")));
			}
		});
		btnShowRadar.setIcon(new ImageIcon(MainFrame.class.getResource("/Monitoring/radar btn.png")));
		btnShowRadar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				radar();
				Mainframe.setLocation(0,80); 
			}
		});
		btnShowRadar.setBounds(588, 238, 128, 128);
		btnShowRadar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Mainpanel.add(btnShowRadar);
		ButtonStyle(btnShowRadar);
		
		Border empty = new EmptyBorder(0,0,0,0);
		
		
		JScrollPane scrollPaneAvion = new JScrollPane();
		scrollPaneAvion.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneAvion.setBounds(74, 34, 632, 150);
		scrollPaneAvion.setBorder(empty);
		Mainpanel.add(scrollPaneAvion);
		
				
		String[] columnsNameAvions = {"N° Ref","Position","State"};
		DefaultTableModel model = new DefaultTableModel(columnsNameAvions, 0);
		tableAvions = new JTable(model);
		tableAvions.setBorder(empty);
		tableAvions.setEnabled(false);
		
	    model.setColumnIdentifiers(columnsNameAvions);
		scrollPaneAvion.setViewportView(tableAvions);
		
		JScrollPane scrollPaneVol = new JScrollPane();
		scrollPaneVol.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneVol.setBorder(empty);
		scrollPaneVol.setBounds(74, 228, 472, 150);
		Mainpanel.add(scrollPaneVol);
		
		
		String[] columnsNameStation = {"ID",  "capacite d'Acceuil",  "capacite du Reservoir", "Position"};
		DefaultTableModel modelStation =  new DefaultTableModel(columnsNameStation, 0);
		tableStations = new JTable(modelStation);
		scrollPaneVol.setViewportView(tableStations);
		
		JScrollPane scrollPaneStation = new JScrollPane();
		scrollPaneStation.setBorder(empty);
		scrollPaneStation.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		scrollPaneStation.setBounds(74, 420, 632, 150);
		Mainpanel.add(scrollPaneStation);
		
		
		String[] columnsNamevols = {"Date de Depart", "Date de Arrive", "lieu de Depart", "lieu de Arrive","Escale",
				 "N° Vol", "N° Avion", "Type", "Vitesse",  "Etat"};
		DefaultTableModel modelvols = new DefaultTableModel(columnsNamevols, 0);
		tableVols = new JTable(modelvols);
		scrollPaneStation.setViewportView(tableVols);
		
		
		Timer tata = new Timer(40, new ActionListener() {
			
            public void actionPerformed(ActionEvent e) {
            	
            	model.setRowCount(0);
        		for (int i = 0; i < MainFrame.annuaireAvion.size(); i++) {
        			
        			Object[] objs = {MainFrame.annuaireAvion.get(i).getNumRef().toString(),
        					MainFrame.annuaireAvion.get(i).getPosition().toString(), 
        					MainFrame.annuaireAvion.get(i).getState().toString()};

        			model.addRow(objs);
        			
        		}
        		modelStation.setRowCount(0);
        		for (int i = 0; i < MainFrame.annuaireStations.size(); i++) {
        			
        			Object[] objs = {MainFrame.annuaireStations.get(i).getID().toString(),
        					MainFrame.annuaireStations.get(i).getAvailableSlots(), 
        					MainFrame.annuaireStations.get(i).getQuantiteCarburant(),
        					MainFrame.annuaireStations.get(i).getPosition().toString()};

        			modelStation.addRow(objs);
        			
        		}
        		modelvols.setRowCount(0);
        		for (int i = 0; i < MainFrame.annuaireVols.size(); i++) {
        			
        			Object[] objs = {MainFrame.annuaireVols.get(i).getDateDepart()
        					,MainFrame.annuaireVols.get(i).getDateArrive()
        					,MainFrame.annuaireVols.get(i).getLieuDepart()
        					,MainFrame.annuaireVols.get(i).getLieuArrive()
        					,MainFrame.annuaireVols.get(i).getEscale().toString()
        					,MainFrame.annuaireVols.get(i).getNumVol()
        					,MainFrame.annuaireVols.get(i).getNumAvion()
        					,MainFrame.annuaireVols.get(i).getType()
        					,MainFrame.annuaireVols.get(i).getVitesse()
        					,MainFrame.annuaireVols.get(i).getEtat()};
        			modelvols.addRow(objs);
        			
        		}
        		
            }
        });
	tata.start();
		
		JButton btnStartAirPlanes = new JButton("Lancer");
		btnStartAirPlanes.setToolTipText("Lancer les avions");
		btnStartAirPlanes.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
		btnStartAirPlanes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Thread t1 = new Thread(){
				    public void run(){
				    	String[] arguments = new String[] {"Avion 1" ,"100"};
						try {
							Client.main(arguments);
						} catch (ClassNotFoundException | IOException | InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }};
				t1.start();
				Thread t2 = new Thread(){
				    public void run(){
				    	String[] arguments = new String[] {"Avion 2","200"};
						try {
							Client.main(arguments);
						} catch (ClassNotFoundException | IOException | InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }};
				t2.start();
				Thread t3 = new Thread(){
				    public void run(){
				    	String[] arguments = new String[] {"Avion 3","150"};
						try {
							Client.main(arguments);
						} catch (ClassNotFoundException | IOException | InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }};
				t3.start();
			}
		});
		btnStartAirPlanes.setBounds(588, 388, 128, 23);
		Mainpanel.add(btnStartAirPlanes);
		
		JLabel BGModels = new JLabel("");
		BGModels.setIcon(new ImageIcon(Start.class.getResource("/Monitoring/MonitoringBG.png")));
		
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		BGModels.setBounds(-9, -30, 800, 620);
		Mainpanel.add(BGModels);
		
	}
    
    @Override
    protected void paintComponent(Graphics _g) {
        super.paintComponent(_g);
        g2 = (Graphics2D) _g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        updateVariables();
        defaultGraphics();
        drawBackground();
        populateRadar();
        updateMovingObjects();
        drawMovingObjects();
    }

    private void initImages() {
		// TODO Auto-generated method stub
    	Tcontrole = new ImageIcon(MainFrame.class.getResource("/images/satellite-dish (1).png")).getImage(); 
    	Airport = new ImageIcon(MainFrame.class.getResource("/images/hangar.png")).getImage(); 
    	Background = new ImageIcon(MainFrame.class.getResource("/images/radar-background.jpg")).getImage(); 

    }
    
    private void setVariables() {
        minLength = Math.min(Radarframe.getWidth(), Radarframe.getHeight());
        spinLineEndPoint = new Point((int) (minLength / 2 * OUTER_CIRCLE_SCALAR), 0);
        spinLine = new Line2D.Double(origin, spinLineEndPoint);
        initArc();
    }

    private void createTimer() {
        tim = new Timer(40, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateSpinLine();
                repaint();
            }
        });
        tim.start();
    }

   
    private void updateVariables() {
        minLength = Math.min(Radarframe.getWidth(), Radarframe.getHeight());
        centrePoint = new Point(Radarframe.getHeight() / 2, Radarframe.getWidth() / 2);
        origin = new Point(0, 0);
    }

   
    private void updateSpinLine() {
        updateLineEndPoint();
        spinLine = new Line2D.Double(origin, spinLineEndPoint);
    }

    
    private void drawSpinLine() {
        g2.setColor(Color.white);
        g2.draw(spinLine);
    }

    private void updateMovingObjects() {
        updateSpinLine();
        MovingPoint();
    }
    
    private void MovingPoint() 
    {
    	
    	for (int i = 0; i < MainFrame.annuaireAvion.size(); i++) {
    		for (int j = 0; j < MainFrame.annuaireVols.size(); j++) {
				if(MainFrame.annuaireVols.get(j).getNumAvion().equals(MainFrame.annuaireAvion.get(i).getNumRef())) {
					// On met a jours l'etat du vol dansl'annuaire vols
					Etat T = MainFrame.annuaireVols.get(j).getEtat();
					
					switch (T) {
					case NORMAL:
						g2.setColor(Color.GREEN);
						break;
					case MANQUE_CARBURANT:
						g2.setColor(Color.ORANGE);
						break;
					case FIN_CARBURANT:
						g2.setColor(Color.RED);
						break;
					default :
						break;
					}
		    	     g2.fill(new Ellipse2D.Double(MainFrame.annuaireAvion.get(i).getPosition().getX(),MainFrame.annuaireAvion.get(i).getPosition().getY(), 10, 10));

				}
			}
    		
  
		}


    }
    
    private void drawMovingObjects() {
        drawSpinLine();
        drawArc();
    }

    private void populateRadar() {
        drawImages();
    }
    
    private void drawImages() {
   	 g2.drawImage(Tcontrole, -16, -16, this);
   	 
   	 for (int i = 0; i < annuaireStations.size(); i++) {
   		g2.drawImage(Airport,(int) annuaireStations.get(i).getPosition().getX()-16,(int)  annuaireStations.get(i).getPosition().getY()-16, this);
   	 }
   	 
     g2.setTransform(defaultTransform);
   }
   
    private void updateLineEndPoint() {
        double xVal = 0 + minLength / 2 * Math.cos(Math.toRadians(curLineAngle) + Math.toRadians(LINE_ANGLE_INCREMENT));
        double yVal = 0 + minLength / 2 * Math.sin(Math.toRadians(curLineAngle) + Math.toRadians(LINE_ANGLE_INCREMENT));
        spinLineEndPoint = new Point((int) (xVal * OUTER_CIRCLE_SCALAR), (int) (yVal * OUTER_CIRCLE_SCALAR));
        if (curLineAngle>=360) {
        	curLineAngle = 0;
		}
        else {
        	curLineAngle = curLineAngle + LINE_ANGLE_INCREMENT;
        }
    }

    private void drawBackground() {
        // Creates the circles to be used as the background, and some other tasks - the outsideCircle is used to check if points are on the radar.
        Shape mainCircle = new Ellipse2D.Double(0, 0, minLength * OUTER_CIRCLE_SCALAR, minLength * OUTER_CIRCLE_SCALAR); //should be filled to create main radar BG
        Shape outerCircle = mainCircle;  // do this drawn, or simply redraw the mainCircle object
        outsideCircle = new Ellipse2D.Double(- (Radarframe.getWidth()/2) * OUTER_CIRCLE_SCALAR, - (Radarframe.getHeight()/2) * OUTER_CIRCLE_SCALAR, minLength * OUTER_CIRCLE_SCALAR, minLength * OUTER_CIRCLE_SCALAR);
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

    
    private void drawArc() {
        AffineTransform arcTransform = new AffineTransform();
        arcTransform.translate(centrePoint.x, centrePoint.y);
        arcTransform.rotate(Math.toRadians(curLineAngle));
        g2.setTransform(arcTransform);
        g2.setPaint(arcPaint);
        g2.fill(spinArc);
        g2.setTransform(defaultTransform);
    }


    private void initArc() {
        spinArc = new Arc2D.Double();
        spinArc.setArcByCenter(0, 0, minLength / 2 * OUTER_CIRCLE_SCALAR, 0, 30, Arc2D.PIE);
        arcPaint = new GradientPaint(spinArc.getStartPoint(), vibrantGreen, spinArc.getEndPoint(), new Color(0f, 0f, 0f, 0f));
    }

 
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

    
    private void defaultGraphics() {
        g2.translate(Radarframe.getWidth() / 2, Radarframe.getHeight() / 2);
        defaultTransform = g2.getTransform();
    }

   
    private void createFrame() {
    	Radarframe = new JFrame("Radar");
    	Radarframe.setIconImage(Toolkit.getDefaultToolkit().getImage(Start.class.getResource("/Menu/radar.png")));
    	Radarframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	Radarframe.setSize(700, 700);
       // Radarframe.setLocationRelativeTo(null);
    	Radarframe.setLocation(700,5);
        Radarframe.setEnabled(true);
        Radarframe.getContentPane().add(this);
        Radarframe.setResizable(false);
        Radarframe.setVisible(true);
        Radarframe.setDefaultCloseOperation(Radarframe.HIDE_ON_CLOSE);
    }
    
    private void ButtonStyle(JButton btn) {
    	//enlecer les bordures des btn
    	 btn.setOpaque(false);
    	 btn.setFocusPainted(false);
    	 btn.setBorderPainted(false);
    	 btn.setContentAreaFilled(false);
    	
    }
}