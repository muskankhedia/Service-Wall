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
        SetAllProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetProcessesBook = new ArrayList<LinkedHashMap<String, String>>();

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

    private Boolean initialiseUnVerifiedProcesses() throws IOException {

        for(LinkedHashMap<String, String> instance : this.SetAllProcesses) {
            if (instance.get("user") != "root") {
                this.SetNonRootProcesses.add(instance);
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

    // updateProcessesBook() updates SetProcessesBook which is a set of all processes with appropriate tags in their priority order
    private void updateProcessesBook() {

        // re-initialise ProcessBook
        this.SetProcessesBook = new ArrayList<LinkedHashMap<String, String>>();

        // add Root processes to ProcessBook
        for (LinkedHashMap<String, String> inst : this.SetRootProcesses) {
            LinkedHashMap<String , String > temp = new LinkedHashMap<String, String>();
            temp.put("command", inst.get("command"));
            temp.put("pid", inst.get("pid"));
            temp.put("tag", "root");
            temp.put("status", "verified");
            this.SetProcessesBook.add(temp);
        }

        if (this.SetUnTrustedProcesses.size() == 0 && this.SetMaliciousProcesses.size() == 0) {
            // add unVerified processes to ProcessBook
            for (LinkedHashMap<String, String> inst : this.SetUnTrustedProcesses) {
                LinkedHashMap<String , String > temp = new LinkedHashMap<String, String>();
                temp.put("command", inst.get("command"));
                temp.put("pid", inst.get("pid"));
                temp.put("tag", "non-root");
                temp.put("status", "unverified");
                this.SetProcessesBook.add(temp);
            }
        } else {
            // add UnTrusted processes to ProcessBook
            for (LinkedHashMap<String, String> inst : this.SetUnTrustedProcesses) {
                LinkedHashMap<String, String> temp = new LinkedHashMap<String, String>();
                temp.put("command", inst.get("command"));
                temp.put("pid", inst.get("pid"));
                temp.put("tag", "root");
                temp.put("status", "verified");
                this.SetProcessesBook.add(temp);
            }

            // add Malicious processes to ProcessBook
            for (LinkedHashMap<String, String> inst : this.SetMaliciousProcesses) {
                LinkedHashMap<String, String> temp = new LinkedHashMap<String, String>();
                temp.put("command", inst.get("command"));
                temp.put("pid", inst.get("pid"));
                temp.put("tag", "root");
                temp.put("status", "verified");
                this.SetProcessesBook.add(temp);
            }
        }

    }

    private void showProcessBook() {

        for (LinkedHashMap<String, String> inst : this.SetProcessesBook) {
            System.out.println(inst);
        }

    }

    private void verifyMaliciousProcessesThroughtGoogleSearch(ArrayList<LinkedHashMap<String, String>> pr) throws IOException {

        for (LinkedHashMap<String , String > inst : pr) {
            boolean isMalicious = ProcessVerifyObject.verifyIfMaliciousProcessThroughGoogleCheck(inst.get("command").trim());
            if (isMalicious) {
                this.SetMaliciousProcesses.add(inst);
                this.SetBlockedProcesses.add(inst);
                String temp = inst.get("pid");
                TerminateProcessObject.Block_Terinate_KillProcess(temp);
                System.out.printf("terminated process %s", temp);
            } else {
                this.SetUnVerifiedProcesses.remove(inst);
                this.SetVerifiedProcesses.add(inst);
            }
        }

    }

    private void blockMaliciousProcess(ArrayList<LinkedHashMap<String, String>> pr) {

        for (LinkedHashMap<String, String> inst : pr) {
            String temp = inst.get("pid");
            if (TerminateProcessObject.Block_Terinate_KillProcess(inst.get(temp))) {
                System.out.printf("terminated process %s", temp);
            } else {
                System.out.printf("failed to terminate process %s", temp);
            }
        }

    }

    public void service_wall_CLI(Engine mainObject) throws Exception {

        System.out.println("initialising Service-Wall ...");
        System.out.println("updating currently running processes ...");
        mainObject.updateCurrentlyRunningProcesses();
        System.out.println("updating currently root and non-root processes ...");
        mainObject.updateCurrentlyRunningRootAndNonRootProcesses();
        System.out.println("initialising un-verified processes ...");
        mainObject.initialiseUnVerifiedProcesses();

    }

    public static void main(String[] args) throws Exception {

        Engine mainObject = new Engine();
        mainObject.service_wall_CLI(mainObject);

    }

}
