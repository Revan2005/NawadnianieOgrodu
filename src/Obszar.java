import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;


public class Obszar {
	ArrayList<Point> punkty;
	Color kolor;
	
	public Obszar(Color k){
		punkty = new ArrayList<Point>();
		kolor = k;
	}
	
	public Obszar(Point a, Point b, Color k){
		punkty = new ArrayList<Point>();
		kolor = k;
		//a i b to rogi prostokata lezace na jednej przekatnej
		int x0, y0, x1, y1, x2, y2, x3, y3;
		x0 = a.x;
		y0 = a.y;
		x1 = b.x;
		y1 = a.y;
		x2 = b.x;
		y2 = b.y;
		x3 = a.x;
		y3 = b.y;
		
		Point p0 = new Point(x0, y0);
		Point p1 = new Point(x1, y1);
		Point p2 = new Point(x2, y2);
		Point p3 = new Point(x3, y3);
		//5 punkt jako domkniecie zeby bylo konsekwentnie z wielobokami
		Point pDomkniecia = new Point(p0);
		punkty.add(p0);
		punkty.add(p1);
		punkty.add(p2);
		punkty.add(p3);
		punkty.add(pDomkniecia);
}
	
	public Obszar(ArrayList<Point> p, Color k){
		punkty = new ArrayList<Point>(p);
		kolor = k;
	}
	
	public Point getPoint(int index){
		return punkty.get(index);
	}
	
	public Color getColor(){
		return kolor;
	}
	
	public Polygon getAsPolygon(){
		/*if(this instanceof Prostokat){
			Point rog1 = getPoint(0);
			Point rog2 = getPoint(1);
			int[] x = new int[4];
			int[] y = new int[4];
			int mx = Math.min(rog1.x, rog2.x); // mniejszy x
			int wx = Math.max(rog1.x, rog2.x); // wiekszy x
			int my = Math.min(rog1.y, rog2.y); // mniejszy y
			int wy = Math.max(rog1.y, rog2.y); // wiekszy y
			x[0] = mx;
			y[0] = my;
			x[1] = mx;
			y[1] = wy;
			x[2] = wx;
			y[2] = wy;
			x[3] = wx;
			y[3] = my;
			Polygon wielobok = new Polygon(x, y, 4);
			return wielobok;
		} else {*/
			int n = punkty.size();
			int[] x = new int[n];
			int[] y = new int[n];
			for(int i=0; i<n; i++){
				x[i] = getPoint(i).x;
				y[i] = getPoint(i).y;
			}	
			Polygon wielobok = new Polygon(x, y, n);
			return wielobok;
		//}
	}

	public void rysuj(Graphics2D g2d){
		//ponizej tylko na probe linie miedzy 1 a 2 zaznaczonym punktem czy rysuje w ogole i pamieta obszary
		/*Point p1 = getPoint(0);
		Point p2 = getPoint(1);
		g2d.setColor(Color.BLACK);
		g2d.drawLine(p1.x, p1.y, p2.x, p2.y);*/
		int n = punkty.size();
		int[] x = new int[n];
		int[] y = new int[n];
		for(int i=0; i<n; i++){
			x[i] = getPoint(i).x;
			y[i] = getPoint(i).y;
		}	
		Polygon wielobok = new Polygon(x, y, n);
		g2d.setColor(kolor);
		g2d.fillPolygon(wielobok);
	}
	
}
