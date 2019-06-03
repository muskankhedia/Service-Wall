package servicewall.modules;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import servicewall.services.Http;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ProcessVerify_Internet {

    private final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    private final short SEARCH_QUERY_NUMBER = 20;
    private final Http HttpObject = new Http();
    private final String GOOGLE_QUERY_PREFIX = "https://www.google.com/search";
    protected final String[] maliciousKeywords = {" harm ", " harmful ", " danger ", " malicious ", " malware ", " ransomware "};
    protected final String[] supportingKeywords = {"definitely", "surely", "obviously"};
    private String textResult;
    boolean check = false;     //it may or may not be suspicious, so initialized as false
    boolean check2 = false;   //it is not definitely malicious
    private int count = 0;

    public boolean verifyIfMaliciousProcessThroughGoogleCheck(String processName) throws IOException {

        final boolean isMalicious = verifyTheProcess(processName);
        System.out.println("isMalicious:: "+ isMalicious);
        return isMalicious;

    }

    public boolean verifyTheProcess(String process) throws IOException {

        String searchURL = GOOGLE_SEARCH_URL + "?q=" + process + "+virus" + "&num=" + this.SEARCH_QUERY_NUMBER;
        // without proper User-Agent, we will get 403 error
        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
        String data = doc.text();

        if (data.contains("Search instead for") || data.contains("Did you mean")) {

            return true;

        } else {
            ArrayList<LinkedHashMap<String, String>> googleResults = this.HttpObject.fetchFromGoogleSearch(process);
            for (LinkedHashMap<String, String> resultQuery : googleResults) {
                String text = resultQuery.get("text");
                String link = resultQuery.get("link");
                check = false;      //initialized as false in every loop
                                    // as it will be true if any malicious keyword will be found

                try {
                    System.out.println("done");
                    doc = Jsoup.connect(link).userAgent("Mozilla/5.0").get();
                    String textContent=doc.text();
                    List<String> list = new ArrayList<String>(Arrays.asList(textContent.split(" . ")));
                    for (String temp : list) {
                        for (String malWord : this.maliciousKeywords) {
                            if (temp.contains(malWord) && containsIgnoreCase(temp, process)) {
                                System.out.println("malWord:: " + malWord);
                                System.out.println("temp:: " + temp);
                                check = true;
                                String[] words = temp.split(" ");
                                for (String word : words) {
                                    if (word == "not" || word == "no" || word == "never" || word == "nothing") {
                                        if (!check) {
                                            check = true;
                                            System.out.println("temp:: " + temp);
                                        } else check = false;
                                    }
                                }
                                if (check) count++;
                                for (String supWord : this.supportingKeywords) {
                                    if (temp.contains(supWord)) {
                                        check2 = true;
                                    }
                                }
                                if ((check && check2)) {
                                    count++;
                                }
                            }
                        }
                    }
                }
                catch (Exception e ) {
                    System.out.println("error");
                }
            }

            System.out.println("count:: "+ count);
            if (count > 0 ) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }

    public static void main(String[] args) throws IOException {

        ProcessVerify_Internet p = new ProcessVerify_Internet();
        final boolean isMalicious = p.verifyTheProcess("systemd");
        System.out.println("isMalicious:: "+ isMalicious);
    }

}
