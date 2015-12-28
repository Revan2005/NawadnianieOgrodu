import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


public class Solver {
	ModelWyniku wynik;
	int[][] reprezentacjaMacierzowa; //[szerokosc][wysokosc]
	double poleZielone, poleZolte, poleCzerwone; // w pixelach
	int pixelsPerMetr;
	ModelOgrodu modelOgrodu;
	ArrayList<Zraszacz> listaDostepnychZraszaczy;
	ArrayList<Zraszacz> listaUstawionychZraszaczy;
	//ponizej codzi o naIleMetrowMogaSiePokrywac
	double parametrZachodzenia = 5.0, krokZachodzenia = 0.1;
	
	public Solver(ModelOgrodu mO){
		modelOgrodu = mO;
		listaUstawionychZraszaczy = new ArrayList<Zraszacz>();
		//listaDostepnychZraszaczy = new ArrayList<Zraszacz>();
		
		reprezentacjaMacierzowa = mO.reprezentacjaMacierzowaTla;
		pixelsPerMetr = mO.pixelsPerMetr;
		poleZielone = 0;
		poleZolte = 0;
		poleCzerwone = 0;
		/*for(int i=0; i<reprezentacjaMacierzowa.length; i++)
			for(int j=0; j<reprezentacjaMacierzowa[0].length; j++){
				if(reprezentacjaMacierzowa[i][j]==1)
					poleZielone++;
				if(reprezentacjaMacierzowa[i][j]==0)
					poleZolte++;
				if(reprezentacjaMacierzowa[i][j]==-1)
					poleCzerwone++;
					
			}*/
		poleZielone = 0;
		for(int x=mO.lewyGornyRogMapki.x; x<mO.lewyGornyRogMapki.x+(mO.szerokoscDzialki*pixelsPerMetr); x+=10)
			for(int y=mO.lewyGornyRogMapki.y; y<mO.lewyGornyRogMapki.y+(mO.wysokoscDzialki*pixelsPerMetr); y+=10){
					for(int k=0; k<mO.listaZaznaczonychObszarow.size(); k++){
						if(mO.listaZaznaczonychObszarow.get(k).kolor.getGreen()>0&&mO.listaZaznaczonychObszarow.get(k).getAsPolygon().contains(new Point(x,y))){
							poleZielone+=100;
							break;
						}
					}		
			}
		
	}

