import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;


public class PanelWyniku extends JPanel {
	ModelWyniku modelWyniku;
	
	public PanelWyniku(ModelWyniku mW){
		modelWyniku = mW;
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		modelWyniku.rysuj(g2d);
	}

}