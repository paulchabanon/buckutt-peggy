package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

//fait un dégradé tout joli

class ContentPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	ContentPanel() {
    	Color newCouleur = new Color(255, 164, 209);
    	
    	File file = new File("couleur");
		FileReader fr;
		try {
			String str;
			fr = new FileReader(file);
			str = "";
			int i = 0;
			//Lecture des données
			while((i = fr.read()) != -1)
				str += (char)i;	
			
			String[] result = str.split(",");
			setBackground(new Color(Integer.parseInt(result[0]), Integer.parseInt(result[1]), Integer.parseInt(result[2])));
			
		} catch (FileNotFoundException e) {
			setBackground(newCouleur);
			e.printStackTrace();
		} catch (IOException e) {
			setBackground(newCouleur);
			e.printStackTrace();
		}
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if ( !isOpaque() ) {
            super.paintComponent(g);
            return;
        }
        
        Color color1 = getBackground();
        Color color2 = color1.darker().darker().darker();
        
        Graphics2D g2d = (Graphics2D)g;
        
        int w = getWidth();
        int h = getHeight();
        
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
        setOpaque(false);
        super.paintComponent(g);
        setOpaque(true);
        
        if(MainFrame.backgroundText != null && MainFrame.backgroundText != "")
        	drawBackgroundText(MainFrame.backgroundText, color1.darker());
    }
    
    //écrit sur le dégradé
    private void drawBackgroundText(String text, Color c){
    	int x_init = (getWidth()/2)*-1;
    	int y_init = 30;
    	int w = text.length()*80;
    	int h = 150;
    	int x = 0;
    	int y = 0;
    	while(y_init + y*h < getHeight()){
    		while(x_init + x*w < getWidth()){
    			addTextLabel(text, c, x_init + x*w, y_init + y*h, w, h);
    			x += 2;
    		}
    		x = 0;
    		x_init += w/2;
    		y += 1;
    	}
    }
    
    private void addTextLabel(String text, Color c, int x, int y, int w, int h){
    	JLabel copyright = new JLabel(text);
    	copyright.setForeground(c);
    	copyright.setHorizontalTextPosition(SwingConstants.CENTER);
    	copyright.setHorizontalAlignment(SwingConstants.CENTER);
    	copyright.setFont(new Font("Arial", Font.BOLD, 100));
    	copyright.setBounds(x, y, w, h);
    	add(copyright);
    }
    
}
