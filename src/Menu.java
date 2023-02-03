import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.TimerTask;
import java.util.concurrent.BrokenBarrierException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Menu------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Menu extends JFrame {
	

	private static final long serialVersionUID = 1L;
	private java.util.Timer chrono2 = new java.util.Timer();

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
					Menu frame = new Menu();
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
	public Menu() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/Menu/airplane.png")));
			
		setResizable(false);
		//setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/Menu_img/medical-care.png")));
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(0, 0, 1000, 659);
		setLocationRelativeTo(null);
	    contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel Docanimation1 = new JLabel("");
		Docanimation1.setIcon(new ImageIcon(Menu.class.getResource("/Menu/Flying around the world ENT.gif")));//animation de base
		chrono2 =new java.util.Timer();
		chrono2.schedule(new TimerTask() {
			@Override
			public void run() {
				Docanimation1.setIcon(new ImageIcon(Menu.class.getResource("/Menu/Flying around the world.gif")));//animation looping
			}
		}, 1500);
		Docanimation1.setBounds(328, 124, 345, 345);
		contentPane.add(Docanimation1);
		JLabel BGModels = new JLabel("");
		BGModels.setIcon(new ImageIcon(Start.class.getResource("/Menu/BG1.png")));
		
		
		JButton btnLeft = new JButton("");
		btnLeft.setToolTipText("Monitoring");
		btnLeft.setIcon(new ImageIcon(Menu.class.getResource("/Menu/radar.png")));
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				chrono2.cancel();
				chrono2.purge();  
				
				
				Thread t1 = new Thread(){
				    public void run(){
				    	String[] arguments = new String[] {""};
						try {
							MainFrame.main(arguments);
							//new MainFrame();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedLookAndFeelException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }};
				t1.start();
				dispose();
			
			}
		});
		btnLeft.setBounds(10, 259, 128, 128);
		btnLeft.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGModels.setIcon(new ImageIcon(Menu.class.getResource("/Menu/BG2.png")));
				ImageIcon animationbtn5 =new ImageIcon(Menu.class.getResource("/Menu/Aircraft 2.gif")); 
				animationbtn5.getImage().flush(); 
				Docanimation1.setIcon(animationbtn5);
				chrono2.cancel();
				chrono2.purge();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGModels.setIcon(new ImageIcon(Menu.class.getResource("/Menu/BG1.png")));
				ImageIcon animationBG =new ImageIcon(Menu.class.getResource("/Menu/Flying around the world ENT.gif"));	//animation de base
				chrono2 =new java.util.Timer();
				chrono2.schedule(new TimerTask() {
					@Override
					public void run() {
						Docanimation1.setIcon(new ImageIcon(Menu.class.getResource("/Menu/Flying around the world.gif")));//animation looping
					}
				}, 1500);
				animationBG.getImage().flush(); // réinitialiser l'animation du menu de base
				Docanimation1.setIcon(animationBG);
			}
		});
		btnLeft.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnLeft);
		ButtonStyle(btnLeft);
		
		JButton btnRight = new JButton("");
		btnRight.setToolTipText("Gestion de Planning");
		btnRight.setIcon(new ImageIcon(Menu.class.getResource("/Menu/airplane.png")));
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRight.setBounds(846, 259, 128, 128);
		btnRight.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				BGModels.setIcon(new ImageIcon(Menu.class.getResource("/Menu/BG3.png")));
				ImageIcon animationbtn5 =new ImageIcon(Menu.class.getResource("/Menu/Aircraft 3.gif")); 
				animationbtn5.getImage().flush(); 
				Docanimation1.setIcon(animationbtn5);
				chrono2.cancel();
				chrono2.purge();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				BGModels.setIcon(new ImageIcon(Menu.class.getResource("/Menu/BG1.png")));
				ImageIcon animationBG =new ImageIcon(Menu.class.getResource("/Menu/Flying around the world ENT.gif"));	//animation de base
				chrono2 =new java.util.Timer();
				chrono2.schedule(new TimerTask() {
					@Override
					public void run() {
						Docanimation1.setIcon(new ImageIcon(Menu.class.getResource("/Menu/Flying around the world.gif")));//animation looping
					}
				}, 1500);
				animationBG.getImage().flush(); // réinitialiser l'animation du menu de base
				Docanimation1.setIcon(animationBG);
			}
		});
		btnRight.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(btnRight);
		ButtonStyle(btnRight);


		
		
		
		BGModels.setBounds(-9, 0, 1000, 620);
		contentPane.add(BGModels);
		
	}
//methode du style des buttons/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 private void ButtonStyle(JButton btn) {
	//enlecer les bordures des btn
	 btn.setOpaque(false);
	 btn.setFocusPainted(false);
	 btn.setBorderPainted(false);
	 btn.setContentAreaFilled(false);
	
}
}
