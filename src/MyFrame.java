import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MyFrame extends JFrame {
	PanelWidoku panelWidoku;
	PanelKontrolny panelKontrolny;
	PanelKontrolny2 panelKontrolny2;
	PanelNarzedzi panelNarzedzi;
	
	public MyFrame(){
		setLayout(new BorderLayout());
		
		panelWidoku = new PanelWidoku(this);
		panelKontrolny = new PanelKontrolny(this);
		panelKontrolny2 = new PanelKontrolny2(panelWidoku);
		panelNarzedzi = new PanelNarzedzi(panelWidoku);
		
		
		add(panelWidoku);
		add(panelKontrolny, BorderLayout.NORTH);
		add(panelNarzedzi, BorderLayout.EAST);
		add(panelKontrolny2, BorderLayout.SOUTH);
		add(new JPanel(), BorderLayout.WEST);
		
		
	}

}