	/*
	public void start(int procentKtoryMozeNieBycPokryty){
		
		int p = procentKtoryMozeNieBycPokryty;
		Zraszacz z = listaDostepnychZraszaczy.get(0);
		//System.out.println("polezielone: "+ poleZielone +"  polezraszacza = "+(z.promienEfektywny*pixelsPerMetr*z.promienEfektywny*pixelsPerMetr*Math.PI));
		int liczbaPotrzebnychZraszaczy = (int)(poleZielone/(z.promienEfektywny*pixelsPerMetr*z.promienEfektywny*pixelsPerMetr*Math.PI));
		
		//ponizej 100 to liczba osobnikow w populacji
		Point[][] populacjaRozkladowZraszaczy = new Point[100][liczbaPotrzebnychZraszaczy]; // to trza bedzie zamienic na liste typu zraszacz zeby uwzglednic rozne promienie i typy zraszaczy
		Point[][] nowaPopulacjaRozkladowZraszaczy = new Point[100][liczbaPotrzebnychZraszaczy]; // to trza bedzie zamienic na liste typu zraszacz zeby uwzglednic rozne promienie i typy zraszaczy
		double[] ppb = new double[100];
		//wylosowanie populacji poczatkowej (siatka o rozdzielczosci 10 pikseli)
		Random random = new Random();
		for(int i=0; i<100; i++){
			for(int j=0; j<liczbaPotrzebnychZraszaczy; j++){
				int x = Math.abs(random.nextInt()%reprezentacjaMacierzowa.length);
				int y = Math.abs(random.nextInt()%reprezentacjaMacierzowa[0].length);
				populacjaRozkladowZraszaczy[i][j] = new Point(x,y);
			}
		}
	
		
		int pokryteZielonePole;
		int procentPokryciaZielonegoPola;
		for(int pokolenie=0; pokolenie<100; pokolenie++){
			//System.out.println();
			System.out.println("p: " +pokolenie);
			//System.out.print("Ciag kolejnych i: ");
			for(int i=0; i<100; i++){
				//sprawdzenie czy ktorys zahacza o czerwone i przypisanie ppb rozmnozenia
				//System.out.print(i+" ");
				if(czyRozmieszczeniePodlewaCzerwone(populacjaRozkladowZraszaczy[i], liczbaPotrzebnychZraszaczy, z.promienEfektywny)){
					ppb[i]=1;
					//System.out.println(i);
				} else {
					//System.out.println(i);
					pokryteZielonePole = pokryteZielonePole(populacjaRozkladowZraszaczy[i], liczbaPotrzebnychZraszaczy, z.promienEfektywny);
					//System.out.println("pokryte zielone pole :"+pokryteZielonePole+" polezielone lacznie: "+poleZielone);
					procentPokryciaZielonegoPola = (pokryteZielonePole*100)/poleZielone;
					if(procentPokryciaZielonegoPola>85){
						ppb[i] = procentPokryciaZielonegoPola;
					} else {
						if(procentPokryciaZielonegoPola>70)
							ppb[i] = procentPokryciaZielonegoPola/2;
						else{
							ppb[i] = procentPokryciaZielonegoPola/4;
						}
					}
				}
			}

			int licznik=0;
			while(licznik<100){
				int randOsobnik = Math.abs(random.nextInt()%100);
				//int randZraszacz = random.nextInt()%liczbaPotrzebnychZraszaczy;
				int rand = Math.abs(random.nextInt()%100);
				//System.out.println(ppb[0]);
				if(ppb[randOsobnik]>rand){
					for(int zraszacz=0; zraszacz<liczbaPotrzebnychZraszaczy; zraszacz++){
						int x= populacjaRozkladowZraszaczy[randOsobnik][zraszacz].x;
						int y = populacjaRozkladowZraszaczy[randOsobnik][zraszacz].y;
						nowaPopulacjaRozkladowZraszaczy[licznik][zraszacz] = new Point(x, y);
					}
					//mutacja
					int czyMutujemy = Math.abs(random.nextInt()%100);
					if(czyMutujemy<=5){ //czestotliwosc mutacji 5%
						int x = Math.abs(random.nextInt()%reprezentacjaMacierzowa.length);
						int y = Math.abs(random.nextInt()%reprezentacjaMacierzowa[0].length);
						nowaPopulacjaRozkladowZraszaczy[0][0] = new Point(x, y);
					}
					licznik++;
				}
			}
			populacjaRozkladowZraszaczy = nowaPopulacjaRozkladowZraszaczy;
			//System.out.println("pokolenie: "+pokolenie+"nowapopulacja 0 0 : "+nowaPopulacjaRozkladowZraszaczy[0][0]);
		}
		
		for(int i=0; i<populacjaRozkladowZraszaczy[0].length; i++){
			int x = populacjaRozkladowZraszaczy[0][i].x;
			int y = populacjaRozkladowZraszaczy[0][i].y;
			z = z.klonuj();
			z.setPolozenie(new Point(x,y));
			listaUstawionychZraszaczy.add(z);
			for(int k=0; k<reprezentacjaMacierzowa.length; k++)
				for(int l=0; l<reprezentacjaMacierzowa[0].length; l++){
					if(Math.sqrt((k-x)*(k-x)+(l-y)*(l-y)) <=
							z.promienEfektywny*pixelsPerMetr)
						reprezentacjaMacierzowa[k][l] = 5;
				}
		}
			
			
		for(int i=0; i<reprezentacjaMacierzowa.length; i++){
			for(int j=0; j<reprezentacjaMacierzowa[0].length; j++){
				if(reprezentacjaMacierzowa[i][j]==1){
					z = z.klonuj();
					z.setPolozenie(new Point(i,j));
					listaUstawionychZraszaczy.add(z);
					for(int k=0; k<reprezentacjaMacierzowa.length; k++)
						for(int l=0; l<reprezentacjaMacierzowa[0].length; l++){
							if(Math.sqrt((k-i)*(k-i)+(l-j)*(l-j)) <=
									z.promienEfektywny*pixelsPerMetr)
								reprezentacjaMacierzowa[k][l] = 5;
						}
				}
					
			}
		}
		for(int i=0; i<listaUstawionychZraszaczy.size(); i++){
			;//System.out.println(listaUstawionychZraszaczy.get(i).polozenie);
		}
		
		
		listaUstawionychZraszaczy = new ArrayList<Zraszacz>();
		for(int i=0; i<liczbaPotrzebnychZraszaczy; i++){
			int x=populacjaRozkladowZraszaczy[0][i].x;
			int y=populacjaRozkladowZraszaczy[0][i].y;
			listaUstawionychZraszaczy.add(new Zraszacz(z.promienEfektywny, z.promienMaksymalny, new Point(x,y), z.cena));
		}
		
		//System.out.println(listaUstawionychZraszaczy);
		wynik = new ModelWyniku(reprezentacjaMacierzowa); 
		//wyswietlenie tego w nowym okienku
		JFrame ramka = new JFrame();
		//ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ramka.setExtendedState(JFrame.MAXIMIZED_BOTH);
		PanelWyniku panelWyniku = new PanelWyniku(wynik);
		ramka.add(panelWyniku);
		ramka.setVisible(true);
	}
	*/
	
