package org.leplan73.tamscout.engine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Declaration extends HashMap<String,String> {
	public boolean getTrimestre()
	{
		String val = (String)this.get("Periode");
		if (val != null && val.compareTo("Trimestre") == 0)
			return true;
		return false;
	}
	
	public String getVisaOriginefr()
	{
		String val = (String)this.get("VisaOrigine");
		return val.compareTo("True") == 0 ? "Ok" : "Non Ok";
	}
	
	public String getVisaAccueilfr()
	{
		String val = (String)this.get("VisaAccueil");
		return val.compareTo("True") == 0 ? "Ok" : "Non Ok";
	}
	
	public String getEtatdeclaration()
	{
		String valOrigine = (String)this.get("VisaOrigine");
		String valAccueil = (String)this.get("VisaAccueil");
		if (valOrigine != null && Boolean.valueOf(valOrigine) == true && valAccueil != null && Boolean.valueOf(valAccueil))
			return "oui";
		return "non";
	}
	
	public String getEtatfr()
	{
		String val = getEtatdeclaration();
		return val.compareTo("oui") == 0 ? "Ok" : "Non Ok";
	}
	
	public Date getDatetri()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String val = (String)this.get("DateDebut");
		if (val != null)
			try {
				return simpleDateFormat.parse(val);
			} catch (ParseException e) {
			}
		return null;
	}
}
