import java.awt.Toolkit;

import javax.swing.JFrame;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyFrame ramka = new MyFrame();
		//Toolkit toolkit = Toolkit.getDefaultToolkit();
		ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//ramka.setSize(toolkit.getScreenSize());
		ramka.setExtendedState(JFrame.MAXIMIZED_BOTH);
		ramka.setVisible(true);

	}

}
