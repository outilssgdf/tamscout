package org.leplan73.tamscout.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.leplan73.tamscout.Declaration;
import org.leplan73.tamscout.Declarations;
import org.leplan73.tamscout.Progress;
import org.leplan73.tamscout.Transformeur;
import org.leplan73.tamscout.TransformeurException;
import org.slf4j.Logger;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class EngineConversionEnLigne extends Engine {

	public EngineConversionEnLigne(Progress progress, Logger logger)
	{
		super(progress, logger);
	}

	public void go(String tmp, File fichierModele, File fichierSortie) throws EngineException {
		logger_.info("Extraction");
		
		Integer departementId = null;
		Integer exerciceId = null;
		
		Object jsonDocument = Configuration.defaultConfiguration().jsonProvider().parse(tmp);
		if (jsonDocument != null) {
			departementId = JsonPath.read(jsonDocument, "$.content.departementId");
			exerciceId = JsonPath.read(jsonDocument, "$.content.exerciceId");
		}
		
		if (departementId != null && exerciceId != null)
		{
			HttpPost httppostStructures2 = null;
			CloseableHttpResponse response = null;
			try {
				httppostStructures2 = new HttpPost(HTTPS_TAM + "/TamV4Service/FC/getExcelDocument");
				httppostStructures2.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
				httppostStructures2.addHeader("Content-Type", "application/json; charset=UTF-8");
				httppostStructures2.addHeader("Accept", "application/json, text/plain, */*");
				
				String query = "{\"numberFi\":\"\",\"organizer\":\"\",\"etatId\":0,\"homeId\":-1,\"decTypeId\":0,\"visaOrigin\":-1,\"visaHome\":-1,\"zipCode\":\"\",\"town\":\"\",\"periodId\":0,\"entryId\":1,\"handicapId\":-1,\"followId\":\"-1\",\"additionalActivities\":false,\"traineesDirectors\":false,\"leastSixYears\":false,\"outOfTime\":false,\"plusQuatreVingt\":-1,\"exerciceId\":"+exerciceId+"}";
				StringEntity JsonEntity = new StringEntity(query);
				httppostStructures2.setEntity(JsonEntity);
				response = httpclient_.execute(httppostStructures2);
				
				HttpEntity entity = response.getEntity();
				byte[] obj2 = EntityUtils.toByteArray(entity);
				
				Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(obj2));
				
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
				
				logger_.info("Génération du fichier destination");
				Map<String, Object> beans = new HashMap<String, Object>();
				beans.put("declarations", declarations);

				FileInputStream modele = new FileInputStream(fichierModele);
				
				FileOutputStream fosSortie = new FileOutputStream(fichierSortie);
				Transformeur.go(modele, beans, fosSortie);
				fosSortie.close();
				modele.close();
				logger_.info("Génération terminée");
				
			} catch (IOException | TransformeurException e) {
				throw new EngineException("Erreur de conversion",e);
			}
			finally
			{
				if (response != null)
					try {
						response.close();
					} catch (IOException e) {
					}
			}
		}
		else
		{
			throw new EngineException("Données de connexion incorrectes");
		}
		
		close();
	}
}
