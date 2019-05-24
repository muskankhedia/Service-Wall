package servicewall.modules;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.BufferedReader;

public class Processes {

    private final String[] CMD_CURRENTLY_RUNNING_PROCESS = {"top", "-b", "-n", "1"};
    private final String LINE_SEPERATOR = "%%%";

    public ArrayList<LinkedHashMap<String, String>> getCurrentlyRunningProcesses() throws IOException {
        ProcessBuilder processFetchProcesses = new ProcessBuilder(this.CMD_CURRENTLY_RUNNING_PROCESS);
        Process currently = processFetchProcesses.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(currently.getInputStream()));

        // skip first line
        String lines = br.readLine(), allLines = "";
        short c = 0;
        boolean resumeBuffer = false, miss = true;

        try {
            while (true) {
                lines = br.readLine();
                if (lines.trim().equals("")) {
                    resumeBuffer = !resumeBuffer;
                    c++;
                    if (c == 1)
                        continue;
                    else if (c == 2)
                        break;
                }
                if (resumeBuffer && !miss) {
                    allLines += lines + this.LINE_SEPERATOR;
                } else
                    miss = !miss;
            }
        } catch (NullPointerException e) {
            ;
        }

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
                            cmdsWithTags.put("pid", j);
                            break;
                        case 2:
                            cmdsWithTags.put("user", j);
                            break;
                        case 3:
                            cmdsWithTags.put("pr", j);
                            break;
                        case 4:
                            cmdsWithTags.put("ni", j);
                            break;
                        case 5:
                            cmdsWithTags.put("virt", j);
                            break;
                        case 6:
                            cmdsWithTags.put("res", j);
                            break;
                        case 7:
                            cmdsWithTags.put("shr", j);
                            break;
                        case 8:
                            cmdsWithTags.put("s", j);
                            break;
                        case 9:
                            cmdsWithTags.put("cpu", j);
                            break;
                        case 10:
                            cmdsWithTags.put("mem", j);
                            break;
                        case 11:
                            cmdsWithTags.put("time", j);
                            break;
                        case 12:
                            cmdsWithTags.put("command", j);
                            break;
                        default:
                            System.err.printf("%s", "out of fields");
                    }
                }
            }
            System.out.println(cmdsWithTags);
            pureCmds.add(cmdsWithTags);
        }
        return pureCmds;

    }
}
