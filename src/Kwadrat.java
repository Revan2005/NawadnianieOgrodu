import java.awt.Color;
import java.awt.geom.Point2D;


public class Kwadrat implements Cloneable{
	
	int x,y,a;
	Color k;
	
	public Kwadrat(int poczatekX, int poczatekY, int bok, Color kolor){
		x=poczatekX; 
		y=poczatekY;
		a=bok;
		k=kolor;
	}
	
	public void setColor(Color kolor){
		k=kolor;
	}

	public Kwadrat klonuj(){
		return new Kwadrat(x, y, a, k);
	}
}
