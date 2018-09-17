package crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	public static boolean hasRelatedTerms(String urlText, String[] relatedTerms,String url) // OLD CODE - Validating using string
	{
		int count = 0;
		String tempURL = urlText.toLowerCase();
		for(int i=0; i<relatedTerms.length;i++)
		{
			if(tempURL.contains((relatedTerms[i]).toLowerCase()))
			{
			//	System.out.println("Word match "+relatedTerms[i].toLowerCase());
				count++;
			}
		}
		
		if(count>=3)
		{
		return true;
		}
		else {
		return false;
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
