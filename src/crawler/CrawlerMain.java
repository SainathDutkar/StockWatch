package crawler;

public class CrawlerMain {


	  public static void main(String[] args) throws Exception
	  {		
			Crawler.loadPropertyfile();
			
		while(Crawler.markedcount()<100)
		{
			String seedurl = Crawler.getURL();
			StringBuilder urlText = new StringBuilder();
			urlText = URLExtractor.getURLTEXT(seedurl);
			if(Validator.verifyReltedTerms(urlText.toString(), Crawler.getRelatedWords()))
			{
				Crawler.addtolist(seedurl);
				
				Crawler.saveCrawledPage(URLExtractor.geturlName(seedurl).toLowerCase(),urlText.toString());
			}
			
			Crawler.addURL(URLExtractor.extractUrls(urlText));
		}	
		Crawler.printer();	
		Crawler.writetoFile();	
		
	  }
			
	}

