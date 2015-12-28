import java.awt.Color;
import java.awt.Graphics2D;


public class ModelWyniku {
	int[][] reprezentacjaMacierzowa;
	
	public ModelWyniku(int[][] wynik){
		reprezentacjaMacierzowa = wynik;
	}
	
	public void rysuj(Graphics2D g2d){
		for(int i=0; i<reprezentacjaMacierzowa.length; i++)
			for(int j=0; j<reprezentacjaMacierzowa[0].length; j++){
				switch (reprezentacjaMacierzowa[i][j]) {
				case -1:
					g2d.setColor(Color.RED);
					break;
				case 0:
					g2d.setColor(Color.YELLOW);
					break;
				case 1:
					g2d.setColor(Color.GREEN);
					break;
				case 5:
					g2d.setColor(new Color(0, 0, 255, 100));
					break;
				default:
					break;
				}
				g2d.drawLine(i, j, i, j);
			}
	}
}
