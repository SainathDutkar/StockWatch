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
		  	Validator.setSymbolProperties();
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
				
				if(Validator.validateLink(doc))
				{
				
					Crawler.addtolist(seedurl);
					System.out.println("Marked "+Crawler.markedcount());
					System.out.println("Processing :"+doc.title());
					JSONObject StockInfo = Validator.getStockInfo(doc);
					System.out.println("Stock Info"+StockInfo);
					wikiInfo =JsonHandler.getWikiInfo(doc);
					wikiHistory = JsonHandler.getWikiHistory(doc.title());
					
					
				//	System.out.println(StockInfo.isEmpty());
					//if(StockInfo!=null)
					if(wikiInfo!=null && wikiHistory!=null && wikiInfo.containsKey("StockExchange"))
					{
						System.out.println("Saving   "+doc.title());
						JsonHandler.writeJsonFile(seedurl,wikiInfo,wikiHistory,StockInfo);
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

