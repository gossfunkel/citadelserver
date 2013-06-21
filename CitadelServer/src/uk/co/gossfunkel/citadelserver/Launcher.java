package uk.co.gossfunkel.citadelserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Launcher {
	
	public static final String VERSION = "0_2";
	private static final String VERSION_URL = "http://www.gossfunkel.co.uk/citadel/serverVersion";
	private static final String HISTORY_URL = "http://www.gossfunkel.co.uk/citadel/serverHistory";
	
	public static void main(String[] args) {
		System.out.println(getLatestVersion() + "/" + VERSION);
		//Server server = new Server();
		//server.start();
	}
	
	public static String getLatestVersion() {
		String str = getHttpData(VERSION_URL);
		if (str.length() > 3)
			return str.substring(0, 3);
		else return str;
	}
	
	public static String getWhatsNew() {
		return getHttpData(HISTORY_URL);
	}
	
	private static String getHttpData(String address) {
		StringBuffer buffer = new StringBuffer("");
		try {
			URL url = new URL(address);
			InputStream is = url.openStream();
			int c = 0;
			while(c != -1) {
				c = is.read();
				buffer.append((char)c);
			}
		} catch (MalformedURLException e1) {
			System.err.println("Updater URL incorrect");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	

}
