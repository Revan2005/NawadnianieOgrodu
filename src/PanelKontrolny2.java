import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


public class PanelKontrolny2 extends JPanel implements ActionListener{
	PanelWidoku panelWidoku;
	JButton utworzMapeZaznaczen;
	JButton znajdzRozmieszczenieZraszaczy;
	JButton opcjeZaawansowane;
//wczytywanie zraszaczy z pliku
	JButton wczytajListeDostepnychZraszaczy;
	JLabel ileProcMaNieBycPokryte;
	JTextField ileProcentMozeNieBycPokryte;
	ArrayList<Zraszacz> listaDostepnychZraszaczy;
	//dane do solvera - domyslne
	final double domyslnyParametrZachodzenia = 5.0, domyslnyKrokZachodzenia = 0.5;
	double parametrZachodzenia = 5.0, krokZachodzenia = 0.5;
	
	public PanelKontrolny2(PanelWidoku pW){
		panelWidoku = pW;
		/*
		listaDostepnychZraszaczy = new ArrayList<Zraszacz>();
		//TU DODAJE ZRASZACZE BEDZIE TRZEBA TO JAKOS LADOWAC Z PLIKU ALBO BD  zajmie sie tym button wczytajlisedostepnychzraszaczy==========================
		listaDostepnychZraszaczy.add(new Zraszacz("1", 10, 20, 5000));
		listaDostepnychZraszaczy.add(new Zraszacz("1", 6, 12, 120));
		listaDostepnychZraszaczy.add(new Zraszacz("3", 5, 10, 1000));
		listaDostepnychZraszaczy.add(new Zraszacz("4", 2.5, 5, 75));
		*/
		wczytajListeDostepnychZraszaczy = new JButton("Wczytaj dostępne typy zraszaczy");
		wczytajListeDostepnychZraszaczy.addActionListener(this);;
		add(wczytajListeDostepnychZraszaczy);
		
		utworzMapeZaznaczen = new JButton("Stworz mapę zaznaczeń");
		utworzMapeZaznaczen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame ramka = new JFrame();
				//ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				ramka.setExtendedState(JFrame.MAXIMIZED_BOTH);
				MapaZaznaczen mapaZaznaczen = new MapaZaznaczen(panelWidoku);
				PanelMapy panelMapy = new PanelMapy(mapaZaznaczen);
				ramka.add(panelMapy);
				ramka.setVisible(true);
				
			}
		});
		//add(utworzMapeZaznaczen);
		
		opcjeZaawansowane = new JButton("Opcje zaawansowane");
		opcjeZaawansowane.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame ramka = new JFrame();
				//ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				//ramka.setExtendedState(JFrame.MAXIMIZED_BOTH);
				ramka.setBounds(300, 200, 150, 150);
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				
				JLabel parametrLabel = new JLabel("Podaj parametr");
				final JTextField parametrTF = new JTextField("Podaj parametr");
				JLabel krokLabel = new JLabel("Podaj krok");
				final JTextField krokTF = new JTextField("Podaj krok");
				
				JButton wczytaj = new JButton("Wczytaj");
				wczytaj.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						String parametr = parametrTF.getText().trim();
						parametr = parametr.replace(",", ".");
						String krok = krokTF.getText().trim();
						krok = krok.replace(",", ".");

						double p=0, k=0;
						try{
							p = Double.parseDouble(parametr);
							k  = Double.parseDouble(krok);
						} catch (NumberFormatException nfe){
							JOptionPane.showMessageDialog(new JFrame(), "Niewłaściwa wartość, w obu polach powinny się znaleźć liczby rzeczywiste dodatnie");
						}
						if((p<0)||(k<0)){
							JOptionPane.showMessageDialog(new JFrame(), "Niewłaściwa wartość, w obu polach powinny się znaleźć liczby rzeczywiste dodatnie");
						} 
						if((p>0)&&(k>0)){
							parametrZachodzenia = p;
							krokZachodzenia = k;
						}
						
						System.out.println("parametrZachodzenia = "+parametrZachodzenia+ "krokZachodzenia = "+krokZachodzenia);
					}
				});
				
				JButton ustawDomyslne = new JButton("Ustaw domyślne");
				ustawDomyslne.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						parametrZachodzenia = domyslnyParametrZachodzenia;
						krokZachodzenia = domyslnyKrokZachodzenia;
					}
				});
				
				panel.add(parametrLabel);
				panel.add(parametrTF);
				panel.add(krokLabel);
				panel.add(krokTF);
				panel.add(wczytaj);
				panel.add(ustawDomyslne);
				ramka.add(panel);
				
				ramka.setVisible(true);
				
			}
		});
		add(opcjeZaawansowane);
		
		ileProcMaNieBycPokryte = new JLabel("Ile procent zielonego pola może pozostawać niepodlane?");
		add(ileProcMaNieBycPokryte);
		
		ileProcentMozeNieBycPokryte = new JTextField("    10      ");
		add(ileProcentMozeNieBycPokryte);
		
		znajdzRozmieszczenieZraszaczy = new JButton("Znajdź rozmieszczenie zraszaczy");
		znajdzRozmieszczenieZraszaczy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Solver solver = new Solver(panelWidoku.listaStanow.getAktualny());
				solver.setParametr(parametrZachodzenia);
				solver.setKrok(krokZachodzenia);
				try {
					int procent = Integer.parseInt(ileProcentMozeNieBycPokryte.getText().trim());
					if((procent<0)||(procent>100))
						JOptionPane.showMessageDialog(new JFrame(), "Procent musi byc liczbą naturalną z przedziału od 0 do 100");
					else{
						solver.startMetodaWierzchoolkowa(procent, listaDostepnychZraszaczy);
						panelWidoku.setListaZraszaczyAktualnegoModeluOgrodu(solver.getUstawienieZraszaczy());
					}
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(new JFrame(), "Procent musi byc liczbą naturalną z przedziału od 0 do 100");
					//System.out.println("printstackrace");
					//e2.printStackTrace();
				} catch (NullPointerException n){
					JOptionPane.showMessageDialog(new JFrame(), "Do znalezienia rozmieszczenia niezbędna jest informacja o dostępnych rodzajach zraszaczy.\n Kliknij przycisk: Wczytaj dostępne typy zraszaczy, a następnie wybierz odpowiedni plik.");
				} 

				
				
			}
		});
		add(znajdzRozmieszczenieZraszaczy);
	}

	//wczytywanie pliku z typami zraszaczy
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				 "txt", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        File f = chooser.getSelectedFile();
        FileReader fileReader;
		try {
			fileReader = new FileReader(f);
			 BufferedReader bufferedReader = new BufferedReader(fileReader);
		     //System.out.println(bufferedReader.readLine());
		     listaDostepnychZraszaczy = new ArrayList<Zraszacz>(); 
		     String nazwaTypuZraszacza;
		     double cena, promienEfektywny, promienMaksymalny, katOd, katDo;
		     //boolean czyRegulowanyKat;
		     String line = bufferedReader.readLine();
		     String[] lineAsArray;
 		     while(line!=null){
		    	 line = line.replace(",", " ");
		    	 line = line.replace(";", " ");
		    	 lineAsArray = line.split(" ");
		    	 for(int i=0; i<lineAsArray.length; i++){
		    		 lineAsArray[i] = lineAsArray[i].trim();
		    		 //System.out.println(i+".   "+lineAsArray[i]);
		    	 }
		    	 try{
		    		 nazwaTypuZraszacza = lineAsArray[0];
		    		 cena = Double.parseDouble(lineAsArray[1]);
		    		 promienEfektywny = Double.parseDouble(lineAsArray[2]);
		    		 promienMaksymalny = Double.parseDouble(lineAsArray[3]);
		    		 if((lineAsArray.length<5)||lineAsArray[4].equals("nie")||
		    			lineAsArray[4].equals("n")||lineAsArray[4].equals("Nie")||
		    			lineAsArray[4].equals("N")||lineAsArray[4].equals("NIE")){
		    		 		Zraszacz z = new Zraszacz(nazwaTypuZraszacza, promienEfektywny, promienMaksymalny, cena);
		    		 		listaDostepnychZraszaczy.add(z);
		    		 } else {
		    			 katOd = Double.parseDouble(lineAsArray[5]);
		    			 katDo = Double.parseDouble(lineAsArray[6]);
		    			 //zamiana kątów zestopni na radiany
		    			 katOd = (katOd*Math.PI*2)/360.0;
		    			 katDo = (katDo*Math.PI*2)/360.0;
		    			 Zraszacz z = new Zraszacz(nazwaTypuZraszacza, promienEfektywny, promienMaksymalny, cena, katOd, katDo);
		    			 listaDostepnychZraszaczy.add(z);
		    		 }
		    	 } catch(ArrayIndexOutOfBoundsException e3){
		 			JOptionPane.showMessageDialog(new JFrame(), "Błędny format pliku");
		    	 }
		    	
		    	 line = bufferedReader.readLine();
		     }
		     
		     bufferedReader.close();
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(new JFrame(), "File not found");
			//e1.printStackTrace();
		} catch (IOException e2){
			JOptionPane.showMessageDialog(new JFrame(), "IOException");
			//e2.printStackTrace();
		} catch (Exception e4){
			JOptionPane.showMessageDialog(new JFrame(), "	Niewłaściwy format pliku, w pliku nie mogą występować spacje ani puste wiersze. \nZwróć uwagę czy na końcu pliku nie występują puste wiersze.");
		}
       
	}

}