	public boolean czyRozmieszczeniePodlewaCzerwone(Point[] lista, int lPZ, double r){
		for(int j=0; j<lPZ; j++){
			//System.out.println(lista[j] + " " + j);
			for(int k=0; k<reprezentacjaMacierzowa.length; k+=10)
				for(int l=0; l<reprezentacjaMacierzowa[0].length; l+=10){
					if(Math.sqrt((k-lista[j].x)*(k-lista[j].x)+(l-lista[j].y)*(l-lista[j].y)) <=
							r*pixelsPerMetr)
						if(reprezentacjaMacierzowa[k][l]==-1){
							return true;
						}
				}
		}
		return false;
	}
	
	public double pokryteZielonePole(ArrayList<Zraszacz> listaZraszaczy){
		//System.out.println("lpz = "+lPZ);
		double licznik = 0;
		ModelOgrodu mO = modelOgrodu;
		ArrayList<Zraszacz> lZ = listaZraszaczy;
		
		for(int x=mO.lewyGornyRogMapki.x; x<mO.lewyGornyRogMapki.x+(mO.szerokoscDzialki*pixelsPerMetr); x+=10)
			for(int y=mO.lewyGornyRogMapki.y; y<mO.lewyGornyRogMapki.y+(mO.wysokoscDzialki*pixelsPerMetr); y+=10){
				label:
				for(int j=0; j<lZ.size(); j++){
					if(Math.sqrt((x-lZ.get(j).polozenie.x)*(x-lZ.get(j).polozenie.x)+(y-lZ.get(j).polozenie.y)*(x-lZ.get(j).polozenie.x)+(y-lZ.get(j).polozenie.y)) <=
							lZ.get(j).promienEfektywny*pixelsPerMetr){
						for(int k=0; k<mO.listaZaznaczonychObszarow.size(); k++){
							if(mO.listaZaznaczonychObszarow.get(k).kolor.getGreen()>0&&mO.listaZaznaczonychObszarow.get(k).getAsPolygon().contains(new Point(x,y))){
								licznik+=100;
								break label;
							}
						}
					}		
				}
		}
		//System.out.println("Licznik = "+licznik);
		//System.out.println("zielonePole = "+poleZielone);
		return licznik/(double)poleZielone;
	}
	
