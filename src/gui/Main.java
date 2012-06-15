package gui;
/**
 * Classe principale
 * On va lancer une fenetre de type MainFrame
 * 
 * @author reivax
 */
import javax.swing.SwingUtilities;

public class Main {
    private Main() {
    }
    
    public static void main(String[] args) {
        if(args.length > 0 && args[0].compareToIgnoreCase("noPertelian") == 0){
        	Pertelian.setActivation(false);
        }
    	SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
