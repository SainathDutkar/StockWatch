package com.insight.cralwler;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
		// Load the stock symbols from property file
			Validator.setSymbolProperties();
		  	int InitialQueueSize = Crawler.getQueueSize();
		  	int count = 0;
		  	String seedurl;
		  	while((seedurl = Crawler.getURL())!=null)
		  	{
		  		
		 		//Get the Jsoup document of the wikipedia page
				doc = JsonHandler.getUrlDoc(seedurl);
				
				if(doc== null)
					continue;
				urlText = doc.toString();
				
				// Validate the wikipedia page to be geniune Nasdaq or NYSE page
				if(Validator.validateLink(doc))
				{
				
					Crawler.addtolist(seedurl);
				
					JSONObject StockInfo = Validator.getStockInfo(doc);
			
					wikiInfo =JsonHandler.getWikiInfo(doc);
					wikiHistory = JsonHandler.getWikiHistory(doc.title());
					
					
					// Write the page informationa and History to JSON file post validation
					if(wikiInfo!=null && wikiHistory!=null && wikiInfo.containsKey("StockExchange"))
					{
					
						JsonHandler.writeJsonFile(seedurl,wikiInfo,wikiHistory,StockInfo);
						JsonHandler.writeRawData(doc);
					}
					
				
				}
			//To limit the depth of the clawer only till Seeds Urls
				if(count < InitialQueueSize)
				{
			
					Crawler.addURL(URLExtractor.getURLJsoup(doc));
					count++;
					
				}
			
		  	}
		  	
		  	System.out.println("Completed");

	
			
	  }
			
	}

