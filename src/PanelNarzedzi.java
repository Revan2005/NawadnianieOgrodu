import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class PanelNarzedzi extends JPanel {
	
	PanelWidoku panelWidoku;
	JButton czerwony;
	JButton zielony;
	JButton zolty;
	int stopienPrzezroczystosci = 100;
	JButton dodajZraszacz;
	
	public PanelNarzedzi(PanelWidoku pw){
		panelWidoku = pw;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//setBackground(Color.YELLOW);
		
		czerwony = new JButton("Czerwony");
		czerwony.setBackground(Color.RED);
		czerwony.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panelWidoku.setColorLPM(new Color(255, 0, 0, stopienPrzezroczystosci));
				//panelWidoku.setTrybDodawaniaZraszaczy(false);
				
			}
		});
		add(czerwony);
		
		zielony = new JButton("   Zielony");
		zielony.setBackground(Color.GREEN);
		zielony.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panelWidoku.setColorLPM(new Color(0, 255, 0, stopienPrzezroczystosci));
				//panelWidoku.setTrybDodawaniaZraszaczy(false);
				
			}
		});
		add(zielony);
		
		zolty = new JButton("     Żółty");
		zolty.setBackground(Color.YELLOW);
		zolty.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panelWidoku.setColorLPM(new Color(255, 255, 0, stopienPrzezroczystosci));
				//panelWidoku.setTrybDodawaniaZraszaczy(false);
				
			}
		});
		add(zolty);
		
		dodajZraszacz = new JButton("Dodaj zraszacz");
		dodajZraszacz.setBackground(Color.BLUE);
		dodajZraszacz.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				panelWidoku.setTrybDodawaniaZraszaczy(true);
				
			}
		});
		add(dodajZraszacz);
		
	}

}
