package org.leplan73.tamscout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Fenetre extends JFrame {

	public void go()
	{
		this.setVisible(true);
		JOptionPane.showMessageDialog(this, 
				"Le fichier d'export est à disposition exclusive des Scouts et Guides de France et uniquement pour ceux qui en ont besoin\n\n"+
				"En cliquant sur le bouton OK, je déclare accepter cette règle.", "Information sur les données TAM", JOptionPane.WARNING_MESSAGE);
	}
}
