package servicewall.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ProcessesActivity {

    private final String[] CMD_CURRENTLY_RUNNING_PROCESS = {"lsof", "-p", ""};
    private final String LINE_SEPERATOR = "%%%";

    public ArrayList<LinkedHashMap<String, String>> getDataInUseByProcessID(int id) throws IOException {

        this.CMD_CURRENTLY_RUNNING_PROCESS[2] = String.valueOf(id);
        ProcessBuilder processFetchProcesses = new ProcessBuilder(this.CMD_CURRENTLY_RUNNING_PROCESS);
        Process currently = processFetchProcesses.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(currently.getInputStream()));

        // skip first line
        String lines = br.readLine(), allLines="";

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

        for (int i1=0; i1< diffLines.length; i1++) {
            String i = diffLines[i1];
            boolean found = false;
            for(int j=i1+1; j< diffLines.length; j++) {
                if (diffLines[j].compareTo(i)==0) {
                    found = true;
                    break;
                }
            }
            LinkedHashMap<String, String> cmdsWithTags= new LinkedHashMap<String, String>();
            if (!found) {
                String[] cmds = i.split(" ");
                short count = 0;
                for(String j : cmds) {
                    if(!j.trim().equals("")) {
                        ++count;
                        switch (count) {
                            case 1:
                                cmdsWithTags.put("command", j);
                                break;
                            case 2:
                                cmdsWithTags.put("pid", j);
                                break;
                            case 3:
                                cmdsWithTags.put("user", j);
                                break;
                            case 4:
                                cmdsWithTags.put("fd", j);
                                break;
                            case 5:
                                cmdsWithTags.put("type", j);
                                break;
                            case 6:
                                cmdsWithTags.put("device", j);
                                break;
                            case 7:
                                cmdsWithTags.put("size/off", j);
                                break;
                            case 8:
                                cmdsWithTags.put("node", j);
                                break;
                            case 9:
                                cmdsWithTags.put("name", j);
                                break;
                            default:
                                System.out.printf("\nextra as => %s\n", j);
                        }
                    }
                }
            }
            pureCmds.add(cmdsWithTags);
        }
        return pureCmds;
    }

    public ArrayList<LinkedHashMap<String, String>> getDataInUseByProcessName(String name) throws IOException {

        this.CMD_CURRENTLY_RUNNING_PROCESS[2] = "$(pidof " + name.trim() + ")";
        ProcessBuilder processFetchProcesses = new ProcessBuilder(this.CMD_CURRENTLY_RUNNING_PROCESS);
        Process currently = processFetchProcesses.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(currently.getInputStream()));

        // skip first line
        String lines = br.readLine(), allLines="";

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

        for (int i1=0; i1< diffLines.length; i1++) {
            String i = diffLines[i1];
            boolean found = false;
            for(int j=i1+1; j< diffLines.length; j++) {
                if (diffLines[j].compareTo(i)==0) {
                    found = true;
                    break;
                }
            }
            LinkedHashMap<String, String> cmdsWithTags= new LinkedHashMap<String, String>();
            if (!found) {
                String[] cmds = i.split(" ");
                short count = 0;
                for(String j : cmds) {
                    if(!j.trim().equals("")) {
                        ++count;
                        switch (count) {
                            case 1:
                                cmdsWithTags.put("command", j);
                                break;
                            case 2:
                                cmdsWithTags.put("pid", j);
                                break;
                            case 3:
                                cmdsWithTags.put("user", j);
                                break;
                            case 4:
                                cmdsWithTags.put("fd", j);
                                break;
                            case 5:
                                cmdsWithTags.put("type", j);
                                break;
                            case 6:
                                cmdsWithTags.put("device", j);
                                break;
                            case 7:
                                cmdsWithTags.put("size/off", j);
                                break;
                            case 8:
                                cmdsWithTags.put("node", j);
                                break;
                            case 9:
                                cmdsWithTags.put("name", j);
                                break;
                            default:
                                System.out.printf("\nextra as => %s\n", j);
                        }
                    }
                }
            }
            pureCmds.add(cmdsWithTags);
        }
        return pureCmds;
    }

}

