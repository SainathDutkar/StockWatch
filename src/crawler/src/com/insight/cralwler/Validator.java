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
	
	
	public static boolean validateSymbol(String title, String symbol)
	{
		String StockName = null;
			
			StockName = stock.getProperty(symbol);
			System.out.println(title);
			System.out.println(symbol);
			System.out.println(StockName);
			
		if(StockName==null)
			return false;
		
		
		if(title.toLowerCase().contains(StockName.toLowerCase())||StockName.toLowerCase().contains(title.toLowerCase()))
			return true;
		else
			return false;
	}

	
	public static boolean validateLink(Document Doc)
	{
		if(Doc.toString().contains(NYSESymbol)||Doc.toString().contains(NasdaqSymbol))
			return true;
		else
			return false;			
	}
	
	
	
}
