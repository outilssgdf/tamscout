package org.leplan73.tamscout.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.leplan73.tamscout.Declaration;
import org.leplan73.tamscout.Declarations;
import org.leplan73.tamscout.Transformeur;
import org.leplan73.tamscout.TransformeurException;
import org.slf4j.Logger;

public class EngineConversion {
	public void go(File fichierEntree, File fichierModele, File fichierSortie, Logger logger) throws IOException, TransformeurException
	{
		logger.info("Lancement de l'analyse");
		
		logger.info("Chargement du fichier source");
		FileInputStream fis = new FileInputStream(fichierEntree);
		Workbook wb = WorkbookFactory.create(fis);
		
		// Colonnes
		Sheet sheet = wb.getSheetAt(0);
		Row row = sheet.getRow(0);
		AtomicInteger nbC = new AtomicInteger(0);
		row.forEach(c -> nbC.incrementAndGet());
        
        Declarations declarations = new Declarations();
		
        int nb = sheet.getLastRowNum();
        for (int i=1;i<=nb;i++)
        {
    		Declaration d = new Declaration();
        	Row row2 = sheet.getRow(i);
        	for (int j=0;j<nbC.get();j++)
        	{
        		Cell cell = row2.getCell(j);
        		d.put(row.getCell(j).toString(),cell != null ? cell.toString() : null);
            }
    		declarations.add(d);
        }
		
		wb.close();
		fis.close();
		
		logger.info("Génération du fichier destination");
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("declarations", declarations);

		FileInputStream modele = new FileInputStream(fichierModele);
		
		FileOutputStream fosSortie = new FileOutputStream(fichierSortie);
		Transformeur.go(modele, beans, fosSortie);
		fosSortie.close();
		modele.close();
		logger.info("Génération terminée");
	}
}

