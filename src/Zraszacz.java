import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;


public class Zraszacz {

	final double promienEfektywny;
	final double promienMaksymalny;
	int pixelsPerMetr;
	Point polozenie;
	double cena;
	double wspolczynnikCenaPromienEfektywny;
	double katOd, katDo, kat=Math.PI*2;
	double kierunek; //jak na matmie kat miedzy wektorem a osia ox 0-360stopni i od tego kata  dajmy na to x rysuje dalej wycinek kola do kata x+kat 
	boolean czyRegulowanyKat = false;
	String nazwa;
	
	public Zraszacz(String n, double r, double R, Point p, double koszt){
		
		nazwa = n;
		cena = koszt;
		promienEfektywny = r;
		promienMaksymalny = R;
		polozenie = p;
		wspolczynnikCenaPromienEfektywny = cena/promienEfektywny;
	
	}
	
	public Zraszacz(String n, double r, double R, double koszt){
		
		nazwa = n;
		promienEfektywny = r;
		promienMaksymalny = R;
		polozenie = new Point(0,0);
		cena = koszt;
		wspolczynnikCenaPromienEfektywny = cena/promienEfektywny;
	}
	
	public Zraszacz(String n, double r, double R, double koszt, double katOd, double katDo){
		
		nazwa = n;
		promienEfektywny = r;
		promienMaksymalny = R;
		polozenie = new Point(0,0);
		cena = koszt;
		this.katOd = katOd;
		this.katDo = katDo;
		czyRegulowanyKat = true;
		wspolczynnikCenaPromienEfektywny = cena/promienEfektywny;
	}
	
	public Zraszacz klonuj(){
		Zraszacz klon = new Zraszacz(nazwa, promienEfektywny, promienMaksymalny, new Point(polozenie.x, polozenie.y), cena);
		klon.cena = cena;
		klon.wspolczynnikCenaPromienEfektywny = wspolczynnikCenaPromienEfektywny;
		klon.nazwa = nazwa;
		klon.czyRegulowanyKat = czyRegulowanyKat;
		klon.katOd = katOd;
		klon.katDo = katDo;
		return klon;
	}
	
	public void setPolozenie(Point p){
		polozenie = p;
	}
	
	public void setKat(double k){
		kat = k;
	}
	
	public void setKierunek(double kier){
		kierunek = kier;
	}
	
	public void rysujZraszacz(Graphics2D g2d, int pPM){
		double srednicaEfektywna = 2*promienEfektywny;
		//na razie nie rysuje tego zasiegu maksymalnego
		double srednicaMaksymalna = 2*promienMaksymalny;
		if(kat == Math.PI*2){ // czy kat ma wartosc domyslna rowna 360 stopniom?
			g2d.setColor(new Color(0, 0, 255, 60));
			g2d.fillOval((int)(polozenie.x-promienEfektywny*pPM), (int)(polozenie.y-promienEfektywny*pPM), (int)(srednicaEfektywna*pPM), (int)(srednicaEfektywna*pPM));
			g2d.setColor(new Color(0, 0, 255, 30));
			//g2d.fillOval((int)(polozenie.x-promienMaksymalny*pPM), (int)(polozenie.y-promienMaksymalny*pPM), (int)(srednicaMaksymalna*pPM), (int)(srednicaMaksymalna*pPM));
		} else {
			g2d.setColor(new Color(0, 0, 255, 60));
			int startAngle = -(int)((kierunek*360)/(Math.PI*2));
			int arcAngle = (int)((kat*360)/(Math.PI*2));
			g2d.fillArc((int)(polozenie.x-promienEfektywny*pPM), (int)(polozenie.y-promienEfektywny*pPM), (int)srednicaEfektywna*pPM, (int)srednicaEfektywna*pPM, startAngle, arcAngle);
		}
	}
	
}
