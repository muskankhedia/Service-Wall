package servicewall.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Http {

    private final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    private final short SEARCH_QUERY_NUMBER = 20;

    // respond to google request with the help of Jsoup
    public ArrayList<Map<String, String>> fetchFromGoogleSearch(String query) throws IOException, NullPointerException {

        String searchTerm = query;
        int num = SEARCH_QUERY_NUMBER;

        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;
        //without proper User-Agent, we will get 403 error
        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

        Elements results = doc.select("h3.r > a");
        ArrayList<Map<String, String>> response = new ArrayList<Map<String, String>>();

        for (Element result : results) {
            HashMap<String, String> searchedResults = new HashMap<String, String>();
            String linkHref = result.attr("href");
            String linkText = result.text();
            searchedResults.put("text", linkText);
            searchedResults.put("link", linkHref.substring(6, linkHref.indexOf("&")).substring(1));
            System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(6, linkHref.indexOf("&")));
            response.add(searchedResults);
        }
        System.out.println(response);
        return response;

    }

}
