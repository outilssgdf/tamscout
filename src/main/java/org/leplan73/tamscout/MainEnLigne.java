package org.leplan73.tamscout;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.leplan73.tamscout.engine.EngineConversionEnLigne;
import org.leplan73.tamscout.engine.LoginEngineException;
import org.leplan73.tamscout.utils.ExcelFileFilter;
import org.leplan73.tamscout.utils.Images;
import org.leplan73.tamscout.utils.Preferences;
import org.leplan73.tamscout.utils.Version;
import org.slf4j.Logger;

import com.jcabi.manifests.Manifests;

@SuppressWarnings("serial")
public class MainEnLigne extends JFrame implements LoggedDialog {

	protected Logger logger_;
	
	private JPanel contentPane;
	private File fModele = new File("conf","modele_tam.xlsx");
	private JFileChooser fcSortieFichier;
	
	protected File fSortieFichier = new File("données","tam_sortie.xlsx");
	protected String nomFichier_;
	private BoutonOuvrir btnFichierOuvrir;
	private JLabel lblSortie;
	private JTextArea txtLog;
	private JButton button;
	
	private String identifiant_;
	private String motdepasse_;
	private String codeorganisateur_;

	/**
	 * Create the frame.
	 * @param logger 
	 */
	public MainEnLigne(Logger logger) {
		
		identifiant_ = Preferences.lit(Consts.TAM_IDENTIFIANT, "", true);
		motdepasse_ = Preferences.lit(Consts.TAM_MOTDEPASSE, "", true);
		codeorganisateur_ = Preferences.lit(Consts.TAM_CODE_ORGANISATEUR, "", true);
		
		logger_ = logger;
		setIconImage(Images.getIcon());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		double x = Preferences.litd(Consts.FENETRE_PRINCIPALE_X, 100);
		double y = Preferences.litd(Consts.FENETRE_PRINCIPALE_Y, 100);
		setBounds((int)x, (int)y, 688, 450);

		Appender.setLoggedDialog(this);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu("Fichier");
		menuBar.add(mnFichier);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Infos...");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Infos().setVisible(true);
			}
		});
		mnFichier.add(mntmNewMenuItem);
		
		JSeparator separator = new JSeparator();
		mnFichier.add(separator);
		
		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mnFichier.add(mntmQuitter);

		setResizable(false);
		majTitre();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Sortie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.anchor = GridBagConstraints.NORTH;
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panel.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		lblSortie = new JLabel(fSortieFichier.getAbsolutePath());
		panel_2.add(lblSortie, BorderLayout.WEST);
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.EAST);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JButton btnFichierSortie = new JButton("Fichier...");
		btnFichierSortie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fcSortieFichier = new JFileChooser();
				fcSortieFichier.setDialogTitle("Fichier de sortie");
				fcSortieFichier.setApproveButtonText("Export");
				fcSortieFichier.setCurrentDirectory(fSortieFichier.getParentFile());
				fcSortieFichier.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fcSortieFichier.removeChoosableFileFilter(fcSortieFichier.getFileFilter());
				fcSortieFichier.removeChoosableFileFilter(fcSortieFichier.getAcceptAllFileFilter());
				fcSortieFichier.addChoosableFileFilter(new ExcelFileFilter());
				int result = fcSortieFichier.showDialog(panel, "OK");
				if (result == JFileChooser.APPROVE_OPTION) {
					fSortieFichier = ajouteExtensionFichier(fcSortieFichier, lblSortie, "xlsx");
					btnFichierOuvrir.maj();
				}
			}
		});
		panel_3.add(btnFichierSortie, BorderLayout.WEST);
		
		btnFichierOuvrir = new BoutonOuvrir("Ouvrir...", lblSortie);
		btnFichierOuvrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					btnFichierOuvrir.ouvrir();
				} catch (SecurityException | IOException e1) {
					logger_.error(Logging.dumpStack(null, e1));
				}
			}
		});
		btnFichierOuvrir.setEnabled(false);
		panel_3.add(btnFichierOuvrir, BorderLayout.EAST);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panel.add(scrollPane, gbc_scrollPane);
		
		txtLog = new JTextArea();
		txtLog.setEditable(false);
		scrollPane.setViewportView(txtLog);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.anchor = GridBagConstraints.SOUTH;
		gbc_panel_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 2;
		panel.add(panel_4, gbc_panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5, BorderLayout.EAST);
		
		JButton button_1 = new JButton("Go");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(() -> {
					try {
						initLog();
						EngineConversionEnLigne engine = new EngineConversionEnLigne(null, logger_);
						engine.init();
						logger_.info("Connexion");
						String tmp = engine.login(codeorganisateur_, identifiant_, motdepasse_);
						engine.go(tmp, fModele, fSortieFichier);
						engine.close();
						btnFichierOuvrir.maj();
					} catch (LoginEngineException e1) {
						logger_.error(Logging.dumpStack(null, e1));
					}
				}).start();
			}
		});
		panel_5.add(button_1);
		
		button = new JButton("Fermer");
		panel_5.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		JButton btnNewButton = new JButton("Paramètres");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Configuration().setVisible(true);
			}
		});
		panel_4.add(btnNewButton, BorderLayout.WEST);
		btnNewButton.setFont(btnNewButton.getFont().deriveFont(btnNewButton.getFont().getSize() - 1f));
		btnNewButton.setToolTipText("Définir l'identification utilisée et autres paramètres");
		btnNewButton.setIcon(Images.getIconCog());
	}

	@Override
	public void initLog() {
		if (txtLog != null) txtLog.setText("");
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
	
	private void majTitre()
	{
		try {
			Version v = Version.parse(Manifests.read("version"));
			setTitle("TamScout v" + v.toString());
		} catch (java.lang.IllegalArgumentException e) {
			setTitle("TamScout (dev)");
		}
	}

	@Override
	public void dispose() {
		Preferences.sauved(Consts.FENETRE_PRINCIPALE_X, this.getLocation().getX());
		Preferences.sauved(Consts.FENETRE_CONFIGURATION_Y, this.getLocation().getY());
		Preferences.sauve(Consts.REPERTOIRE_SORTIE, this.fSortieFichier.getAbsoluteFile().getParent(), false);
		super.dispose();
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
	
	public void go()
	{
		this.setVisible(true);
	}
	public JTextArea getTxtLog() {
		return txtLog;
	}
	public JButton getButton() {
		return button;
	}
}
