package org.fbcmd4j;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import facebook4j.Account;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;



public class Utils {
	static final Logger logger = LogManager.getLogger(Utils.class);
	
	public static <T, R> Properties loadPropertiesFromFile(String foldername, String filename) throws IOException{
		Properties props = new Properties();
		Path configFile = Paths.get(foldername, filename);
		if (Files.exists(configFile)){
			props.load(Files.newInputStream(configFile));
			Predicate<String> funcionalInterface = v ->  {
				if(v.isEmpty())
					logger.info(v+" Se encuentra vacio");
				return false;
			};
			for (Enumeration<Object> e = props.keys(); e.hasMoreElements() ; ) {
			    Object obj = e.nextElement();
			    funcionalInterface.test(obj.toString());
			}
		} else {
			logger.info("Creando nuevo archivo de condifugración.");
			Files.copy(Paths.get(foldername,filename), configFile);
		}
		return props;
	}
	
	public static Facebook configuraFacebook(Properties props){
		Facebook facebook = new FacebookFactory().getInstance();
		Scanner scanner = new Scanner(System.in);
		logger.info("Configurando Instancia de facebook");
		
		String appId = props.getProperty("oauth.appId");
		String appSecret = props.getProperty("oauth.appSecret");
		if(appId==null || appId.isEmpty()){
			System.out.println("Ingresa appId:");
			appId = scanner.nextLine();
		}
		if(appSecret==null || appSecret.isEmpty()){
			System.out.println("Ingresa appSecret:");
			appSecret = scanner.nextLine();
		}
		
		facebook.setOAuthAppId(appId,appSecret);
		facebook.setOAuthPermissions(props.getProperty("oauth.permissions"));
		

		
		
		if(!(props.getProperty("oauth.accessToken")==null)){
			facebook.setOAuthAccessToken(new AccessToken(props.getProperty("oauth.accessToken"), null));
		}

		return facebook;
	}

	static void obtenerAccessToken(String foldername, String filename, Properties props, Scanner scanner) {
		try {
			Facebook facebook = new FacebookFactory().getInstance();

			facebook.getOAuthAppAccessToken();
			ResponseList<Account> accounts = facebook.getAccounts();
			Account yourPageAccount = accounts.get(0);  // if index 0 is your page account.
			String pageAccessToken = yourPageAccount.getAccessToken();
			String shortLivedToken = "your-short-lived-token";
			AccessToken extendedToken = facebook.extendTokenExpiration(shortLivedToken);
			
			logger.info("Obteninedo access Token.");
			System.out.println("Access token: " + extendedToken.getToken());
			

			
		} catch(FacebookException e){
			logger.error(e);
		}
	}

	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/*	
	public static void saveProperties(String foldername, String filename, Properties props) throws IOException{
		Path configFile = Paths.get(foldername, filename);
		props.store(Files.newOutputStream(configFile), "Generado por ObtenerAccessToken");
	}

	*/
}