	public ArrayList<Zraszacz> getUstawienieZraszaczy(){
		//System.out.println(listaUstawionychZraszaczy);
		return listaUstawionychZraszaczy;
	}

	
	
	
//TO CO ISTOTNE==================================================================================================
	
	public void startMetodaWierzchoolkowa(int procentKtoryMozePozostacNiepokryty, ArrayList<Zraszacz> lDZ){
		
		ArrayList<ArrayList<Zraszacz>> listaWszystkichWygenerowanychUstawien = new ArrayList<ArrayList<Zraszacz>>();
		//petla w ktorej zadaje rozne dopuszczalne dlugosci zachodzenia na siebie kol (od 10cm do 5m co 10 cm)- najmniejsze da pewnie najwiekszy niepodlany obszar
		//ale tez bedzie najtansze raczej wiec pozniej to posortuje po cenie i wybiore pierwsze, ktore pokrywa tyle procent ile chcial uzytkownik
		for(double i=0.1; i<parametrZachodzenia; i+=krokZachodzenia){
			ArrayList<Zraszacz> rozwazaneRozmieszczenieZraszaczy = iteracjaMetodyWierzchoolkowej(procentKtoryMozePozostacNiepokryty, lDZ, i);
			//if(rozwazaneRozmieszczenieZraszaczy != null)
			listaWszystkichWygenerowanychUstawien.add(rozwazaneRozmieszczenieZraszaczy);
		}
		//obliczenie kosztu
		double[] koszty = new double[listaWszystkichWygenerowanychUstawien.size()];
		double koszt;
		for(int i=0; i<koszty.length; i++){
			koszt = 0;
			for(int j=0; j<listaWszystkichWygenerowanychUstawien.get(i).size(); j++){
				koszt += listaWszystkichWygenerowanychUstawien.get(i).get(j).cena;
			}
			//System.out.println(i+1+". "+koszt);
			koszty[i] = koszt;
		}
		//teraz na podstawie tablicy kosztow sortuje ustawienia od najtanszego do najdrozszego
		//znajduje indeks najmniejszego kosztu, w liscie ustawien zamieniam obiekt z tego indeksu z pierwszym
		//po czym ustawiam pole z najmniejszym kosztem na 1000000000
		//koszty backupuje w tablicy kosztyBackup ktora tez sortuje
		double[] posortowaneKoszty = new double[koszty.length];
		for(int i=0; i<posortowaneKoszty.length; i++)
			posortowaneKoszty[i] = koszty[i];
		//sort tablicy kosztow
		double temp;
		for(int i=0; i<posortowaneKoszty.length; i++){
			for(int j=i; j<posortowaneKoszty.length; j++){
				if(posortowaneKoszty[j]<posortowaneKoszty[i]){
					temp = posortowaneKoszty[i];
					posortowaneKoszty[i] = posortowaneKoszty[j];
					posortowaneKoszty[j] = temp;
				}
			}
		}
		//sort listy
		ArrayList<ArrayList<Zraszacz>> posortowanaListaWszystkichWygenerowanychUstawien = new ArrayList<ArrayList<Zraszacz>>();
		double kosztMin;
		int indeksMinKosztu = 0;
		ArrayList<Zraszacz> tmp;
		double dtmp;
		for(int i=0; i<koszty.length; i++){
			kosztMin = 1000000000;
			for(int j=0; j<koszty.length; j++){
				if(koszty[j]<=kosztMin){
					indeksMinKosztu = j;
					kosztMin = koszty[j];
				}
			}	
			posortowanaListaWszystkichWygenerowanychUstawien.add(listaWszystkichWygenerowanychUstawien.get(indeksMinKosztu));
			//ustawiam najtansze rozmieszczenie na itej pozycji na liscie zamieniajac je z rozmieszczeniem z pozycji itej
			//tmp = listaWszystkichWygenerowanychUstawien.get(i);
			//listaWszystkichWygenerowanychUstawien.set(i, listaWszystkichWygenerowanychUstawien.get(indeksMinKosztu));
			//listaWszystkichWygenerowanychUstawien.set(indeksMinKosztu, tmp);
			//po czym zamieniam wartosc kosztu i tak zeby go juz nie uwzgledniac 
			koszty[i] = 1000000000;
		}
	
		/*
		for(int i=0; i<posortowaneKoszty.length; i++)
			System.out.println(i+1+".  "+posortowaneKoszty[i]);
		for(int i=0; i<posortowanaListaWszystkichWygenerowanychUstawien.size(); i++){
			koszt = 0;
			for(int j=0; j<posortowanaListaWszystkichWygenerowanychUstawien.get(i).size(); j++){
				koszt += posortowanaListaWszystkichWygenerowanychUstawien.get(i).get(j).cena;
			}
			System.out.println(i+1+".  "+koszt);
		}
		*/
			
//zwracam pierwszy indeks dla korego spelnione jest wymagany procent pokrycia
//
		int i=0;
		//System.out.println(pokryteZielonePole(posortowanaListaWszystkichWygenerowanychUstawien.get(0)));
		try{
			while(pokryteZielonePole(posortowanaListaWszystkichWygenerowanychUstawien.get(i))<1-procentKtoryMozePozostacNiepokryty/100.0){
				i++;
			}
		} catch (IndexOutOfBoundsException iobe){
			JOptionPane.showMessageDialog(new JFrame(), "Niestety nie znaleziono rozmieszczenia zraszaczy spełniającego ograniczenia.");
			iobe.printStackTrace();
		}
		if(i<posortowanaListaWszystkichWygenerowanychUstawien.size()){
			listaUstawionychZraszaczy = posortowanaListaWszystkichWygenerowanychUstawien.get(i);
			String tekst1 = "Koszt wynosi "+posortowaneKoszty[i]+"zł.\n"+"Procent pokrycia zielonego pola wynosi "+
					pokryteZielonePole(posortowanaListaWszystkichWygenerowanychUstawien.get(i))*100.0+"%.";
			//wypisuje liczbe kazdego typu zraszacza uzytego do pokrycia pola oraz cena za sztuke
			String tekst2 = new String(" ");
			for(int j=0; j<listaDostepnychZraszaczy.size(); j++){
				int licznik=0;
				for(int k=0; k<listaUstawionychZraszaczy.size(); k++){
					if(listaUstawionychZraszaczy.get(k).nazwa.equals(listaDostepnychZraszaczy.get(j).nazwa)){
						licznik++;
					}
				}
				tekst2+=(licznik+" zraszaczy typu "+listaDostepnychZraszaczy.get(j).nazwa+" o promieniu = "+listaDostepnychZraszaczy.get(j).promienEfektywny+"m, w cenie "+listaDostepnychZraszaczy.get(j).cena+"zł/szt. "+"Łącznie "+listaDostepnychZraszaczy.get(j).cena*licznik+"zł."+"\n");
			}
			JOptionPane.showMessageDialog(new JFrame(), tekst1+"\n\n"+tekst2);
			//System.out.println(tekst);
			//System.out.println();
		} else {
			//JOptionPane.showMessageDialog(new JFrame(), "Niestety nie znaleziono rozmieszczenia zraszaczy spełniającego ograniczenia.");
		}
 		
	}
	
