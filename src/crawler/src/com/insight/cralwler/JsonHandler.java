package com.insight.cralwler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class JsonHandler {

	static String path,StockExchange,Symbol ;

	
	
	public static Document getUrlDoc(String url)
	{
		Document doc;
		
		try {
			
			doc = Jsoup.connect(url).get();
		    
		   } catch (IOException e) {
			return null;
		}
		return doc;	
	}
	
	
	
	
	public static HashMap<String,String> getWikiInfo(Document doc)
	{
		HashMap<String, String>wikiInfo = new HashMap<>(); 
		Elements companyInfo = doc.getElementsByClass("infobox vcard");
		
         Element table = companyInfo.first();
         String title = doc.title();
         wikiInfo.put("Name", title.substring(0, title.length()-12));
         
         try
         {
          Elements rows = table.select("tr");
          
          for (Element row : rows)
          {
        	  if(row.select("td").text().contains("NYSE:") ||row.select("td").text().contains("NASDAQ:"))
        	  {
        		 String rowData = row.select("td").text();
        		 
        		 if(rowData.contains("NYSE")){
        		  wikiInfo.put("StockExchange","NYSE");
        		  Symbol = rowData.substring(rowData.indexOf("NYSE:")+6,rowData.length());}
        		 else {
        			 wikiInfo.put("StockExchange","NASDAQ");
        			 Symbol = rowData.substring(rowData.indexOf("NASDAQ:")+8,rowData.length());}
        		 
        		// Symbol = rowData.substring(rowData.indexOf(':')+2,rowData.length());
        		
        		 if(Symbol.replaceAll("[0-9]","").replaceAll("]","").replaceAll("\\p{P}","").length()>5)
        			 Symbol = Symbol.substring(0, Symbol.indexOf(' ')).replaceAll("[^a-zA-Z0-9]","");
        		// Symbol = rowData.substring(rowData.indexOf(':')+2,rowData.indexOf(' ',rowData.indexOf(' ')+1));
        		 
        		// System.out.println(Symbol);
        		 wikiInfo.put("Symbol",Symbol.trim());	 
        	  }
        	  else
        	  {
        	  wikiInfo.put(row.select("th").text(), row.select("td").text());
        	  }
          }
         }
         catch(NullPointerException e)
         {
        	 return null;
         }
		return wikiInfo;
	}
	
	public static JSONArray getWikiHistory(String title)
	{
		JSONArray history = new JSONArray();
		
        String Name = title.substring(0, title.length()-12);
         
        String url ="https://en.wikipedia.org/w/index.php?title=";
       Document doc;
	try {
		doc = Jsoup.connect(url+Name+"&action=history").get();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		return null;
	}
         Elements pageHistory = doc.getElementsByAttributeValue("id", "pagehistory").get(0).getElementsByTag("li");
         int length = pageHistory.size();
         if(length > 10) {length = 10;}
        	 
         for(int i=0 ; i<pageHistory.size();i++ )
         {
         
         	Elements Links  =  pageHistory.get(i).select("a[href]");
         	JSONObject historyData = new JSONObject();
         	//for(Element link : Links)
         	for(int j=0 ;j<Links.size();j++)
         	{
         		
         	if(Links.get(j).text().equals("prev"))
         	{
         		historyData.put("prev",Links.get(j).attr("abs:href").toString());
         		historyData.put("editTime", Links.get(j+1).text());
         		
         		historyData.put("editLink", Links.get(j+1).attr("abs:href").toString());
         	
         		historyData.put("user",Links.get(j+2).text());
         		historyData.put("userLink",Links.get(j+2).attr("abs:href").toString());	
         	}
        		
         	}
         		
         	history.add(historyData);	
       
         	
         	}	
		return history;
	}
	
	public static boolean writeJsonFile(String seedURL,HashMap<String, String> wikiInfo, JSONArray wikiHistory )
	{
		
		JSONObject wikiJson = new JSONObject();
		
		for(String key : wikiInfo.keySet())
		{
			if(key!=null)
			wikiJson.put(key, wikiInfo.get(key));
		}
		wikiJson.put("PageHistory",wikiHistory);
		wikiJson.put("WikiLink", seedURL);
		path = "output/"+wikiInfo.get("Name")+".json";
		
		try (FileWriter file = new FileWriter(path)) {

            file.write(wikiJson.toJSONString());
            file.flush();

        } catch (IOException e) {
            return false;
        }
		return true;
	}
	
	
	public static boolean writeRawData(Document doc)
	{
		
		path = "rawFiles/"+doc.title()+".html";
		
		try (FileWriter file = new FileWriter(path)) {

            file.write(doc.toString());
            file.flush();

        } catch (IOException e) {
            return false;
        }
		return true;
	}
	
	
	
	public static boolean writeJsonFileWithHistory(HashMap<String, String> wikiInfo, HashMap<String, String> wikiHistory )
	{
		JSONObject wikiJson = new JSONObject();
		
		for(String key : wikiInfo.keySet())
		{
			wikiJson.put(key, wikiInfo.get(key));
		}
		
		for(String key : wikiHistory.keySet())
		{
			wikiJson.put(key, wikiHistory.get(key));
		}
		
	//	wikiJson.put("PageHistory",wikiHistory);
		
		path = "output/"+wikiInfo.get("Name")+".json";
		
		try (FileWriter file = new FileWriter(path)) {

            file.write(wikiJson.toJSONString());
            file.flush();

        } catch (IOException e) {
            return false;
        }
		return true;
	}
	
	
	public static HashMap<String, String> getTopWikiHistory(String title)
	{
		HashMap<String, String> history = new HashMap<>();
		
        String Name = title.substring(0, title.length()-12);
         
        String url ="https://en.wikipedia.org/w/index.php?title=";
       Document doc;
	try {
		doc = Jsoup.connect(url+Name+"&action=history").get();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		return null;
	}
         Elements pageHistory = doc.getElementsByAttributeValue("id", "pagehistory").get(0).getElementsByTag("li");
     
         
         int size;
         if(pageHistory.size()<4)
        	 size = pageHistory.size();
         else 
        	 size = 4;
         
         for(int i=0; i<size;i++)
         {
  
         	Elements Links  =  pageHistory.get(i).select("a[href]");
         	JSONObject historyData = new JSONObject();
         
         	for(int j=0 ;j<Links.size();j++)
         	{
         		
         	if(Links.get(j).text().equals("prev"))
         	{
         		history.put("editTime"+i, Links.get(j+1).text());
         		
         		history.put("editLink"+i, Links.get(j+1).attr("abs:href").toString());
         	
         		history.put("user"+i,Links.get(j+2).text());
         		history.put("userLink"+i,Links.get(j+2).attr("abs:href").toString());
         		
         	}
        		
         	}
         
         }
		
		return history;
	}
}