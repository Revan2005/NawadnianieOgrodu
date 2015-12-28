import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;


public class PanelWidoku extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
	
	MyFrame ramkaRodzic;
	int wysokoscDzialki, szerokoscDzialki;
	//Kwadrat[][] kwadraciki; zamiast tego bedzie stan obecny i pare rzeczy trzeba przeniesc do innej klasy (model ogrodu albo listastanow ogrodu)
	ModelOgrodu aktualnyModelOgrodu;
	Point2D polozenieMyszyWMomencieKlikniecia;
	//Point lewyGornyRogMapki;
	Point2D[] ramkaZaznaczenia;
	//int a = 10;
	//ModelOgrodu modelOgrodu; 
	ListaStanowOgrodu listaStanow;
	Color aktualnyKolorLPM;
	boolean trybDodawaniaZraszaczy, trybZaznaczaniaObszaru;
	ArrayList<Point> listaPunktowPrzyZaznaczaniu;
	final int r = 5;
	
	
	public PanelWidoku(MyFrame f){
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEtchedBorder());
		
		trybDodawaniaZraszaczy = false;
		trybZaznaczaniaObszaru = false;
		listaPunktowPrzyZaznaczaniu = new ArrayList<Point>();
		aktualnyKolorLPM = Color.WHITE;
		
		listaStanow = new ListaStanowOgrodu(this, f.panelKontrolny);	
		ramkaRodzic = f;
		
		listaStanow.add(new ModelOgrodu());	
	}
	
	public void setColorLPM(Color k){
		aktualnyKolorLPM = k;
		
		trybDodawaniaZraszaczy = false;
		listaPunktowPrzyZaznaczaniu = new ArrayList<Point>();
		repaint();
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		
		listaStanow.getAktualny().rysujPodzialke(g2d, this);
		listaStanow.getAktualny().rysuj(g2d);
		
		
		//ramka obramowanie kiedy zaznaczam mysza obszar
		if((ramkaZaznaczenia!=null)&&(ramkaZaznaczenia[1]!=null)){
			g2d.setColor(Color.BLACK);
			int xP, yP, xA, yA;
			xP = (int)ramkaZaznaczenia[0].getX();
			yP = (int)ramkaZaznaczenia[0].getY();
			xA = (int)ramkaZaznaczenia[1].getX();
			yA = (int)ramkaZaznaczenia[1].getY();
			g2d.drawRect(Math.min(xP, xA), Math.min(yP,  yA), Math.abs(xP-xA), Math.abs(yP-yA));
		}
		
		//listapunktow przy zaznaczaniu - rysuje jak niepusta
		if(listaPunktowPrzyZaznaczaniu!=null){
			g2d.setColor(Color.BLACK);
			for(int i=0; i<listaPunktowPrzyZaznaczaniu.size(); i++){
				Point p = listaPunktowPrzyZaznaczaniu.get(i);
				g2d.drawOval(p.x-r, p.y-r, 2*r, 2*r);
			}
			for(int i=1; i<listaPunktowPrzyZaznaczaniu.size(); i++){
				Point p1 = listaPunktowPrzyZaznaczaniu.get(i-1);
				Point p2 = listaPunktowPrzyZaznaczaniu.get(i);
				g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton()==3){
			trybZaznaczaniaObszaru = false;
			listaPunktowPrzyZaznaczaniu = new ArrayList<Point>();
			repaint();
		}
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		
		polozenieMyszyWMomencieKlikniecia = e.getPoint();
		ramkaZaznaczenia = new Point[2];
		ramkaZaznaczenia[0] = e.getPoint();
		
		repaint();
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		//ten color white to stan neutralny nie rysuje bialych obiektow biel daje mi sygnal ze dzialania nie powinny niesc skutkow - kolor jest bialy na poczatku dzialania programu
		if((trybDodawaniaZraszaczy == false)&&(trybZaznaczaniaObszaru==false)&&(aktualnyKolorLPM != Color.WHITE)){
			//boolean czyStanSieZmienil = false;
			ramkaZaznaczenia = null;
			Point pA = e.getPoint(); //pA - polozenieAktualne
			Point pP = (Point)polozenieMyszyWMomencieKlikniecia; //pP - polozenie poczatkowe
			
			ModelOgrodu nowyModel = listaStanow.getAktualny().addObszar(new Obszar(pA, pP, aktualnyKolorLPM));
			if(nowyModel!=null)
				listaStanow.add(nowyModel);
			repaint();
		} 
		if(trybDodawaniaZraszaczy == true){
			//int bokKwadracika = listaStanow.getAktualny().pixelsPerMetr;
			ModelOgrodu newModel = listaStanow.getAktualny().addZraszacz(new Zraszacz("Zraszacz uzytkownika", 5, 10, new Point(e.getPoint()), 0));
			listaStanow.add(newModel);
			
			trybDodawaniaZraszaczy = false;
			repaint();
		}
		//znowu bialy = stan neutralny nic nie zaznaczamy 
		if((trybZaznaczaniaObszaru == true)&&(aktualnyKolorLPM != Color.WHITE)){
			listaPunktowPrzyZaznaczaniu.add(e.getPoint());
			int size = listaPunktowPrzyZaznaczaniu.size();
			for(int i=0; i<size-2; i++){ //size-1 to ostatni size-2 to przedostatni(jak jest petla miedzy ostatnim a przedostatnim to obszar = zero -nie ma sensu rozpatrywac)
				Point p1 = listaPunktowPrzyZaznaczaniu.get(size-1);
				Point p2 = listaPunktowPrzyZaznaczaniu.get(i);
				//System.out.println(Math.abs(p1.x-p2.x));
				//gdy zaznaczony obszar sie zamyka dodaje do aktualnego stanu modeluogrodu nowy ksztalt wyznaczony przez liste punktowprzyzaznaczaniu
				if( (Math.abs(p1.x-p2.x)<=r) && (Math.abs(p1.y-p2.y)<=r) ){
					//listaStanow.getAktualny().zaznacz(listaPunktowPrzyZaznaczaniu)
					ModelOgrodu newModel = listaStanow.getAktualny().addObszar(new Obszar(listaPunktowPrzyZaznaczaniu, aktualnyKolorLPM));
					listaStanow.add(newModel);
					listaPunktowPrzyZaznaczaniu = new ArrayList<Point>();
					trybZaznaczaniaObszaru = false;
					break;
				}
			}
			repaint();
		}
		
		
	}

	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(ramkaZaznaczenia!=null)
			ramkaZaznaczenia[1] = e.getPoint();
		repaint();
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		JButton sourceButton = (JButton)e.getSource();
		trybZaznaczaniaObszaru = false; //po kliknieciu w innny button zmieniam stan zaznaczania na neutralny
		listaPunktowPrzyZaznaczaniu = new ArrayList<Point>(); //jak nie jestem juz w trybie zaznaczania to musza zniknac punkty i linie dotad zaznaczone
		repaint();
		
		try{
			//setTrybDodawaniaZraszaczy(false);
			Point wymiaryDzialki = ramkaRodzic.panelKontrolny.getWymiaryDzialki();
			wysokoscDzialki = wymiaryDzialki.y;
			szerokoscDzialki = wymiaryDzialki.x;

			if(sourceButton.getText().equals("Zaznacz obszar")){
				trybZaznaczaniaObszaru = true;
				trybDodawaniaZraszaczy = false;
			}
			
			if(sourceButton.getText().equals("Wczytaj pustą działkę")){			
				if((wysokoscDzialki>300)||(szerokoscDzialki>300))
					JOptionPane.showMessageDialog(new JFrame(), "Wymiary działki nie mogą przekraczać 300 metrów.");
				else{
					listaStanow.add(new ModelOgrodu(this, wysokoscDzialki, szerokoscDzialki));
					repaint();
				}
			}
		
			if(sourceButton.getText().equals("Wczytaj obraz działki")){
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"JPG & GIF Images", "jpg", "gif");
		        chooser.setFileFilter(filter);
		        int returnVal = chooser.showOpenDialog(this);
		        File f = chooser.getSelectedFile();
		        try {
					Image img = ImageIO.read(f);
					
					double dlugoscDzialkiPIXELS = img.getWidth(new ImageObserver() {
						
						@Override
						public boolean imageUpdate(Image img, int infoflags, int x, int y,
								int width, int height) {
							// TODO Auto-generated method stub
							return false;
						}
					});
					double szerokoscDzialkiPIXELS = img.getHeight(new ImageObserver() {
						
						@Override
						public boolean imageUpdate(Image img, int infoflags, int x, int y,
								int width, int height) {
							// TODO Auto-generated method stub
							return false;
						}
					});
					
					double skala = ramkaRodzic.panelKontrolny.getSkala();
					if(skala==0){
						JOptionPane.showMessageDialog(new JFrame(), "Błędny format skali. Powinny to być dwie liczby przedzielone dwukropkiem, seperatorem dziesiętnym może być przecinek lub kropka (przykłady porawneych wartości 1:100 1:200 1:120,5 1,5:10 1.5:200)");
					} else {
						if((dlugoscDzialkiPIXELS*skala>300)||(szerokoscDzialkiPIXELS*skala>300)){
							JOptionPane.showMessageDialog(new JFrame(), "Wymiary działki nie mogą przekraczać 300 metrów.");
						} else {
							listaStanow.add(new ModelOgrodu(this, img, skala));
							repaint();
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}    
			}
			
		} catch(NumberFormatException npe){
			//JOptionPane.showMessageDialog(new JFrame(), "Podaj wymiary działki.");
			npe.printStackTrace();
		}
		
		
	}
	
	public void setTrybDodawaniaZraszaczy(boolean b){
		trybDodawaniaZraszaczy = b;
		if(trybDodawaniaZraszaczy){
			trybZaznaczaniaObszaru=false;
			listaPunktowPrzyZaznaczaniu = new ArrayList<Point>();
			repaint();
		}
	}
	
	//korzysta z tego panel kontrolny 2 przy nacisnieciu buttona znajdz rozmieszczenie zraszaczy - ustawia w modelu liste zraszaczy ktora wygenerowal
	public ArrayList<Zraszacz> getListaZraszaczyAktualnegoModeluOgrodu(){
		aktualnyModelOgrodu = listaStanow.getAktualny();
		return aktualnyModelOgrodu.listaZraszaczy;
	}
	
	public void setListaZraszaczyAktualnegoModeluOgrodu(ArrayList<Zraszacz> lista){
		//trzeba przesunac lewy gorny rog z polozenia 0,0 do lewego gornego rogu obrazka na panelu
		for(int i=0; i<lista.size(); i++){
			//Point lGRM = listaStanow.getAktualny().getLewyGornyRogMapki();
			int x = lista.get(i).polozenie.x;//+lGRM.x;
			int y = lista.get(i).polozenie.y;//+lGRM.y;
			lista.get(i).polozenie = new Point(x, y);
		}
		aktualnyModelOgrodu = listaStanow.getAktualny();
		aktualnyModelOgrodu.listaZraszaczy = lista;
		repaint();
	}
	

}
