package servicewall.modules;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.BufferedReader;

public class NetworkHandle {

    private final String[] CMD_NETWORK_HANDLE_PROCESS = {"netstat", "-tanp"};
    private final String LINE_SEPERATOR = "%%%";

    public ArrayList<LinkedHashMap<String, String>> getNetworkUsageOfRunningProcess() throws IOException {
        ProcessBuilder processFetchProcesses = new ProcessBuilder(this.CMD_NETWORK_HANDLE_PROCESS);
        Process currently = processFetchProcesses.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(currently.getInputStream()));

        // skip first line
        String lines = br.readLine(), allLines = "";
        //skip second line
        lines = br.readLine();
        short c = 0;
        boolean resumeBuffer = true, miss = false;

        try {
            while (true) {
                lines = br.readLine();
                if (lines.trim().equals("")) {
                    break;
                }
                allLines += lines + this.LINE_SEPERATOR;
            }
        } catch (NullPointerException e) {;}

        String[] diffLines = allLines.split(this.LINE_SEPERATOR);
        ArrayList<LinkedHashMap<String, String>> pureCmds = new ArrayList<LinkedHashMap<String, String>>();

        for (String i : diffLines) {
            String[] cmds = i.split(" ");

            LinkedHashMap<String, String> cmdsWithTags = new LinkedHashMap<String, String>();
            short count = 0;
            for (String j : cmds) {
                if (!j.trim().equals("")) {
                    ++count;
                    switch (count) {
                        case 1:
                            cmdsWithTags.put("Proto", j);
                            break;
                        case 2:
                            cmdsWithTags.put("Recv-Q", j);
                            break;
                        case 3:
                            cmdsWithTags.put("Send-Q", j);
                            break;
                        case 4:
                            cmdsWithTags.put("Local Address", j);
                            break;
                        case 5:
                            cmdsWithTags.put("Foreign Address", j);
                            break;
                        case 6:
                            cmdsWithTags.put("State", j);
                            break;
                        case 7:
                            cmdsWithTags.put("PID/Process name", j);
                            break;
                        default:
                            System.err.printf("%s", "out of fields");
                    }
                }
            }
            pureCmds.add(cmdsWithTags);
        }
        return pureCmds;

    }

    public static void main(String[] args) throws IOException {
        System.out.println(new NetworkHandle().getNetworkUsageOfRunningProcess());
    }
}
