package servicewall.engine;

import servicewall.modules.RecentModified;
import java.io.IOException;
import java.util.ArrayList;

public class SystemFilesMonitor implements Runnable {

    private RecentModified recentModifiedObject = new RecentModified();
    private ArrayList<String> cacheRecentModified = new ArrayList<String>();

    private void printModifiedFiles(ArrayList<String> pr) {

        for (String x : pr) {
                System.out.println("[system-files-monitor] " + x);
                this.cacheRecentModified.add(x);
        }

    }

    public ArrayList<String> getModifiedFiles() throws IOException {

        ArrayList<String> modifiedFilesList = recentModifiedObject.getRecentlyModifiedFiles(0.5f, "/home");
        this.printModifiedFiles(modifiedFilesList);
        return modifiedFilesList;

    }

    public void run() {
        System.out.println("\n[running system-files-monitor thread ...]");

        try {
            while (true) {
                this.getModifiedFiles();
                Thread.sleep(30000);
            }
        } catch (Exception e) {}
    }

}
