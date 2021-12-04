package fr.ezzud.castlewar.methods;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class checkUpdate {
	public static void github(String version) throws IOException, InvalidConfigurationException {
		URL url;
		try {
			url = new URL("https://raw.githubusercontent.com/Ezzud/castlewars/main/plugin.yml");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int status = con.getResponseCode();
			if (status > 299) {
			    new InputStreamReader(con.getErrorStream());
			}
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
					YamlConfiguration config = new YamlConfiguration();
					config.loadFromString(content.toString());
					System.out.println(config.getString("version"));
					in.close();
					con.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		

		
		
	}
	public static String getTextFromGithub(String link) {
	    URL Url = null;
	    try {
	        Url = new URL(link);
	    } catch (MalformedURLException e1) {
	        e1.printStackTrace();
	    }
	    HttpURLConnection Http = null;
	    try {
	        Http = (HttpURLConnection) Url.openConnection();
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
	    Map<String, List<String>> Header = Http.getHeaderFields();
	    
	    for (String header : Header.get(null)) {
	        if (header.contains(" 302 ") || header.contains(" 301 ")) {
	            link = Header.get("Location").get(0);
	            try {
	                Url = new URL(link);
	            } catch (MalformedURLException e) {
	                e.printStackTrace();
	            }
	            try {
	                Http = (HttpURLConnection) Url.openConnection();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            Header = Http.getHeaderFields();
	        }
	    }
	    InputStream Stream = null;
	    try {
	        Stream = Http.getInputStream();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    String Response = null;
	    try {
	        Response = GetStringFromStream(Stream);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return Response;
	}

	private static String GetStringFromStream(InputStream Stream) throws IOException {
	    if (Stream != null) {
	        Writer Writer = new StringWriter();

	        char[] Buffer = new char[2048];
	        try {
	            Reader Reader = new BufferedReader(new InputStreamReader(Stream, "UTF-8"));
	            int counter;
	            while ((counter = Reader.read(Buffer)) != -1) {
	                Writer.write(Buffer, 0, counter);
	            }
	            Reader.close();
	        } finally {
	            Stream.close();
	        }
	        return Writer.toString();
	    } else {
	        return "No Contents";
	    }
	}
}
