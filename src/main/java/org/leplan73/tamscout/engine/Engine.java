package org.leplan73.tamscout.engine;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.leplan73.tamscout.Progress;
import org.slf4j.Logger;

public class Engine {

	protected Progress progress_;
	protected Logger logger_;
	
	protected static final String PEER_CERTIFICATES = "PEER_CERTIFICATES";
	protected static final String HTTPS_TAM = "https://tam.extranet.jeunesse-sports.gouv.fr";
	
	protected CloseableHttpClient httpclient_;
	
	public Engine(Progress progress, Logger logger)
	{
		progress_ = progress;
		logger_ = logger;
	}
	
	public void init() throws EngineException
	{
		BasicCookieStore cookieStore = new BasicCookieStore();
		
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		SSLContext sslContext;
		try {
			sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, 
			NoopHostnameVerifier.INSTANCE);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = 
			RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
			
			BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
			httpclient_ = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(connectionManager).setDefaultCookieStore(cookieStore).build();
		} catch (KeyManagementException e) {
			throw new EngineException("Erreur de connexion", e);
		} catch (NoSuchAlgorithmException e) {
			throw new EngineException("Erreur de connexion", e);
		} catch (KeyStoreException e) {
			throw new EngineException("Erreur de connexion", e);
		}
	}
	
	public void close()
	{
		try {
			httpclient_.close();
		} catch (IOException e) {
		}
	}
	
	public String login(String codeOrganisateur, String login, String motdepasse) throws EngineException
	{
		HttpPost httppostStructures = new HttpPost(HTTPS_TAM + "/TamV4Service/Account/Login");
		httppostStructures.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:31.0) Gecko/20100101 Firefox/31.0");
		httppostStructures.addHeader("Content-Type", "application/json; charset=UTF-8");
		httppostStructures.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");

		String query = "{\"codeOrg\":\""+codeOrganisateur+"\",\"login\":\""+login+"\",\"password\":\""+motdepasse+"\"}";
		StringEntity JsonEntity;
		CloseableHttpResponse response = null;
		try {
			JsonEntity = new StringEntity(query);
			httppostStructures.setEntity(JsonEntity);
			response = httpclient_.execute(httppostStructures);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				HttpEntity entity = response.getEntity();
				String obj = EntityUtils.toString(entity);
				response.close();
				return obj;
			}
			else
			{
				throw new EngineException("Erreur de connexion");
			}
		} catch (IOException e) {
			throw new EngineException("Erreur de connexion");
		}
		finally
		{
			try {
				if (response != null) response.close();
			} catch (IOException e) {
			}
		}
	}
}
