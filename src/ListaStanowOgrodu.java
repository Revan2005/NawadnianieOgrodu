import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;


public class ListaStanowOgrodu implements ActionListener {
	
	static final int MAX_LICZBA_PAMIETANYCH_STANOW = 100;
	LinkedList<ModelOgrodu> stany; 
	int wyswietlanyStan; //indeks wyswietlanego stanu (wazne z perspektywy cofnij/ ponow)
	PanelWidoku panelWidoku;
	PanelKontrolny panelKontrolny;
	
	public ListaStanowOgrodu(PanelWidoku pW, PanelKontrolny pK){
		
		panelWidoku = pW;
		panelKontrolny = pK;
		stany = new LinkedList<ModelOgrodu>();
		wyswietlanyStan = stany.size();
		
	}
	
	public void add(ModelOgrodu m){
		stany.add(m);
		if(stany.size()>MAX_LICZBA_PAMIETANYCH_STANOW)
			stany.remove(0);
		wyswietlanyStan = stany.size()-1;
		//if(panelKontrolny!=null)
			//panelKontrolny.cofnij.setEnabled(true);
	}

/*	public ModelOgrodu get(int i){
		return stany.get(i);
	}*/
	
	public int getSize(){
		return stany.size();
	}
	
/*	public int getWyswietlanyStan(){
		return wyswietlanyStan;
	}*/
	
	public ModelOgrodu getAktualny(){
		return stany.get(wyswietlanyStan);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//JButton sourceButton = (JButton)e.getSource();
		if(stany.size()>0){	
			if(e.getActionCommand()=="Cofnij"){
				if(wyswietlanyStan>0)//{
					wyswietlanyStan--;
					/*panelKontrolny.ponow.setEnabled(true);
				} else
					sourceButton.setEnabled(false);
					*/
			}	
			if(e.getActionCommand()=="Ponów"){
				if(wyswietlanyStan<stany.size()-1)
					wyswietlanyStan++;
				//else
					//sourceButton.setEnabled(false);
			}
			/*if(e.getActionCommand()=="Cofnij"){
				stany.addFirst(stany.getLast());
				stany.removeLast();
			}	
			if(e.getActionCommand()=="Ponów"){
				stany.addLast(stany.getFirst());
				stany.removeFirst();
				
			}*/
			panelWidoku.repaint();
		}

	}
	
}
