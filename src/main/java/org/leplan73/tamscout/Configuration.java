package org.leplan73.tamscout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.leplan73.tamscout.engine.EngineValidation;
import org.leplan73.tamscout.engine.EngineException;
import org.leplan73.tamscout.utils.Preferences;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class Configuration extends Dialogue {

	private final JPanel contentPanel = new JPanel();
	private JTextField txfIdentifiant;
	private JPasswordField txfMotdepasse;
	private JTextField txfCodeOrganisateur;
	private JButton okButton;

	/**
	 * Create the dialog.
	 */
	public Configuration() {
		logger_ = LoggerFactory.getLogger(Configuration.class);
		setModal(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Configuration");

		double x = Preferences.litd(Consts.FENETRE_CONFIGURATION_X, 100);
		double y = Preferences.litd(Consts.FENETRE_CONFIGURATION_Y, 100);
		setBounds((int)x, (int)y, 430, 280);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Code organisateur", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				txfCodeOrganisateur = new JTextField();
				txfCodeOrganisateur.setColumns(15);
				panel.add(txfCodeOrganisateur, BorderLayout.CENTER);
				txfCodeOrganisateur.setText(Preferences.lit(Consts.TAM_CODE_ORGANISATEUR, "", true));
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Acc\u00E8s Intranet", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Identifiant", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(panel_1, BorderLayout.WEST);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfIdentifiant = new JTextField();
					panel_1.add(txfIdentifiant);
					txfIdentifiant.setColumns(15);
					txfIdentifiant.setText(Preferences.lit(Consts.TAM_IDENTIFIANT, "", true));
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Mot de passe", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfMotdepasse = new JPasswordField();
					txfMotdepasse.setColumns(15);
					panel_1.add(txfMotdepasse);
					txfMotdepasse.setText(Preferences.lit(Consts.TAM_MOTDEPASSE, "", true));
				}
				{
					JCheckBox chckbxNewCheckBox = new JCheckBox("Afficher");
					chckbxNewCheckBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (chckbxNewCheckBox.isSelected()) {
								txfMotdepasse.setEchoChar((char)0);
							   } else {
								   txfMotdepasse.setEchoChar('*');
							   }
						}
					});
					panel_1.add(chckbxNewCheckBox);
				}
			}
		}
		{
			JButton btnNewButton = new JButton("Validation");
			GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
			gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnNewButton.gridx = 0;
			gbc_btnNewButton.gridy = 2;
			contentPanel.add(btnNewButton, gbc_btnNewButton);
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					go();
				}
			});
			{
				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				{
					okButton = new JButton("OK");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					okButton.setActionCommand("OK");
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);
				}
			}
		}
		okButton.setEnabled(txfCodeOrganisateur.getText().isEmpty() == false);
	}

	public void go() {
		ProgressMonitor guiprogress = new ProgressMonitor(this, "Validation code organisateur", "", 0, 100);
		
		Progress progress = new GuiProgress(guiprogress, this.getTitle());
		progress.setMillisToPopup(0);
		progress.setMillisToDecideToPopup(0);
		
		new Thread(() -> {
			initLog();
				try {
					EngineValidation engine = new EngineValidation(progress, logger_);
					engine.go(txfCodeOrganisateur.getText(), txfIdentifiant.getText(), new String(txfMotdepasse.getPassword()));
					okButton.setEnabled(true);
				} catch (EngineException e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
				} catch (Exception e) {
					logger_.error(Logging.dumpStack(null, e));
				}
		}).start();
	}

	@Override
	public void dispose() {
		Preferences.sauved(Consts.FENETRE_CONFIGURATION_X, this.getLocation().getX());
		Preferences.sauved(Consts.FENETRE_CONFIGURATION_Y, this.getLocation().getY());
		Preferences.sauve(Consts.TAM_IDENTIFIANT, txfIdentifiant.getText(), true);
		Preferences.sauve(Consts.TAM_MOTDEPASSE, new String(txfMotdepasse.getPassword()), true);
		Preferences.sauve(Consts.TAM_CODE_ORGANISATEUR, txfCodeOrganisateur.getText(), true);
		super.dispose();
	}
}
