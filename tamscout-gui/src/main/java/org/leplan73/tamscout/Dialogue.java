package org.leplan73.tamscout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.leplan73.tamscout.utils.Images;
import org.leplan73.tamscout.utils.Preferences;
import org.slf4j.Logger;

public class Dialogue extends JDialog implements LoggedDialog {

	protected String identifiant_;
	protected String motdepasse_;
	protected String codeorganisateur_;
	
	protected Logger logger_;
	protected JTextArea txtLog;
	
	public Dialogue()
	{
		setIconImage(Images.getIcon());
		addEscapeListener(this);
		
		identifiant_ = Preferences.lit(Consts.TAM_IDENTIFIANT, "", true);
		motdepasse_ = Preferences.lit(Consts.TAM_MOTDEPASSE, "", true);
		codeorganisateur_ = Preferences.lit(Consts.TAM_CODE_ORGANISATEUR, "", true);
	}

	@Override
	public void initLog() {
		if (txtLog != null) txtLog.setText("");
	}
	
	private static void addEscapeListener(final JDialog dialog) {
	    ActionListener escListener = new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            dialog.setVisible(false);
	        }
	    };

	    dialog.getRootPane().registerKeyboardAction(escListener,
	            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	            JComponent.WHEN_IN_FOCUSED_WINDOW);

	}
	
	protected boolean checkIntranet()
	{
		if (identifiant_.isEmpty()) {
			logger_.error("L'identifiant est vide");
			return false;
		}
		if (motdepasse_.isEmpty()) {
			logger_.error("Le mode de passe est vide");
			return false;
		}
		if (codeorganisateur_.isEmpty()) {
			logger_.error("Le code organisateur est vide");
			return false;
		}
		return true;
	}
	
	protected boolean checkIntranet(boolean codeStructureNationalAutorise)
	{
		if (identifiant_.isEmpty()) {
			logger_.error("L'identifiant est vide");
			return false;
		}
		if (motdepasse_.isEmpty()) {
			logger_.error("Le mode de passe est vide");
			return false;
		}
		return true;
	}
	
	protected File ajouteExtensionFichier(JFileChooser fc, JLabel label, String extension)
	{
		File f = fc.getSelectedFile();
		String path = f.getPath();
		if (path.endsWith("."+extension) == false) {
			path = path + "."+extension;
			f = new File(path);
		}
		label.setText(path);
		return f;
	}

	@Override
	public void addLog(String message) {
		if (txtLog != null)
		{
			String texte = txtLog.getText();
			if (texte.length() > 0)
				txtLog.append("\n");
			txtLog.append(message);
			txtLog.setCaretPosition(txtLog.getDocument().getLength());
		}
	}
}
