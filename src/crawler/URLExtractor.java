package crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLExtractor {

	public static StringBuilder getURLTEXT(String urlseed)throws Exception
	{
		String text;
		URL wiki = new URL(urlseed);
		BufferedReader br = new BufferedReader(new InputStreamReader(wiki.openStream()));
		StringBuilder urls = new StringBuilder();
		while((text=br.readLine())!= null)
		{
		 urls.append(text);
		}
		 
		br.close();
		return urls;
	}
	
	
	public static List<String> extractUrls(StringBuilder urls)
	{
	    List<String> containedUrls = new ArrayList<String>();
	    Pattern pattern = Pattern.compile("href=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
	    Matcher urlMatcher = pattern.matcher(urls);
	    String tempString = null;
	    
	    while (urlMatcher.find())
	    {
	    	tempString = urls.substring(urlMatcher.start(0),urlMatcher.end(0));
	    	if(tempString.startsWith("href=\"/wiki/")){
	    		tempString= tempString.substring(6,tempString.length()-1);
	    		tempString = "https://en.wikipedia.org"+tempString;
	    		containedUrls.add(tempString);
	    	}
	    	else if(tempString.startsWith("href=\"https://en.wikipedia.org/wiki/"))
	    	{
	    		tempString= tempString.substring(6,tempString.length()-1);
	    		containedUrls.add(tempString);
	    	}
	    }

	    return containedUrls;
	}
	
	public static String geturlName(String seedURL)
	{
		String tempString = seedURL;
		tempString= tempString.substring(30,tempString.length());
		tempString = tempString.replaceAll("[^a-zA-Z0-9]+", "");
		return tempString;
	}
	
}
