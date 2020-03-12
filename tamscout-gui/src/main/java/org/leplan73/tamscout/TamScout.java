package org.leplan73.tamscout;

import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.leplan73.tamscout.utils.Preferences;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "tamscout", mixinStandardHelpOptions = true)
public class TamScout extends TamScoutCmd {

	public static void main(String[] args) {
		new TamScout().go(args);
	}

	private void go(String[] args) {
		
		try {
			CommandLine commandLine = new CommandLine(this);
			List<CommandLine> parsed = commandLine.parse(args);
			
			if (parsed.size() == 1)
				run(parsed.get(0));
			else
				commandLine.usage(System.out);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
		}
	}

	
	public void run(CommandLine commandLine) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		TamScoutCmd cmd = (TamScoutCmd)commandLine.getCommand();
		
		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		Preferences.init();
		Logging.initLogger(TamScout.class, cmd.nologfile);
		new MainEnLigne(Logging.logger_).go();
	}

}
