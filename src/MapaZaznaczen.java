import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;


public class MapaZaznaczen {
	//ArrayList<Zraszacz> listaZraszaczy;
	ArrayList<Obszar> listaZaznaczonychObszarow;
	//PanelWidoku panelWidoku;
	Point lewyGornyRogMapki;
	int wysokoscMapy, szerokoscMapy; //dlugosc i szerokosc mapy w pikselach
	int pixelsPerMetr; 
	ModelOgrodu mO;
	Color[][] bitMapa;
	
	
	public MapaZaznaczen(PanelWidoku pW){
		mO = pW.listaStanow.getAktualny();
		//listaZraszaczy = mO.getListaZraszaczy();
		listaZaznaczonychObszarow = mO.getListaZaznaczonychObszarow();
		lewyGornyRogMapki = mO.getLewyGornyRogMapki();
		pixelsPerMetr = mO.pixelsPerMetr;
		wysokoscMapy = mO.wysokoscDzialki*pixelsPerMetr;
		szerokoscMapy = mO.szerokoscDzialki*pixelsPerMetr;
		bitMapa = new Color[szerokoscMapy][wysokoscMapy];
	}
	
	public void rysuj(Graphics2D g2d){
		
		//dla kazdego piksela w obszarze sprawdzam wszystkie obieky obszarow zaznaczen skladowe barwy
		//zraszacze na razie pomijam!!
		System.out.println(mO.reprezentacjaMacierzowaTla[0][0]);
		for(int i=0; i<szerokoscMapy; i++){
			for(int j=0; j<wysokoscMapy; j++){
				//int x = i+lewyGornyRogMapki.x;
				//int y = j+lewyGornyRogMapki.y;
				switch (mO.reprezentacjaMacierzowaTla[i][j]) {
				case -1:
					bitMapa[i][j] = Color.RED;
					break;
				case 0:
					bitMapa[i][j] = Color.YELLOW;
					break;
				case 1:
					bitMapa[i][j] = Color.GREEN;
					break;
				default:
					break;
				}
			}
		}
		
		//rysuje bimape na panelu ktory przekazal mi obiekt g2d
		for(int i=0; i<szerokoscMapy; i++){
			for(int j=0; j<wysokoscMapy; j++){
				int x = i+lewyGornyRogMapki.x;
				int y = j+lewyGornyRogMapki.y;
				if(bitMapa[i][j]!=null){
					g2d.setColor(bitMapa[i][j]);
				}
				else
					g2d.setColor(Color.WHITE);
				g2d.fillRect(x, y, 10, 10);
			}
		}
		
		
	}
	
}
