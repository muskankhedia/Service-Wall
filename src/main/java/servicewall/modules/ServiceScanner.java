package servicewall.modules;

import servicewall.modules.Processes;
import servicewall.services.Http;
import servicewall.services.SystemConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ServiceScanner {

    private final Processes ProcessesObject = new Processes();
    private final SystemConfig SystemConfigObject = new SystemConfig();
    public ArrayList<LinkedHashMap<String, String>> currentProcesses;
    public ArrayList<LinkedHashMap<String, String>> NON_ROOT_PROCESSES;

    protected void fetchCurrentProcesses() throws IOException {

        // fetch current processes
        this.currentProcesses = ProcessesObject.getCurrentlyRunningProcesses();

    }

    protected ArrayList<LinkedHashMap<String, String>> filterRootProcesses(ArrayList<LinkedHashMap<String, String>> m) {
        // currently, we assume the root processes to be authorized and free from all restrictions

        ArrayList<LinkedHashMap<String, String>> filtered = new ArrayList<LinkedHashMap<String, String>>();

        for(int i=0; i< m.size(); i++) {
            LinkedHashMap<String, String> process = m.get(i);
            if (!process.get("user").equals("root")) {
                filtered.add(process);
            }
        }
        this.NON_ROOT_PROCESSES = filtered;
        return filtered;

    }

    public boolean UpdateVariables() throws IOException {

        this.fetchCurrentProcesses();
        this.filterRootProcesses(this.currentProcesses);

        return true;

    }

}
