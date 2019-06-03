package servicewall.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class RecentModified {

    private final String[] cmd = {"find", "", "-cmin", ""};
    public String username = "";
    private final String separator = "%%%";

    public ArrayList<String> getRecentlyModifiedFiles(float timeInMinutes, String path) throws IOException, NullPointerException {

        this.cmd[1] = path;
        this.cmd[3] = String.valueOf(timeInMinutes);
        ProcessBuilder recentFilesProcess = new ProcessBuilder(this.cmd);
        Process runners = recentFilesProcess.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(runners.getInputStream()));
        String line = br.readLine(), lines = "";
        while(true) {
            try {
                line.trim().equals(""); // will throw NullPointerException if input stream ends
            } catch (NullPointerException e) {
                break;
            }
            lines += line + this.separator;
            line = br.readLine();
        }
        String[] inArr = lines.split(this.separator);
        ArrayList<String> t = new ArrayList<String>();
        for (String ele : inArr) {
            if (!ele.trim().equals(""))
                t.add(ele);
        }
        return t;

    }

}
