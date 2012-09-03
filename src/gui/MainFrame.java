package gui;
/**
 * Classe MainFrame
 * Correspond à la fenêtre principale
 * Actuellement, elle est optimisée pour une taille d'écran eeeTop. Pour passer en plein écran, _
 * il suffit de passer la variable eeeTop à false (résultats non garantis ^^)
 * Actuellement, le badgage est géré en keyListener. C'est amené à changer...
 * Chaque fenêtre est composé d'un corps (content) et d'un footer. Seul le content change d'une page à l'autre.
 * Si on trouve des infos à mettre dans le footer en fonction des pages, ça peut être possible de le modifier _
 * en fonction des pages.
 * 
 * @author reivax
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.rpc.ServiceException;

//VERSION POUR LA COMPIL PAR ECLIPSE
import localhost.BuckUTT.server.PBUY_class_php.*;

//VERSION POUR LA COMPIL PAR SCRIPT
//import axis.*;







public class MainFrame extends JFrame implements KeyListener {
	/**
	 *
	 */
	public static MainFrame getInstance(){ return instance; }
	private static final long serialVersionUID = 1L;
	public static int width; //largeur de la fenetre
    public static int height; //hauteur de l'écran
    private static MainFrame instance = null;
    private boolean eeeTop = true; //true pour dimension eeeTop, false pour s'adapter direct à l'écran... utile pour voir ce que ça rendrait sur un eeeTop pendant le développement
    private static JPanel content;
    private static JPanel footer;
    private static ConnexionPanel connexionPanel = new ConnexionPanel();
    private static VentePanel ventePanel;
    private static String fenetreActive;
    public static Vendeur vendeur;
    public static PBUYPort PBUY = null;
    public static int id_point;
    public static String nom_point;
    private String codeEtu = "";
    public static String ip = "";
    public static String versionPeggy = "DEV V2.1E";
    public static String backgroundText = "BETA";
    
    /** 
     * Création de la fenêtre principale et du footer.
     */
    public MainFrame() {
    	super();
    	instance = this;
    	
        if(eeeTop) {
            setSize(1366,768);
            setResizable(false); //On interdit la redimensionnement de la fenêtre
            width = getWidth();
            height = getHeight();
        }
        else {
            setResizable(true);
            setExtendedState(MAXIMIZED_BOTH); //marche si on autorise le resizable. utile si pas tjs des eeeTop
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            width = screen.width;
            height = screen.height;
        }
        
        setTitle("Buckutt");
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
               
        content = new JPanel();
        content.setLayout(null);
        content.setBounds(0,0,width,height);
        
        add(content);
        java.net.InetAddress i = null;
		try {
			i = java.net.InetAddress.getLocalHost();
			ip = i.getHostName();
		}
		catch(Exception e){e.printStackTrace();}
		AfficheurDeporte.setBienvenue();
		

		
        addKeyListener(this);
        requestFocus();
        
        vendeur = new Vendeur(); //on crée un vendeur, qui sera ainsi commun à toutes les pages
        MainFrame.showConnexionPanel();
    }
    
    
    /**
     * Affiche la page de connexion 
     */
    public static void showConnexionPanel() {
    	
    	try {
	    	PBUYServiceLocator service = new PBUYServiceLocator();
	    	service.setMaintainSession(true);
	    	PBUY = service.getPBUYPort();
    	} catch (ServiceException e) {
    		e.printStackTrace();
    	}
    	
    	updateIdPoint();
    	
    	content.setVisible(false);
    	content.removeAll();
    	
    	connexionPanel = new ConnexionPanel();
    	JPanel contenu = connexionPanel.ShowGUI();
    	contenu.setBounds(0,0,width,height-30);
    	content.add(contenu);
    	
    	footer = new Footer(nom_point, "connexion");
    	footer.setBounds(0,height-30,width, 30);
    	content.add(footer);

    	AfficheurDeporte.setFerme();
    	
    	content.revalidate();
    	content.setVisible(true);
    	
    	fenetreActive = "connexion";
    	
	   	//performe autologin
	   	String[] autolog = getAutoLogin();
	   	if(autolog == null)
	   		return;
	   	
	   	for(char c : autolog[0].toCharArray())
	   		getInstance().loginKeyInput(c);
	   	
	   	connexionPanel.performeLogin(autolog[1]);
    }
    
    /**
     * Affiche la page de vente
     * @param idSellet id du vendeur 
     */
    public static void showVentePanel(String idSeller) {
    	content.removeAll();
    	content.setVisible(false);
    	
    	ventePanel = new VentePanel();
    	JPanel contenu = ventePanel.ShowGUI();
    	contenu.setBounds(0,0,width,height-30);
    	content.add(contenu);
    	
    	footer = new Footer(nom_point, "vente");
    	footer.setBounds(0,height-30,width, 30);
    	content.add(footer);

    	content.revalidate();
    	content.setVisible(true);

    	fenetreActive = "vente";
    	
    	AfficheurDeporte.setOuvert();
    	
		//ThreadDelog cp1 = new ThreadDelog (100);
    	//cp1.start();
    }
    
    /**
     * Met à jour le id_point
     */
    public static void updateIdPoint() {	
    	File file = new File("id_point");
		FileReader fr;
		try {
			String str;
			fr = new FileReader(file);
			str = "";
			int i = 0;
			//Lecture des données
			while((i = fr.read()) != -1)
				str += (char)i;	
			
			str = str.replaceAll("\n", "");
			if(id_point != Integer.parseInt(str)) {
				id_point = Integer.parseInt(str);
				nom_point = PBUY.getPointName(id_point);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Permet de s'auto loguer pour aller plus vite lorsqu'on test
     * Le fichier doit s'appeller auto_login 
     * être sur deux lignes sous la forme:
     * mon mol4 + clé
     * mon code
     */
    public static String[] getAutoLogin(){
    	String info[] = {"", ""};
             
        try {
			Scanner s = new Scanner(new File("auto_login"));
			String line = null;
			if((line = s.next()) != null  && line != "")
				info[0] = line;
			
			if((line = s.next()) != null && line != "")
				info[1] = line;
			
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
        return info;
    }
    
    /**
     * Pour chaque touche tapé par l'utilisateur
     * (ou entrée badgeuse)
     */
    private void loginKeyInput(char c){
    	if (Character.getNumericValue(c) != -1) {
        	codeEtu += Character.getNumericValue(c);
        }
        
        if (Character.getNumericValue(c) == -1) {       	
            if(fenetreActive == "connexion") {
            	connexionPanel.hideBoutonModeManuel();
                connexionPanel.idEtu = codeEtu;
                connexionPanel.annuleUser();
                connexionPanel.updateInfoUser(4); //mean of log 4 => trame
            }
            else if(fenetreActive == "vente") {
            	try {
					System.out.println(PBUY.isLoadedSeller());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	ventePanel.setIdEtu(codeEtu);
            	ventePanel.updateInfoUser(4); //mean of log 4 => trame
            }
            codeEtu = "";
        }
    }
    
    public void keyPressed(KeyEvent evt){ } //obligÃ© de la mettre
    public void keyReleased(KeyEvent evt){ }  //obligÃ© de la mettre
    public void keyTyped(KeyEvent ev) { //lÃ , c'est la mÃ©thode qu'on modifie pour faire genre quelqu'un a badgÃ©...  	
    	loginKeyInput(ev.getKeyChar());
    }

}
