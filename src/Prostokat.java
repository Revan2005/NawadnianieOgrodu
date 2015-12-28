/*import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;


public class Prostokat extends Obszar{

	public Prostokat(Point a, Point b, Color k){
		super(k);
		//punkty = new ArrayList<Point>(); ta linijka jest juz zawarta w konstruktorze super
		punkty.add(a);
		punkty.add(b);
		
	}
	
	@Override
	public void rysuj(Graphics2D g2d){
		g2d.setColor(kolor);
		int x0, y0, x1, y1;
		x0 = punkty.get(0).x;
		y0 = punkty.get(0).y;
		x1 = punkty.get(1).x;
		y1 = punkty.get(1).y;
		g2d.fillRect(Math.min(x0, x1), Math.min(y0,  y1), Math.abs(x0-x1), Math.abs(y0-y1));
	}
	
	
}*/
