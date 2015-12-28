import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class PanelKontrolny extends JPanel {

	MyFrame ramkaRodzic;
	JLabel podajWysokoscDzialkiLabel, podajSzerokoscDzialkiLabel, podajSkaleLabel;
	JTextField podajWysokoscDzialki, podajSzerokoscDzialki, podajSkale;
	JButton wczytajNowaDzialke, wczytajObrazDzialki, cofnij, ponow, zaznacz;
	ButtonGroup paraCofnijPonow;
	int wysokoscDzialki=1, szerokoscDzialki=2;
	
	public PanelKontrolny(MyFrame f){
		setBorder(BorderFactory.createLineBorder(Color.black));
		ramkaRodzic = f;
		podajWysokoscDzialkiLabel = new JLabel("Podaj wysokość [m]");
		podajWysokoscDzialki = new JTextField("          64");
		podajSzerokoscDzialkiLabel = new JLabel("Podaj szerokość [m]");
		podajSzerokoscDzialki = new JTextField("          47");
		wczytajNowaDzialke = new JButton("Wczytaj pustą działkę");
		wczytajNowaDzialke.addActionListener(f.panelWidoku);
		podajSkaleLabel = new JLabel("Podaj skalę");
		podajSkale = new JTextField("  1:100  ");
		wczytajObrazDzialki = new JButton("Wczytaj obraz działki");
		wczytajObrazDzialki.addActionListener(f.panelWidoku);
		cofnij = new JButton("Cofnij");
		cofnij.addActionListener(f.panelWidoku.listaStanow);
		ponow = new JButton("Ponów");
		ponow.addActionListener(f.panelWidoku.listaStanow);
		paraCofnijPonow = new ButtonGroup();
		paraCofnijPonow.add(cofnij);
		paraCofnijPonow.add(ponow);
		zaznacz = new JButton("Zaznacz obszar");
		zaznacz.addActionListener(f.panelWidoku);
		//System.out.println(cofnij.get());
		add(cofnij);
		add(ponow);
		
		add(podajWysokoscDzialkiLabel);
		add(podajWysokoscDzialki);
		add(podajSzerokoscDzialkiLabel);
		add(podajSzerokoscDzialki);
		add(wczytajNowaDzialke);
		add(podajSkaleLabel);
		add(podajSkale);
		add(wczytajObrazDzialki);
		add(zaznacz);
		
	}
	
	public Point getWymiaryDzialki(){

		wysokoscDzialki = Integer.parseInt(podajWysokoscDzialki.getText().trim());
		szerokoscDzialki = Integer.parseInt(podajSzerokoscDzialki.getText().trim());

		return new Point(szerokoscDzialki, wysokoscDzialki);
	}

	public double getSkala(){
		//return 0 oznacza jakis blad np. zly format
		String s = podajSkale.getText();
		s = s.replace(':', ' ');
		s = s.replace(',', '.');
		s = s.trim();
		String[] sArray = s.split(" ");
		
		try{
			double d1 = Double.parseDouble(sArray[0]);
			double d2 = Double.parseDouble(sArray[1]);
			return d1/d2;
		} catch(Exception e){
			return 0; //info o blednym formacie skali pojawi sie z poziomu panelu widoku bo dostanie tam 0 jako wynik tej metody
		}
		
	}
	
}
