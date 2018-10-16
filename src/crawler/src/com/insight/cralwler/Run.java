package com.insight.cralwler;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.jsoup.nodes.Document;

import com.insight.cralwler.Crawler;
import com.insight.cralwler.URLExtractor;

public class Run {


	  public static void main(String[] args) throws Exception
	  {		
		  	System.out.println("Execution started");
		  	Document doc;
		  	String urlText;
		  	HashMap<String, String> wikiInfo;
		  	JSONArray wikiHistory;
			
		  	// Load the seed URLS from the property File 
		  	Crawler.loadPropertyfile();
		  	int InitialQueueSize = Crawler.getQueueSize();
		  	int count = 0;
		  	String seedurl;
		  	while((seedurl = Crawler.getURL())!=null)
		  	{
		  		System.out.println(Crawler.getQueueSize());
		 		
				doc = JsonHandler.getUrlDoc(seedurl);
				
				if(doc== null)
					continue;
				urlText = doc.toString();
				//System.out.println(doc.title());
				if(urlText.contains("www.nyse.com/quote")||urlText.contains("www.nasdaq.com/symbol/") )
				{
				
					Crawler.addtolist(seedurl);
				//	System.out.println(Crawler.markedcount());
					
					wikiInfo =JsonHandler.getWikiInfo(doc);
					wikiHistory = JsonHandler.getWikiHistory(doc.title());
					
					if(wikiInfo!=null && wikiHistory!=null && wikiInfo.containsKey("StockExchange"))
					{
						System.out.println("Saving   "+doc.title());
						JsonHandler.writeJsonFile(seedurl,wikiInfo,wikiHistory);
						JsonHandler.writeRawData(doc);
					}
					
				//		JsonHandler.writeJsonFileWithHistory(JsonHandler.getWikiInfo(doc), JsonHandler.getTopWikiHistory(doc.title()));
					
				}
			//To limit the depth of the clawer only till Seeds Urls
				if(count < InitialQueueSize)
				{
				//	System.out.println(count);
					Crawler.addURL(URLExtractor.getURLJsoup(doc));
					count++;
					
				}
			
		  	}
		  	
		  	System.out.println("Completed");

	
			
	  }
			
	}

