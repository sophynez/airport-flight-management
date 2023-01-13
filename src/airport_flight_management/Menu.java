package airport_flight_management;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatLightLaf;

////////////////////////////////////////////////////////////////////////////////-----------Fenetre Menu------------///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Menu extends JFrame {
	

	private static final long serialVersionUID = 1L;
	private JTextField textFieldPath;
	private JTextField textFieldNBR;
	private JFileChooser fileChooser;
	private JTextField textFieldD;
	private String File_Selected="C:/Users/name/Desktop/...";

	
	private int posX = 0;   //Position X de la souris au clic
    private int posY = 0;   //Position Y de la souris au clic
    private JScrollPane scrollpaneIMG; 

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
		setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/Menu_img/artificial-intelligence.png")));
		
		setUndecorated(true);	
		setResizable(false);
		//setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/Menu_img/medical-care.png")));
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 550);
		setShape(new RoundRectangle2D.Double(0d, 0d, 800d, 550d, 25d, 25d));
		setLocationRelativeTo(null);
		//vu que la frame est Undecorated on a besoin de ces MouseListeners pour la faire bouger(frame)
		  addMouseListener(new MouseAdapter() {
	            @Override
	            //on recupere les coordonn�es de la souris
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
			
			

			textFieldPath = new JTextField();
			textFieldPath.setEditable(false);
			textFieldPath.setToolTipText("Path to save train & test files");
			textFieldPath.setText("C:/Users/name/Desktop/...");
			textFieldPath.setFont(new Font("Microsoft PhagsPa",Font.CENTER_BASELINE,12));
			textFieldPath.setForeground(Color.GRAY);
			textFieldPath.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (textFieldPath.getText().toString().equals("C:/Users/name/Desktop/...") && textFieldPath.isEditable()) {
						textFieldPath.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
						textFieldPath.setForeground(Color.LIGHT_GRAY);
						textFieldPath.setText("");
					}
				}
				@Override
				public void focusLost(FocusEvent e) {
					if (textFieldPath.getText().toString().equals("")) {
						textFieldPath.setFont(new Font("Microsoft PhagsPa",Font.CENTER_BASELINE,12));
						textFieldPath.setForeground(Color.GRAY);
						textFieldPath.setText("C:/Users/name/Desktop/...");
					}
				}
			});
			textFieldPath.setColumns(10);
			textFieldPath.setBounds(141, 83, 285, 34);
			contentPane.add(textFieldPath);
			
			scrollpaneIMG = new JScrollPane();
			scrollpaneIMG.setBounds(50, 188,  548, 311);
			scrollpaneIMG.setBorder(BorderFactory.createEmptyBorder());
			scrollpaneIMG.getViewport().setOpaque(false);
			contentPane.add(scrollpaneIMG);
			
			JLabel ModelsIMG = new JLabel("");
			scrollpaneIMG.setViewportView(ModelsIMG);
			
			JButton btnchooser = new JButton("");
			btnchooser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnchooser.setIcon(new ImageIcon(Menu.class.getResource("/Datasets_img/folder.png")));
			btnchooser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fileChooser = new JFileChooser(); 
					fileChooser.setDialogTitle("Choose Directory");
					fileChooser.setCurrentDirectory(new File("C:/Users/LATITUDE/Desktop/DATASET TOP/"));
			        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			 
			        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png"));
			 
		            int option = fileChooser.showOpenDialog(btnchooser);
		            if(option == JFileChooser.APPROVE_OPTION){
		            	File_Selected = fileChooser.getSelectedFile().getAbsolutePath();
		            	textFieldPath.setText(File_Selected);
		            	textFieldPath.setForeground(Color.LIGHT_GRAY);
		            	ModelsIMG.setIcon(new ImageIcon(File_Selected));
		        		textFieldNBR.setEnabled(true);
		        		textFieldNBR.setEditable(true);
		        		textFieldD.setEnabled(true);
		        		textFieldD.setEditable(true);
		            }
		            else{
		            	File_Selected= "C:/Users/name/Desktop/...";
		            	ModelsIMG.setIcon(new ImageIcon());
		            }
				}
			});
			btnchooser.setToolTipText("Path chooser");
			btnchooser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnchooser.setBounds(436, 83, 34, 34);
			contentPane.add(btnchooser);
		
			   
		JLabel BGMenu = new JLabel("");
		BGMenu.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/BG-1.png")));	// Back ground de base	
       
