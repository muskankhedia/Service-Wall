package servicewall.modules;

import servicewall.services.Http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ProcessVerify_Internet {

    private final Http HttpObject = new Http();
    private final String GOOGLE_QUERY_PREFIX = "https://www.google.com/search";
    protected final String[] maliciousKeywords = {"harm", "harmful", "danger", "malicious", "malware"};
    private String textResult;

    public boolean verifyProcessThroughGoogleCheck(String processName) throws IOException {

        ArrayList<LinkedHashMap<String, String>> googleResults = this.HttpObject.fetchFromGoogleSearch(processName);
        for (LinkedHashMap<String, String> resultQuery : googleResults) {
            String text = resultQuery.get("text");
            for (String malWord : this.maliciousKeywords) {
                if (text.contains(malWord)) {
                    return false;
                }
            }
        }
        return true;

    }

}
