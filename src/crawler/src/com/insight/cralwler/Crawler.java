package com.insight.cralwler;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {

	private static Queue<String> queue = new LinkedList<String>();
	private static List<String> visitedURL = new ArrayList<String>();
	private static List<String> related = new ArrayList<String>(); 
	private static List<String> savedPages = new ArrayList<String>();
	static int visitedcount,pagecount = 0;
	
	// To get the queue size
	public static int getQueueSize()
	{
		return queue.size();
	}
	
	
	// To get the new URL from the queue 
	public static String getURL()
	{	
		String url = null;
		if(!queue.isEmpty())
		{
		//	System.out.println("Size of Queue "+queue.size());
			do
			{
		     url =  queue.poll();
			}while(isMarked(url));
		return url;			
		}
		else
		{
			return null;
		}
	}
	
	// To add the new extracted URLs to the list 
	public static void addURL(List<String> url)
	{
		Iterator<String> ir = url.iterator();
		while(ir.hasNext())
		queue.add(ir.next());	
	}
	
	
	// To verify if the new URL is already saved 
	public static boolean isMarked (String url)
	{
		if(visitedURL.contains(url))
			return true;
		else 
			return false;
		
	}
	
	// to add the url to the visited URLs to list
	public static void addtolist(String url)
	{
		visitedcount++;
	//	System.out.println("Added"+visitedcount+" : "+url);
		visitedURL.add(url);
	}
	
	
	public static void addRelatedwords(String relatedword)
	{
		related.add(relatedword);
	}
	
	public static String[] getRelatedWords()
	{
		String[] relatedwords = new String[related.size()];
		relatedwords = related.toArray(relatedwords);
		return relatedwords;
	}
	
	
	// to get the Seed URL and related words from the property file
	public static void loadPropertyfile() throws IOException
	{
		Properties prop = new Properties();
		InputStream is = new FileInputStream("config.properties");
		prop.load(is);
		
		List<String> url = new ArrayList<String>();
		
		String nyseUrl = prop.getProperty("seed1");
		for(char c='A';c<='Z';c++)
		{
			url.add(nyseUrl+c+")");
		}
		
		for(int i=2 ;i<=7;i++)
		{
			url.add(prop.getProperty("seed"+i));
		}
		

		
	
		addURL(url);
		
		for(int i=0;i<6;i++)
		{
			addRelatedwords(prop.getProperty("relatedTerm"+i));
		}
	}
	
	public static void printer()    /*created for testing purpose*/
	{
		System.out.println("Visited URLS size "+visitedURL.size());

	}
	
	public static int markedcount()
	{
		return visitedURL.size();
	}
	
	// to create a list of all visited URLs in the text file 
	public static void writetoFile()
	{
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	
		try  (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("VisitedURLs_"+timeStamp+".txt"), "utf-8"))) {

			Iterator<String> ir = visitedURL.iterator();
			while(ir.hasNext())
			{
				writer.write(ir.next());
				((BufferedWriter) writer).newLine();
			}

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
	
	
	
	public static boolean verifyReltedTerms(String urlText,String[] relatedTerms) // New function validating using regex pattern matching
	{
	    int count =0; 
		for(int i=0; i<relatedTerms.length;i++)
		{
			Pattern pattern = Pattern.compile(relatedTerms[i], Pattern.CASE_INSENSITIVE);
		    Matcher urlMatcher = pattern.matcher(urlText);
		    if(urlMatcher.find())
		    {
		    	count++;
		    }
		    if(count>=3)
		    {
		    	return true;
		    }
		}
		return false;
	}
		
}
