package crawler;

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

public class Crawler {
	
	private static Queue<String> queue = new LinkedList<String>();
	private static List<String> visitedURL = new ArrayList<String>();
	private static List<String> related = new ArrayList<String>(); 
	private static List<String> savedPages = new ArrayList<String>();
	static int visitedcount,pagecount = 0;
	
	public static String getURL()
	{
		
		String url = null;
		if(!queue.isEmpty())
		{
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
	
	public static void addURL(List<String> url)
	{
		Iterator<String> ir = url.iterator();
		while(ir.hasNext())
		queue.add(ir.next());	
	}
	
	public static boolean isMarked (String url)
	{
		if(visitedURL.contains(url))
			return true;
		else 
			return false;
		
	}
	
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
	
	public static void loadPropertyfile() throws IOException
	{
		Properties prop = new Properties();
		InputStream is = new FileInputStream("config.properties");
		prop.load(is);
		
		List<String> url = new ArrayList<String>();
		url.add(prop.getProperty("seed1"));
		url.add(prop.getProperty("seed2"));
		addURL(url);
		for(int i=0;i<7;i++)
		{
			addRelatedwords(prop.getProperty("relatedTerm"+i));
		}
	}
	
	public static void printer()    /*created for testing purpose*/
	{
		System.out.println("Visited URLS size "+visitedURL.size());
	/*	Iterator<String> ir = visitedURL.iterator();
		while(ir.hasNext())
		{
			System.out.println("Printing from printer "+ir.next());
		}
		*/
	}
	
	public static int markedcount()
	{
		return visitedURL.size();
	}
	
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
	
	public static void saveCrawledPage(String URLNAME,String URLTEXT)
	{
		pagecount++;
		
		
		if(savedPages.contains(URLNAME))
		{
			URLNAME = URLNAME+"extension";
		}else if(URLNAME.equals("aux")||URLNAME.equals("con")||URLNAME.equals("prn")||URLNAME.equals("nul"))
		{
			URLNAME = URLNAME+"extension";
		}
		savedPages.add(URLNAME);
	//	System.out.println(URLNAME);
		
		try  (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(URLNAME+".html"), "utf-8"))) {

				writer.write(URLTEXT);

		} catch (IOException e) {

			e.printStackTrace();

		}
		
	}
	
	
}
