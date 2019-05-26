package servicewall.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Http {

    private final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    private final short SEARCH_QUERY_NUMBER = 20;

    // respond to google request with the help of Jsoup
    public ArrayList<LinkedHashMap<String, String>> fetchFromGoogleSearch(String query) throws IOException, NullPointerException {

        String searchTerm = query;
        int num = SEARCH_QUERY_NUMBER;

        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm + "&num=" + this.SEARCH_QUERY_NUMBER;
        //without proper User-Agent, we will get 403 error
        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
//        Elements results = doc.select("h3.r > a");
        Elements results = doc.select("div[class=jfp3ef] > a");
        ArrayList<LinkedHashMap<String, String>> response = new ArrayList<LinkedHashMap<String, String>>();
        for (Element result : results) {
            LinkedHashMap<String, String> searchedResults = new LinkedHashMap<String, String>();
            String linkHref = result.attr("href").substring(6);
            String linkText = result.text();
            searchedResults.put("text", linkText);
            searchedResults.put("link", linkHref);
            response.add(searchedResults);
        }
        return response;

    }

    public static void main(String[] args) throws IOException {
        System.out.println(new Http().fetchFromGoogleSearch("Harkishen"));
    }

}
