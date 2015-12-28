import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;


public class ModelOgrodu {
	
	int[][] reprezentacjaMacierzowaTla; //-1 czerwone 0 zolte 1 zielone
	ArrayList<Zraszacz> listaZraszaczy;
	ArrayList<Obszar> listaZaznaczonychObszarow;
	PanelWidoku panelWidoku;
	Point lewyGornyRogMapki;
	int wysokoscDzialki, szerokoscDzialki; // bok kwadracika - liczba pikseli odpowiadajaca 1 metrowi
	int pixelsPerMetr = 10;
	Image obrazekWTle;
	
	//konstruktor do utworzenia pierwszego modelu w zasadzie bez znaczenia
	public ModelOgrodu(){
		listaZaznaczonychObszarow = new ArrayList<Obszar>();
		listaZraszaczy = new ArrayList<Zraszacz>();
		reprezentacjaMacierzowaTla = new int[10][10];
	}
	
	//konstruktor do wczytywania dzialki golej bez obrazka w tle
	public ModelOgrodu(PanelWidoku pW, int wys, int szer){
		panelWidoku = pW;
		obrazekWTle = null;
		wysokoscDzialki = wys;
		szerokoscDzialki = szer;
		listaZraszaczy = new ArrayList<Zraszacz>();
		listaZaznaczonychObszarow = new ArrayList<Obszar>();
		
		int wysokoscPanelu = pW.getHeight();
		int szerokoscPanelu = pW.getWidth();
		int a = 10;
		//dostosowuje skale jak dialka sie nie miesci to pomniejszam a, ...
		while((wysokoscPanelu*0.9<wysokoscDzialki*a)||(szerokoscPanelu*0.9<szerokoscDzialki*a))
			a-=1;	
		// a jezeli dzialka jest malutka to powiekszam a
		while((wysokoscPanelu*0.8>wysokoscDzialki*a)&&(szerokoscPanelu*0.8>szerokoscDzialki*a))
			a+=1;
		
		pixelsPerMetr = a;
		//ustalam polozenie lewego gornego rogu mapki
		int x=(szerokoscPanelu-(szerokoscDzialki*a))/2;
		int y=(wysokoscPanelu-(wysokoscDzialki*a))/2;
		lewyGornyRogMapki = new Point(x, y);
		
		//tworze macierz reprezentujaca poszczegolne piksele
		reprezentacjaMacierzowaTla = new int[szerokoscDzialki*a][wysokoscDzialki*a];
	}
	
	
	
	
	public ModelOgrodu(PanelWidoku pW, Image o, double skala){
		panelWidoku = pW;
		obrazekWTle = o;
		listaZraszaczy = new ArrayList<Zraszacz>();
		listaZaznaczonychObszarow = new ArrayList<Obszar>();
		
		double szerokoscDzialkiPIXELS = obrazekWTle.getWidth(new ImageObserver() {
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y,
					int width, int height) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		szerokoscDzialki = (int)(szerokoscDzialkiPIXELS*skala);
		
		double wysokoscDzialkiPIXELS = obrazekWTle.getHeight(new ImageObserver() {
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y,
					int width, int height) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		wysokoscDzialki = (int)(wysokoscDzialkiPIXELS*skala);
		
		double stosunekWysDoSzerObrazka = wysokoscDzialkiPIXELS/szerokoscDzialkiPIXELS;
		int wysokoscPanelu = pW.getHeight();
		int szerokoscPanelu = pW.getWidth();

		//dostosowuje skale jak dzialka sie nie miesci to pomniejszam a, ...
		while((wysokoscPanelu*0.9<wysokoscDzialkiPIXELS)||(szerokoscPanelu*0.9<szerokoscDzialkiPIXELS)){
			szerokoscDzialkiPIXELS *= 0.9;
			wysokoscDzialkiPIXELS = stosunekWysDoSzerObrazka*szerokoscDzialkiPIXELS;
		}
				
		// a jezeli dzialka jest malutka to powiekszam a
		while((wysokoscPanelu*0.8>wysokoscDzialkiPIXELS)&&(szerokoscPanelu*0.8>szerokoscDzialkiPIXELS)){
			szerokoscDzialkiPIXELS *= 1.1;
			wysokoscDzialkiPIXELS = stosunekWysDoSzerObrazka*szerokoscDzialkiPIXELS;
		}
		
		obrazekWTle = Scalr.resize((BufferedImage)obrazekWTle, (int)szerokoscDzialkiPIXELS, (int)wysokoscDzialkiPIXELS);
		//pixelspermetr (wczesniej ta zmienna nazywala sie "bokKwadracika") to ma byc metr potrzebuje info o dl i szer w metrach obrazka
		pixelsPerMetr = (int)(szerokoscDzialkiPIXELS/szerokoscDzialki);
		//ustalam polozenie lewego gornego rogu mapki
		int x=(int)(szerokoscPanelu-(szerokoscDzialkiPIXELS))/2;
		int y=(int)(wysokoscPanelu-(wysokoscDzialkiPIXELS))/2;
		lewyGornyRogMapki = new Point(x, y);
		
		reprezentacjaMacierzowaTla = new int[(int)szerokoscDzialkiPIXELS][(int)wysokoscDzialkiPIXELS];
	}
	
	
	
	public void rysuj(Graphics2D g2d){
		try {
				g2d.drawImage(obrazekWTle, lewyGornyRogMapki.x, lewyGornyRogMapki.y, new ImageObserver() {
				
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y,
						int width, int height) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("pewnie null pointer exception");
			e.printStackTrace();
		}
		
		//narysuj wszystkie ksztalty na liscie
		for(int i=0; i<listaZaznaczonychObszarow.size(); i++){
			listaZaznaczonychObszarow.get(i).rysuj(g2d);
		}
		
		//narysuj wszystkie zraszacze na liscie
		for(int i=0; i<listaZraszaczy.size(); i++){
			listaZraszaczy.get(i).rysujZraszacz(g2d, pixelsPerMetr);
		}
	}
	
	public void rysujPodzialke(Graphics2D g2d, PanelWidoku pW){
		g2d.setColor(Color.BLACK);
		if(szerokoscDzialki>0)
			for(int i=0; i<=szerokoscDzialki; i++)
				if(i%10==0)
					g2d.drawLine(lewyGornyRogMapki.x+i*pixelsPerMetr, 0, lewyGornyRogMapki.x+i*pixelsPerMetr, 21);
				else if(i%5==0)
					g2d.drawLine(lewyGornyRogMapki.x+i*pixelsPerMetr, 0, lewyGornyRogMapki.x+i*pixelsPerMetr, 14);
				else
					g2d.drawLine(lewyGornyRogMapki.x+i*pixelsPerMetr, 0, lewyGornyRogMapki.x+i*pixelsPerMetr, 7);
		if(wysokoscDzialki>0)
			for(int i=0; i<=wysokoscDzialki; i++)
				if(i%10==0)
					g2d.drawLine(0, lewyGornyRogMapki.y+i*pixelsPerMetr, 21, lewyGornyRogMapki.y+i*pixelsPerMetr);
				else if(i%5==0)
					g2d.drawLine(0, lewyGornyRogMapki.y+i*pixelsPerMetr, 14, lewyGornyRogMapki.y+i*pixelsPerMetr);
				else
					g2d.drawLine(0, lewyGornyRogMapki.y+i*pixelsPerMetr, 7, lewyGornyRogMapki.y+i*pixelsPerMetr);
		//rysuje skale w rogu x,y przwy dolny rog (z marginesem 30)
		int x = pW.getWidth()-30;
		int y = pW.getHeight()-0;
		for(int i=0; i<=10; i++)
			if(i%10==0)
				g2d.drawLine(x-i*pixelsPerMetr, y-21, x-i*pixelsPerMetr, y);
			else if(i%5==0)
				g2d.drawLine(x-i*pixelsPerMetr, y-14, x-i*pixelsPerMetr, y);
			else
				g2d.drawLine(x-i*pixelsPerMetr, y-7, x-i*pixelsPerMetr, y);
	
		g2d.drawString("10m", x-5*pixelsPerMetr-10, y-20);
	}
	
	
	
	
	public ModelOgrodu klonuj(){	
		PanelWidoku pW = panelWidoku;
		int wys = wysokoscDzialki;
		int szer = szerokoscDzialki;
		ModelOgrodu newModel = new ModelOgrodu(pW, wys, szer);
		
		Image o = obrazekWTle;
		newModel.obrazekWTle = o;
		
		Point lgrm = new Point(lewyGornyRogMapki.x, lewyGornyRogMapki.y);
		newModel.lewyGornyRogMapki = lgrm;
		
		int a = pixelsPerMetr;
		newModel.pixelsPerMetr = a;
		
		ArrayList<Obszar> lZO = new ArrayList<Obszar>(listaZaznaczonychObszarow);
		newModel.listaZaznaczonychObszarow = lZO;
		
		ArrayList<Zraszacz> lZ = new ArrayList<Zraszacz>(listaZraszaczy);
		newModel.listaZraszaczy = lZ;
		
		int s = szerokoscDzialki*pixelsPerMetr;
		int h = wysokoscDzialki*pixelsPerMetr;
		newModel.reprezentacjaMacierzowaTla = new int[s][h];
		for(int i=0; i<s; i++)
			for(int j=0; j<h; j++)
				newModel.reprezentacjaMacierzowaTla[i][j] = reprezentacjaMacierzowaTla[i][j];
		
		return newModel;
	}
	
	public ModelOgrodu addZraszacz(Zraszacz z){
		ModelOgrodu newModel;
		newModel = this.klonuj();
		newModel.listaZraszaczy.add(z);
		return newModel;
	}
	
	public ModelOgrodu removeZraszacz(){
		ModelOgrodu newModel;
		newModel = this.klonuj();
		if(newModel.listaZraszaczy.size()>0)
			newModel.listaZraszaczy.remove(newModel.listaZraszaczy.size()-1);
		return newModel;
	}
	
	public ModelOgrodu addObszar(Obszar p){
		ModelOgrodu newModel;
		newModel = this.klonuj();
		newModel.listaZaznaczonychObszarow.add(p);	
		
		//reprezentacjaMacierzowa
		int szerokoscPixels = reprezentacjaMacierzowaTla.length;
		int wysokoscPixels = reprezentacjaMacierzowaTla[0].length;
		for(int i=0; i<szerokoscPixels; i++){
			for(int j=0; j<wysokoscPixels; j++){
				int x = i+lewyGornyRogMapki.x;
				int y = j+lewyGornyRogMapki.y;
				Polygon wielobok = 
						p.getAsPolygon();
				if(wielobok.contains(new Point(x, y))){
					Color kolor = p.getColor();
					if((kolor.getRed()==0)&&(kolor.getGreen()>0)){
						//czerwone zawsze na wierzchu
						if(reprezentacjaMacierzowaTla[i][j]==-1){
							;
						} else {
							newModel.reprezentacjaMacierzowaTla[i][j] = 1;
							//System.out.println("zielony");
						}
					}
					if((kolor.getRed()>0)&&(kolor.getGreen()>0)){
						//czerwone na wierzchu
						if(reprezentacjaMacierzowaTla[i][j]==-1){
							;
						} else {
							newModel.reprezentacjaMacierzowaTla[i][j] = 0;
							//System.out.println("zolty");
						}	
					}
					if((kolor.getRed()>0)&&(kolor.getGreen()==0)){
						newModel.reprezentacjaMacierzowaTla[i][j] = -1;
						//System.out.println("czerwony");
					}
					
				}
			}
		}
		
		
		return newModel;
	}
	
	public ModelOgrodu removeObszar(Obszar p){
		ModelOgrodu newModel;
		newModel = this.klonuj();
		if(newModel.listaZaznaczonychObszarow.size()>0)
			newModel.listaZaznaczonychObszarow.remove
			(newModel.listaZaznaczonychObszarow.size()-1);	
		
		//reprezentacjaMacizerzowa
		
		return newModel;
	}
	
	
	public ArrayList<Zraszacz> getListaZraszaczy(){
		return new ArrayList<Zraszacz>(listaZraszaczy);
	}
	
	public ArrayList<Obszar> getListaZaznaczonychObszarow(){
		return new ArrayList<Obszar>(listaZaznaczonychObszarow);
	}
	
	public Point getLewyGornyRogMapki(){
		return new Point(lewyGornyRogMapki);
	}
}
