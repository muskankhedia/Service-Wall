package servicewall.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class RecentModified {

    private final String[] cmd = {"find", "/", "-cmin", ""};
    private final String separator = "%%%";

    public ArrayList<String> getRecentlyModifiedFiles(int timeInMinutes) throws IOException, NullPointerException, InterruptedException {

        this.cmd[3] = String.valueOf(timeInMinutes);
        System.out.println(Arrays.toString(this.cmd));
        ProcessBuilder recentFilesProcess = new ProcessBuilder(this.cmd);
        Process runners = recentFilesProcess.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(runners.getInputStream()));
        String line = br.readLine(), lines = "";
        System.out.println(line);
        while(true) {
            try {
                line.trim().equals(""); // will throw NullPointerException if input stream ends
            } catch (NullPointerException e) {
                break;
            }
            System.out.println(line);
            lines += line + this.separator;
            line = br.readLine();
        }
        String[] inArr = lines.split(this.separator);
        ArrayList<String> t = new ArrayList<String>();
        for (String ele : inArr) {
            if (!ele.trim().equals(""))
                t.add(ele);
        }
        System.out.println(t);
        return t;

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new RecentModified().getRecentlyModifiedFiles(-2);
    }

}
