/*
 * Author: Yash Agarwal
 * CS 210 Spring 21
 * Purpose: This file scrapes a Wikipedia page and goes thorugh the
 * html code on it to return the links on that page
 * 
 * It takes in a String with the page name, converts it to a valid URL,
 * visits it, goes through the html, finds the links on it and returns them
 */
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * TODO: You will have to implement memoization somewhere in this class.
 */
public class WikiScraper {
	
	private static Map<String, Set<String>> memo = new HashMap<>();
			
	/*
	 * This function takes in the page name as a String and returns the links
	 * on that page using the other helper function in this file
	 * param- link- the string storing the name of the current page
	 * return- Set<String>- Set which stores the links on that page
	 */
	public static Set<String> findWikiLinks(String link) {
		// implementation of memoization
		if(memo.containsKey(link)) {
			return memo.get(link);
		}
		// calls the function which returns the HTML on 
		// the current page
		String html = fetchHTML(link);
		// scrapes the HTML stored in html and
		// calls scrapeHTML on it which returns
		// the links on the current page
		Set<String> links = scrapeHTML(html);
		memo.put(link, links);
		return links;
	}
	
	/*
	 * This function takes in the link of the page
	 * to be analyzed. It connects to the internet and
	 * visits the Wikipedia page stored in link finds the HTML
	 * on that page and returns it as a string
	 * param- link- String which stores the URL of the page
	 * return- String- the HTML on that page 
	 */
	private static String fetchHTML(String link) {
		StringBuffer buffer = null;
		try {
			URL url = new URL(getURL(link));
			InputStream is = url.openStream();
			int ptr = 0;
			buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	/*
	 * Helper function which returns the URL by adding
	 * the page name to the specified URL format
	 * param- link- the page name
	 * return- String- the URL of the page
	 */
	private static String getURL(String link) {
		return "https://en.wikipedia.org/wiki/" + link;
	}
	
	/*
	 * This function scrapes the HTML code to return
	 * the valid web pages on that page. The link should not contain
	 * ':' or '#'
	 * param- html- the HTML code provided by the calling function
	 * return- Set<String>- The set containing all the links on that
	 * page
	 */
	private static Set<String> scrapeHTML(String html) {
		Set<String> set = new HashSet<String>();
		int skipLength = 0;
		int counter = 0;
		// the subtraction of 15 is because of the number of characters in 
		// "<a href=\"/wiki/", to avoid a index out of bounds error
		for(int i = 0; i < html.length()-15; i++) {
			if(html.substring(i, i+15).equals("<a href=\"/wiki/")) {
				// if format matches starts to check the following characters
				// to check if it does not contain a : or a #
				char ch = html.charAt(i+15);
				String str = "";
				int count = 0;
				while(html.charAt(i+count+15) != '"'){
					if(html.charAt(i+count+15) == ':' || html.charAt(i+count+15) == '#') {
						str = "";
						break;
					}
					ch = html.charAt(count+i+15);
					str += ch;
					count++;
			}
				// adds if the page name is not empty
				if(str != "")
				set.add(str);
				skipLength = str.length();
				count = 0;
		}
			i += skipLength;
			skipLength = 0;
			counter++;
	
	}
	return set;
}
}
