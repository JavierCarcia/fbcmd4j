package org.fbcmd4j;


import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import facebook4j.Facebook;
import facebook4j.PostUpdate;





public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class);
	
	private static final String CONFIG_DIR = "src\\config";
	private static final String CONFIG_FILE = "fbcmd4j.properties";
	private static final String APP_VERSION = "v1.0";
	
	public static void main(String[] args) {
		
		logger.info("inicializando app");
		Facebook facebook = null;
		Properties props = null;
		
		try {
			props = Utils.loadPropertiesFromFile(CONFIG_DIR, CONFIG_FILE);
		} catch (IOException ex) {	
			System.out.println(ex);
			logger.error(ex);
		}

		int seleccion;
		try (Scanner scanner = new Scanner(System.in)){
			while(true){
				facebook = Utils.configuraFacebook(props);
			
				System.out.format("Simple Facebook client %s\n\n", APP_VERSION);
				System.out.println("Opciones: ");
				System.out.println("(0) Cargar configuracion");
				System.out.println("(1) Obtener Tokens");
				System.out.println("(2) Obtener NewsFeed");
				System.out.println("(3) Obtener Wall");
				System.out.println("(4) Publicar Estado");
				System.out.println("(5) Publicar Link");
				System.out.println("(6) Salir");
				System.out.println("\nPor favor ingrese una opción: ");
		
				try {
					seleccion = scanner.nextInt();
					scanner.nextLine();

					switch(seleccion){
					case 0:
						props = Utils.loadPropertiesFromFile(CONFIG_DIR, CONFIG_FILE);
						break;
					case 1: 
						Utils.obtenerAccessToken(CONFIG_DIR, CONFIG_FILE, props, scanner);
						props = Utils.loadPropertiesFromFile(CONFIG_DIR, CONFIG_FILE);
						break;
						case 2:
							//ResponseList<Post> feed = facebook.getHome();
							break;
						case 3:
							
							break;
						case 4:
							System.out.println("Escribe tu mensaje");
							String mensaje = scanner.nextLine();
							facebook.postStatusMessage(mensaje);
							break;
						case 5:
							System.out.println("Escribe tu url");
							String url = scanner.nextLine();
							facebook.postLink(new URL(url));
							facebook.postLink(new URL(url), "A Java library for the Facebook Graph API");
							break;
						case 6:
							System.exit(0);
						default:
							logger.error("Opción inválida");
							break;
					}
				} catch (InputMismatchException ex){
					System.out.println("Ocurrió un errror, favor de revisar log.");
					logger.error("Opción inválida. %s. \n", ex.getClass());
					scanner.next();
				}catch (Exception ex){
					System.out.println("Ocurrió un errror, favor de revisar log.");
					logger.error(ex);
					scanner.next();
				} 
			}
		} catch (Exception ex){
			logger.error(ex);
		}
	}

}
