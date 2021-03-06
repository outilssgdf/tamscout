package org.leplan73.tamscout.engine;

import org.slf4j.Logger;

public class EngineValidation extends Engine {

	public EngineValidation(Progress progress, Logger logger)
	{
		super(progress, logger);
	}

	public void go(String codeOrganisateur, String login, String motdepasse) throws EngineException {
		init();
		login(codeOrganisateur, login, motdepasse);
		close();
	}
}
