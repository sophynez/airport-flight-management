import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;


////////////////////////////////////////////////////////////////////////////////-----------Fenetre Menu Medecin------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Start extends JFrame {
	

	private static final long serialVersionUID = 1L;
	private java.util.Timer chrono2 = new java.util.Timer();

	//protected static final String ID_Med = null;
	
	Connection cnx=null;
	PreparedStatement prepared = null;
	ResultSet resultat =null; 
	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
    

	private JPanel contentPane;

	/**
	 * Launch the application.
	 * @throws UnsupportedLookAndFeelException 
	 */
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		FlatLightLaf.install();	
		UIManager.setLookAndFeel(new FlatLightLaf() );
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start frame = new Start();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	public Start() {
		//setIconImage(Toolkit.getDefaultToolkit().getImage(Start.class.getResource("/Menu_img/artificial-intelligence.png")));
		//cnx

		setUndecorated(true);	
		setResizable(false);

		setTitle("About ?");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 500);
		setShape(new RoundRectangle2D.Double(0d, 0d, 800d, 500d, 25d, 25d));
		setLocationRelativeTo(null);
		//vu que la frame est Undecorated on a besoin de ces MouseListeners pour la faire bouger(frame)
		  addMouseListener(new MouseAdapter() {
	            @Override
	            //on recupere les coordonnées de la souris
	            public void mousePressed(MouseEvent e) {
	                posX = e.getX();    //Position X de la souris au clic
	                posY = e.getY();    //Position Y de la souris au clic
	            }
	        });
	        addMouseMotionListener(new MouseMotionAdapter() {
	            // A chaque deplacement on recalcul le positionnement de la fenetre
	            @Override
	            public void mouseDragged(MouseEvent e) {
	                int depX = e.getX() - posX;
	                int depY = e.getY() - posY;
	                setLocation(getX()+depX, getY()+depY);
	            }
	        });      
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel BGModels = new JLabel("");
		BGModels.setIcon(new ImageIcon(Start.class.getResource("/Start/riented Flights (1).gif")));
		
		chrono2 =new java.util.Timer();
		chrono2.schedule(new TimerTask() {
			@Override
			public void run() {
				chrono2.cancel();
				chrono2.purge();
				Menu frame = new Menu();
				frame.setLocationRelativeTo(contentPane);
				frame.setVisible(true);
				dispose();
			}
		}, 7000);
		
		JLabel Docanimation1_1 = new JLabel("");
		Docanimation1_1.setIcon(new ImageIcon(Menu.class.getResource("/Start/wheel.gif")));//animation de base
		Docanimation1_1.setBounds(343, 304, 150, 150);
		contentPane.add(Docanimation1_1);
		
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		BGModels.setBounds(0, 0, 800, 500);
		contentPane.add(BGModels);
		
	}
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
}