	public ArrayList<Zraszacz> iteracjaMetodyWierzchoolkowej(int procentKtoryMozePozostacNiepokryty, ArrayList<Zraszacz> lDZ, double naIleMetrowKolaMogaNaSiebieZachodzic){
		//wielobok przechowuje zdublowany pierwszy punkt(jako pierwsz i ostatni pnkt zaznaczenia;
		ArrayList<Obszar> listaObszarow = modelOgrodu.listaZaznaczonychObszarow;
		ArrayList<Zraszacz> newlUZ = new ArrayList<Zraszacz>();
		Zraszacz rozpatrywanyTypZraszacza;
		Polygon wielobok; 
		Obszar obszar;
		
		listaDostepnychZraszaczy = lDZ;
		//sortuje liste dostepnych zraszaczy po stosunku cena/promien efektywny
		Zraszacz tmp;
		for(int i=0; i<listaDostepnychZraszaczy.size(); i++){
			for(int j=i+1; j<listaDostepnychZraszaczy.size(); j++){
				if(listaDostepnychZraszaczy.get(i).wspolczynnikCenaPromienEfektywny>
						listaDostepnychZraszaczy.get(j).wspolczynnikCenaPromienEfektywny){
					tmp = listaDostepnychZraszaczy.get(i);
					listaDostepnychZraszaczy.set(i, listaDostepnychZraszaczy.get(j));
					listaDostepnychZraszaczy.set(j, tmp);
				}
			}
		}
		for(int i=0; i<listaDostepnychZraszaczy.size(); i++)
			;//System.out.println(listaDostepnychZraszaczy.get(i).wspolczynnikCenaPromienEfektywny);
		
		
		for(int typ=0; typ<listaDostepnychZraszaczy.size(); typ++){
			
			rozpatrywanyTypZraszacza = listaDostepnychZraszaczy.get(typ);
			//dopisalem 4 linijki ponizej
			if(rozpatrywanyTypZraszacza.promienEfektywny<=naIleMetrowKolaMogaNaSiebieZachodzic){
				//System.out.println(rozpatrywanyTypZraszacza.promienEfektywny+"   "+naIleMetrowKolaMogaNaSiebieZachodzic);
				continue;
			}
			for(int i=0; i<listaObszarow.size(); i++){
				//na razie rozwazam tylko zielone obszary
				if(listaObszarow.get(i).getColor().getGreen()>0){
					obszar = listaObszarow.get(i);
					wielobok = obszar.getAsPolygon();
					
					//ustalam W PIXELAXCH odleglosci miedzy kazda para kolejnych rogow wieloboku
					ArrayList<Integer> listaOdleglosci = new ArrayList<Integer>();
					Point p1, p2;
					int d;
					for(int j=0; j<obszar.punkty.size()-1; j++){
						p1 = obszar.getPoint(j);
						p2 = obszar.getPoint(j+1);
						d = (int)Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
						listaOdleglosci.add(d);
					}

					
					//ustalam liste punktow na obwodzie (niektore sie zdubluja - trudno)
					ArrayList<Point> punktyObwodu = new ArrayList<Point>();
					//najpierw ida rogi a potem punkty na obwidzie
					for(int j=0; j<obszar.punkty.size()-1; j++){
						punktyObwodu.add(new Point(obszar.punkty.get(j).x, obszar.punkty.get(j).y));
					}
					//punkty na bokach wielokata ograniczajacego zielony obszar
					for(int j=0; j<obszar.punkty.size()-1; j++){
						Point punkt1 = obszar.punkty.get(j);
						Point punkt2 = obszar.punkty.get(j+1);
						double tgAlfa = (punkt2.y-punkt1.y)/(double)(punkt2.x-punkt1.x);
						double ctgAlfa = (punkt2.x-punkt1.x)/(double)(punkt2.y-punkt1.y);
						int x, y;
						//System.out.println(punkt1+"   "+punkt2);
						for(int ix=0; ix<Math.abs(punkt1.x-punkt2.x); ix++){
							if(punkt1.x<punkt2.x){
								x = punkt1.x + ix;
								y = punkt1.y + (int)Math.round(ix*tgAlfa);
							} else {
								x = punkt2.x + ix;
								y = punkt2.y + (int)Math.round(ix*tgAlfa);
							}
							punktyObwodu.add(new Point(x,y));
							//System.out.println(x+"  "+y);
						}
						for(int iy=0; iy<Math.abs(punkt1.y-punkt2.y); iy++){
							//y = Math.min(punkt1.y, punkt2.y) + iy;
							//x = (int)Math.round(y*ctgAlfa);
							if(punkt1.y<punkt2.y){
								y = punkt1.y + iy;
								x = punkt1.x + (int)Math.round(iy*ctgAlfa);
							} else {
								y = punkt2.y + iy;
								x = punkt2.x + (int)Math.round(iy*ctgAlfa);
							}
							punktyObwodu.add(new Point(x,y));
						}
					}
					//INACZEJ - bede szedl po obwodzieaz znajde 2 punkty w ktorych okrag przecina sie
					//w rogach moga byc tylko te ktore maja regulowany kat zraszania
					if(rozpatrywanyTypZraszacza.czyRegulowanyKat){
						//ustawiam zraszacze w rogach
						if(obszar.getColor().getGreen()>0){
//tu zmiana!!				//for(int j=0; j<obszar.punkty.size()-1; j++){
							for(int j=0; j<punktyObwodu.size()-1; j++){
								//int x = obszar.punkty.get(j).x;
								//int y = obszar.punkty.get(j).y;
								int x = punktyObwodu.get(j).x;
								int y = punktyObwodu.get(j).y;
								Point p = new Point(x, y);
								Zraszacz z = new Zraszacz(rozpatrywanyTypZraszacza.nazwa, rozpatrywanyTypZraszacza.promienEfektywny, rozpatrywanyTypZraszacza.promienMaksymalny, p, rozpatrywanyTypZraszacza.cena);
								//Zraszacz z = rozpatrywanyTypZraszacza.klonuj();
								boolean czyJuzPodlewane = false;
								for(int k=0; k<newlUZ.size(); k++){
									Zraszacz wczesniejUstawionyZraszacz = newlUZ.get(k);
									double odleglosc = Math.sqrt( (wczesniejUstawionyZraszacz.polozenie.x-z.polozenie.x)*(wczesniejUstawionyZraszacz.polozenie.x-z.polozenie.x)
											+(wczesniejUstawionyZraszacz.polozenie.y-z.polozenie.y)*(wczesniejUstawionyZraszacz.polozenie.y-z.polozenie.y) );
									if(odleglosc<pixelsPerMetr*(wczesniejUstawionyZraszacz.promienEfektywny+z.promienEfektywny-naIleMetrowKolaMogaNaSiebieZachodzic)){
										czyJuzPodlewane = true;
										break;
									}
								}
								if(!czyJuzPodlewane){
									//tu sprawdze jaki kat bedzie najlepszy
									double katZielonego = 0;
									double katKierunku = 0;
									boolean zmianaKoloruNaObwodzie = false;
									boolean zielonyNaObwodzie = false;
									int xO = (int)(x + z.promienEfektywny*pixelsPerMetr*Math.cos(0));
									int yO = (int)(y + z.promienEfektywny*pixelsPerMetr*Math.sin(0));
									if(wielobok.contains(new Point(xO, yO)))
										zielonyNaObwodzie = true;
									for(double k=0; k<2*Math.PI; k+=Math.PI/180.0){ //ide co jeden stopien
										xO = (int)(x + z.promienEfektywny*pixelsPerMetr*Math.cos(k));
										yO = (int)(y + z.promienEfektywny*pixelsPerMetr*Math.sin(k));
										if(wielobok.contains(new Point(xO, yO))){
											if(!zielonyNaObwodzie){
												zielonyNaObwodzie = true;
											}
											katZielonego+=Math.PI/180.0; //kat w radianach
										} else {
											if(zielonyNaObwodzie){ // zmiana z zielonego na niezielony to mnie interesuje
												//tu powinno wejsc tylko raz dla kazdego rogu i zraszacza
												zielonyNaObwodzie = false;
												katKierunku = k;
												//System.out.println("Typ zraszacza: "+rozpatrywanyTypZraszacza.nazwa+" punkt = "+p+" katKierunku = "+katKierunku*360/(2*Math.PI));
											}
										}
									}
									//System.out.println("katzielonego="+katZielonego+" z.katOd="+z.katOd+" z.katDo="+z.katDo);
									if((katZielonego>=rozpatrywanyTypZraszacza.katOd)&&(katZielonego<=rozpatrywanyTypZraszacza.katDo)){
										z.setKat(katZielonego);
										z.setKierunek(katKierunku);
										newlUZ.add(z);
										//System.out.println(z.kat);
									}
								} else
									;//System.out.println("juzpodlewane");
							}
						}
					}
					
					/* tak bylo wczesniej a calej wczesniejszej sekcji nei bylo
					//ustawiam zraszacze w rogach
					if(obszar.getColor().getGreen()>0){
						for(int j=0; j<obszar.punkty.size()-1; j++){
							int x = obszar.punkty.get(j).x;
							int y = obszar.punkty.get(j).y;
							Point p = new Point(x, y);
							Zraszacz z = new Zraszacz(rozpatrywanyTypZraszacza.nazwa, rozpatrywanyTypZraszacza.promienEfektywny, rozpatrywanyTypZraszacza.promienMaksymalny, p, rozpatrywanyTypZraszacza.cena);
							boolean czyJuzPodlewane = false;
							for(int k=0; k<newlUZ.size(); k++){
								Zraszacz wczesniejUstawionyZraszacz = newlUZ.get(k);
								double odleglosc = Math.sqrt( (wczesniejUstawionyZraszacz.polozenie.x-z.polozenie.x)*(wczesniejUstawionyZraszacz.polozenie.x-z.polozenie.x)
										+(wczesniejUstawionyZraszacz.polozenie.y-z.polozenie.y)*(wczesniejUstawionyZraszacz.polozenie.y-z.polozenie.y) );
								if(odleglosc<wczesniejUstawionyZraszacz.promienEfektywny*pixelsPerMetr){
									czyJuzPodlewane = true;
									break;
								}
							}
							if(!czyJuzPodlewane)
								newlUZ.add(z);
							else
								;//System.out.println("juzpodlewane");
						}
					}*/
					
					//wielobok.getBounds(); daje mi prostokat w krotym wpisany jest moj wielobok x,y,width,height
					Rectangle ramkaObszaru = wielobok.getBounds();
					//ta pierwsza petla for musze zamienic na while i uwzgledniac procent pokrycia pola i od tego ma zalezec koniec - petla while ma byc
					for(int m=0; m<1; m++){
						for(int x=ramkaObszaru.x; x<ramkaObszaru.x+ramkaObszaru.width; x++){
							for(int y=ramkaObszaru.y; y<ramkaObszaru.y+ramkaObszaru.height; y++){
								if(wielobok.contains(x, y)){
									int licznik=0;
									for(int l=0; l<newlUZ.size(); l++){
										Zraszacz z = newlUZ.get(l);
										double distance = Point.distance(x, y, z.polozenie.x, z.polozenie.y);
										//System.out.println(distance);
										//2 na koncu to 2 metry marginesu = tyle moga na siebie zachodzic kola spryskiwaczy
										if(distance<pixelsPerMetr*(z.promienEfektywny+rozpatrywanyTypZraszacza.promienEfektywny-naIleMetrowKolaMogaNaSiebieZachodzic))
											break;
										else 
											licznik++;	
									}
									if(licznik == newlUZ.size()){
										Zraszacz dodawanyZraszacz = new Zraszacz(rozpatrywanyTypZraszacza.nazwa, rozpatrywanyTypZraszacza.promienEfektywny,
												rozpatrywanyTypZraszacza.promienMaksymalny, new Point(x, y), rozpatrywanyTypZraszacza.cena);
										newlUZ.add(dodawanyZraszacz);
										//System.out.println("wszedl nowy zraszacz");
									}
										
								}
							}
						}
					}
				}
			}
			
		}
		return newlUZ;
	}
	
	public void setParametr(double parametr){
		parametrZachodzenia = parametr;
	}

	public void setKrok(double krok){
		krokZachodzenia = krok;
	}

}
