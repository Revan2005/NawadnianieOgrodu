import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;

import javax.swing.JPanel;


public class PanelMapy extends JPanel {
	MapaZaznaczen mapaZaznaczen;
	
	public PanelMapy(MapaZaznaczen mZ){
		mapaZaznaczen = mZ;
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		mapaZaznaczen.rysuj(g2d);
	}

}