// Bouton Reduire ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton Minimise_BTN = new JButton("");
		Minimise_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/Minimize ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Minimise_BTN.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/Minimize ML .png")));
			}
		});
		Minimise_BTN.setToolTipText("Minimize");
		ButtonStyle(Minimise_BTN);
		Minimise_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(ICONIFIED);
			}
		});
		Minimise_BTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Minimise_BTN.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/Minimize ML .png")));
		Minimise_BTN.setBounds(672, 5, 32, 32);
		contentPane.add(Minimise_BTN);
// Exit bouton//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		JButton Exit_BTN = new JButton("");
		Exit_BTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/Exit ML selected.png")));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Exit_BTN.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/Exit ML.png")));
			}
		});
		Exit_BTN.setToolTipText("Exit");
		ButtonStyle(Exit_BTN);
		Exit_BTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					int ClickedButton	=JOptionPane.showConfirmDialog(null, "Do you really want to leave?", "Close", JOptionPane.YES_NO_OPTION);
					if(ClickedButton==JOptionPane.YES_OPTION)
					{					
						dispose();
					}
			}
			
		});
		Exit_BTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Exit_BTN.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/Exit ML.png")));
		Exit_BTN.setBounds(756, 5, 32, 32);
		contentPane.add(Exit_BTN);
