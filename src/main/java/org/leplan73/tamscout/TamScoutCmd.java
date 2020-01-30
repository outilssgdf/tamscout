package org.leplan73.tamscout;

import com.jcabi.manifests.Manifests;

import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Option;

public class TamScoutCmd implements IVersionProvider {
	
	@Option(names = "-nologfile", description = "pas de log dans un fichier (Valeur par défaut: ${DEFAULT-VALUE})", hidden = true)
	public boolean nologfile = false;

	@Override
	public String[] getVersion() throws Exception {
		return new String[] {"Version: "+Manifests.read("version"),"Date du build: "+Manifests.read("Build-Time")};
	}
}
