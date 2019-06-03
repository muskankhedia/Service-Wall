package servicewall.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import servicewall.modules.Processes;
import servicewall.modules.ProcessVerify_Internet;
import servicewall.engine.TerminateProcess;

public class Engine {

    private Processes ProcessesObject = new Processes();
    private ProcessVerify_Internet ProcessVerifyObject = new ProcessVerify_Internet();
    private final TerminateProcess TerminateProcessObject = new TerminateProcess();

    private ArrayList<LinkedHashMap<String, String>> SetRootProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetVerifiedProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetUnVerifiedProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetNonRootProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetUnTrustedProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetMaliciousProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetBlockedProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetAllProcesses = new ArrayList<LinkedHashMap<String, String>>();

    private Boolean updateCurrentlyRunningProcesses() throws IOException {
        this.SetAllProcesses = this.ProcessesObject.getCurrentlyRunningProcesses();
        return true;
    }

    private Boolean updateCurrentlyRunningRootProcesses() throws IOException {

        for(LinkedHashMap<String, String> instance : this.SetAllProcesses) {
            if (instance.get("user") == "root") {
                this.SetRootProcesses.add(instance);
                this.SetVerifiedProcesses.add(instance);
            }
        }
        return true;

    }

    private Boolean updateCurrentlyRunningNonRootProcesses() throws IOException {

        for(LinkedHashMap<String, String> instance : this.SetAllProcesses) {
            if (instance.get("user") != "root") {
                this.SetNonRootProcesses.add(instance);
            }
        }
        return true;

    }

    private Boolean updateCurrentlyRunningRootAndNonRootProcesses() throws IOException {

        for(LinkedHashMap<String, String> instance : this.SetAllProcesses) {
            if (instance.get("user") == "root") {
                this.SetRootProcesses.add(instance);
                this.SetVerifiedProcesses.add(instance);
            } else {
                this.SetNonRootProcesses.add(instance);
            }
        }
        return true;

    }

    private void verifyMaliciousProcessesThroughtGoogleSearch(ArrayList<LinkedHashMap<String, String>> pr) throws IOException {
        for (LinkedHashMap<String , String > inst : pr) {
            boolean isMalicious = ProcessVerifyObject.verifyIfMaliciousProcessThroughGoogleCheck(inst.get("command").trim());
            if (isMalicious) {
                this.SetMaliciousProcesses.add(inst);
                this.SetBlockedProcesses.add(inst);
            }
        }
    }

    private void blockMaliciousProcess(ArrayList<LinkedHashMap<String, String>> pr) {
        for (LinkedHashMap<String, String> inst : pr ) {
            String temp = inst.get("pid");
            if (TerminateProcessObject.Block_Terinate_KillProcess(inst.get(temp))) {
                System.out.printf("terminated process %s", temp);
            } else {
                System.out.printf("failed to terminate process %s", temp);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Engine o = new Engine();
        o.updateCurrentlyRunningProcesses();
        System.out.println(o.SetAllProcesses);
        o.updateCurrentlyRunningRootAndNonRootProcesses();
        System.out.println(o.SetRootProcesses);
        System.out.println(o.SetNonRootProcesses);
    }

}