//Boutton home//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JButton btnHome = new JButton("");
		btnHome.setEnabled(false);
		btnHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (btnHome.isEnabled()) {
					btnHome.setIcon(new ImageIcon(Menu.class.getResource("/Models_img/home selected.png")));//changer les couleurs button
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (btnHome.isEnabled()) {
					btnHome.setIcon(new ImageIcon(Menu.class.getResource("/Models_img/home.png")));//remetre le bouton de base
				}
			}
		});
		btnHome.setIcon(new ImageIcon(Menu.class.getResource("/Models_img/home.png")));
		btnHome.setToolTipText("Menu");
		btnHome.setBounds(714, 5, 32, 32);
		ButtonStyle(btnHome);
		contentPane.add(btnHome);
		
		
		textFieldNBR = new JTextField();
		textFieldNBR.setEnabled(false);
		textFieldNBR.setEditable(false);
		textFieldNBR.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) {
		     
		      char c = e.getKeyChar();
		      if (!((c >= '0') && (c <= '9') ||
		         (c == KeyEvent.VK_BACK_SPACE) ||
		         (c == KeyEvent.VK_DELETE))) {
		        getToolkit().beep();
		        e.consume();
		      }
		    }
		  });
		textFieldNBR.setToolTipText("Precision");
		textFieldNBR.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textFieldNBR.getText().toString().equals("1 , 2 ...")) {
					textFieldNBR.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
					textFieldNBR.setForeground(Color.LIGHT_GRAY);
					textFieldNBR.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (textFieldNBR.getText().toString().equals("")) {
					textFieldNBR.setFont(new Font("Microsoft PhagsPa",Font.CENTER_BASELINE,12));
					textFieldNBR.setForeground(Color.GRAY);
					textFieldNBR.setText("1 , 2 ...");
				}
			}
		});
		textFieldNBR.setText("1 , 2 ...");
		textFieldNBR.setForeground(Color.GRAY);
		textFieldNBR.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 12));
		textFieldNBR.setColumns(10);
		textFieldNBR.setBounds(601, 103, 145, 32);
		contentPane.add(textFieldNBR);
		
		textFieldD = new JTextField();
		textFieldD.setEnabled(false);
		textFieldD.setEditable(false);
		textFieldD.setToolTipText("distance");
		textFieldD.setText("1 , 2 ...");
		textFieldD.setForeground(Color.GRAY);
		textFieldD.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 12));
		textFieldD.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) {
		      char c = e.getKeyChar();
		    	 
		      if (!((c >= '0') && (c <= '9') ||
		    	(c == '.')||	  
		         (c == KeyEvent.VK_BACK_SPACE) ||
		         (c == KeyEvent.VK_DELETE))) {
		        getToolkit().beep();
		        e.consume();
		      }
		    }
		  });
		textFieldD.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textFieldD.getText().toString().equals("1 , 2 ...")) {
					textFieldD.setFont(new Font("Microsoft PhagsPa", Font.BOLD, 16));
					textFieldD.setForeground(Color.LIGHT_GRAY);
					textFieldD.setText("");
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (textFieldD.getText().toString().equals("")) {
					textFieldD.setFont(new Font("Microsoft PhagsPa",Font.CENTER_BASELINE,12));
					textFieldD.setForeground(Color.GRAY);
					textFieldD.setText("1 , 2 ...");
				}
			}
		});
		textFieldD.setColumns(10);
		textFieldD.setBounds(601, 59, 145, 32);
		contentPane.add(textFieldD);
		
		JButton Search = new JButton("");
		Search.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Search.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/search 1.png")));
		Search.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Search.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/search 2.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Search.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/search 1.png")));

			}
		});
		Search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!File_Selected.equals("C:/Users/name/Desktop/..."))
				{
					
					String Path =textFieldPath.getText();
					String d =textFieldD.getText();
					String nbr =textFieldNBR.getText();
					String path_script ="";
		                path_script = Menu.class.getResource("/scripts_py/search_car.py").getPath();
		                path_script = path_script.substring(1, path_script.length());
		                ProcessBuilder pb = null;
					  pb = new ProcessBuilder("python",path_script,"--path_image",Path ,"--distance",d,"--nbr",nbr ).inheritIO();
                      @SuppressWarnings("unused")
						Process process = null;
						try {
							process = pb.start();
							try {
								 process.waitFor();
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						
							String test = "";
							File file = new File("C:/users/"+System.getProperty("user.name")+"/Desktop/output.txt");
						    Scanner sc;
							try {
								sc = new Scanner(file);
								while (sc.hasNextLine())
									test = sc.nextLine();
								sc.close();
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							if (test.equals("VOITURE TROUVEE ! ")) {
								JOptionPane.showMessageDialog(null,  "Acc�s non autoris�.","state" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(Menu.class.getResource("/state/error.png")));	
							}
							else {
								/*if (test.equals("VOITURE NON TROUVEE ! ")) {
									JOptionPane.showMessageDialog(null,  "Acc�s autoris�.","state" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(Menu.class.getResource("/state/stamp.png")));	
									Step2 frame = new Step2(Path);
									frame.setLocationRelativeTo(contentPane);
									frame.setVisible(true);
								}
								else {
									JOptionPane.showMessageDialog(null,  "Acc�s autoris�(aucun vehicule black list�e).","state" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(Menu.class.getResource("/state/stamp.png")));	
									Step2 frame = new Step2(Path);
									frame.setLocationRelativeTo(contentPane);
									frame.setVisible(true);
								}*/
							}	    
				}
				else {
					JOptionPane.showMessageDialog(null,  "Veuillez choisir une image d'un vehicule.","state" ,JOptionPane.PLAIN_MESSAGE, new ImageIcon(Menu.class.getResource("/state/error.png")));	

				}
			}
		});
		Search.setToolTipText("la voiture est elle black list\u00E9e");
		Search.setBounds(681, 326, 64, 64);
		contentPane.add(Search);
		ButtonStyle(Search);
		
		JButton Search_1 = new JButton("");
		Search_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Search_1.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/license 1.png")));
		Search_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "C:/Users/LATITUDE/Desktop/Bureau/dev/CV_PROJECT/bin/scripts_py/DATASET MAT/");
				} catch (IOException ex) {
		             Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
		Search_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Search_1.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/license 2.png")));

			}
			@Override
			public void mouseExited(MouseEvent e) {
				Search_1.setIcon(new ImageIcon(Menu.class.getResource("/Menu_img/license 1.png")));

			}
		});
		Search_1.setToolTipText("la liste des voitures black list�es");
		Search_1.setBounds(682, 235, 64, 64);
		contentPane.add(Search_1);
		ButtonStyle(Search_1);
//le background////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		BGMenu.setBounds(0, 0, 800, 550);
		contentPane.add(BGMenu);
		
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
