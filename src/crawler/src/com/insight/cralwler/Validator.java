package com.insight.cralwler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Validator {

	static String NYSESymbol = "https://www.nyse.com/quote/XNYS:";
	static String NasdaqSymbol = "www.nasdaq.com/symbol/";
	static Properties stock = new Properties();

	
	public static void setSymbolProperties() throws IOException
	{
	InputStream is = new FileInputStream("stockSymbol.properties");
	stock.load(is);
	
	
	}
	
	public static String getTitle(String title)
	{
		title = title.substring(0, title.indexOf(' '));
		title = title.replaceAll("[,'-.]"," ").trim().replaceAll(" +", " ");
		return title;
	}
	
	// Get the basic stock information of the page
	public static JSONObject getStockInfo(Document Doc)
	{
		JSONObject stock = new JSONObject();
		String symbol  = null;
		Elements links = Doc.select("a[href]");
				 	
			for(int i=0 ;i<links.size();i++)
			{

				if(links.get(i).text().equals("NYSE")||links.get(i).text().equals("NASDAQ") )
				{
					symbol  = links.get(i+1).text();
					
					if(validateSymbol(getTitle(Doc.title()), symbol))
					{
						stock.put("StockExchange", links.get(i).text());
						stock.put("Symbol", symbol);
					}
					return stock;
				}
				
				
			}
		return null;
	}
	
	// Validate the symbol on the page to be a geniune Stock symbol related to the webpage
	public static boolean validateSymbol(String title, String symbol)
	{
		String StockName = null;
			
			StockName = stock.getProperty(symbol);
			
		if(StockName==null)
			return false;
		
		
		if(title.toLowerCase().contains(StockName.toLowerCase())||StockName.toLowerCase().contains(title.toLowerCase()))
			return true;
		else
			return false;
	}

	// Validate the link to be a Nasdaq or NYSE page
	public static boolean validateLink(Document Doc)
	{
		if(Doc.toString().contains(NYSESymbol)||Doc.toString().contains(NasdaqSymbol))
			return true;
		else
			return false;			
	}
	
	
	
}
