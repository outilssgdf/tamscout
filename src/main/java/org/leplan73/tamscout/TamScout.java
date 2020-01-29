package org.leplan73.tamscout;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.leplan73.tamscout.utils.Preferences;

public class TamScout {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
		}
		Preferences.init();
		Logging.initLogger(TamScout.class);
		new MainEnLigne(Logging.logger_).go();
	}

}
