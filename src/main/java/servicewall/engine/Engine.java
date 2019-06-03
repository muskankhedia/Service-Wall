package servicewall.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import servicewall.engine.SystemFilesMonitor;
import servicewall.modules.Processes;
import servicewall.modules.ProcessVerify_Internet;

import javax.sound.midi.SysexMessage;

public class Engine {

    private Processes ProcessesObject = new Processes();
    private ProcessVerify_Internet ProcessVerifyObject = new ProcessVerify_Internet();
    private final TerminateProcess TerminateProcessObject = new TerminateProcess();

    protected ArrayList<LinkedHashMap<String, String>> SetRootProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetVerifiedProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetUnVerifiedProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetNonRootProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetUnTrustedProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetMaliciousProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetBlockedProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetAllProcesses = new ArrayList<LinkedHashMap<String, String>>(),
        SetProcessesBook = new ArrayList<LinkedHashMap<String, String>>();

    protected Boolean updateCurrentlyRunningProcesses() throws IOException {

        this.SetAllProcesses = this.ProcessesObject.getCurrentlyRunningProcesses();
        return true;

    }

    protected Boolean updateCurrentlyRunningRootProcesses() throws IOException {

        for(LinkedHashMap<String, String> instance : this.SetAllProcesses) {
            if (instance.get("user").equals("root")) {
                this.SetRootProcesses.add(instance);
                this.SetVerifiedProcesses.add(instance);
            }
        }
        return true;

    }

    protected Boolean initialiseUnVerifiedProcesses() throws IOException {

        for(LinkedHashMap<String, String> instance : this.SetAllProcesses) {
            if (!instance.get("user").equals("root")) {
                this.SetNonRootProcesses.add(instance);
            }
        }
        return true;

    }

    protected Boolean updateCurrentlyRunningNonRootProcesses() throws IOException {

        for(LinkedHashMap<String, String> instance : this.SetAllProcesses) {
            if (!instance.get("user").equals("root")) {
                this.SetNonRootProcesses.add(instance);
            }
        }
        return true;

    }

    protected Boolean updateCurrentlyRunningRootAndNonRootProcesses() throws IOException {

        for(LinkedHashMap<String, String> instance : this.SetAllProcesses) {
            if (instance.get("user").equals("root")) {
                this.SetRootProcesses.add(instance);
                this.SetVerifiedProcesses.add(instance);
            } else {
                this.SetNonRootProcesses.add(instance);
            }
        }
        return true;

    }

    // updateProcessesBook() updates SetProcessesBook which is a set of all processes with appropriate tags in their priority order
    protected void updateProcessesBook() {

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

        if (this.SetUnTrustedProcesses.size() == 0 || this.SetMaliciousProcesses.size() == 0) {

            // add unVerified processes to ProcessBook
            for (LinkedHashMap<String, String> inst : this.SetNonRootProcesses) {
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

    protected void showProcessBook() {

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

    public void clearScreen() throws IOException {

        System.out.print("\033[H\033[2J");
        System.out.flush();

    }

    public void Initiator() throws IOException {

        this.clearScreen();
        System.out.println("initialising service-wall ...");

        System.out.println("updating currently running processes");
        this.updateCurrentlyRunningProcesses();

        System.out.println("updating currently root and non-root processes ...");
        this.updateCurrentlyRunningRootAndNonRootProcesses();

        System.out.println("initialising un-verified processes ...");
        this.initialiseUnVerifiedProcesses();

    }

    public void updateBuffers() throws IOException {

        System.out.printf("\nupdating buffer values ...");
        this.updateCurrentlyRunningProcesses();
        this.updateCurrentlyRunningRootAndNonRootProcesses();
        this.initialiseUnVerifiedProcesses();
        System.out.printf("done!\n");
        this.clearScreen();

    }

    private final Scanner Sc = new Scanner(System.in);

    private SystemFilesMonitor SystemFilesMonitorObject = new SystemFilesMonitor();

    private void viewVerifiedProcesses() {
        for (LinkedHashMap<String , String > x : this.SetVerifiedProcesses) {
            System.out.println(x);
        }
    }

    private void viewRootProcesses() {
        for (LinkedHashMap<String , String > x : this.SetRootProcesses) {
            System.out.println(x);
        }
    }

    private void viewUnTrustedProcesses() {
        for (LinkedHashMap<String , String > x : this.SetUnTrustedProcesses) {
            System.out.println(x);
        }
    }

    private void viewNonRootProcesses() {
        for (LinkedHashMap<String , String > x : this.SetNonRootProcesses) {
            System.out.println(x);
        }
    }

    private void viewMaliciousProcesses() {
        for (LinkedHashMap<String , String > x : this.SetMaliciousProcesses) {
            System.out.println(x);
        }
    }

    private void viewBlockedProcesses() {
        for (LinkedHashMap<String , String > x : this.SetBlockedProcesses) {
            System.out.println(x);
        }
    }

    private void SystemFilesMonitorController() {

        Thread SystemFilesMonitorThread = new Thread(SystemFilesMonitorObject);
        SystemFilesMonitorThread.start();

    }

    private void listFunctionalities_CLI() {
        System.out.println("\tfunctionalities");
        System.out.println("\t\t(a) Process Verification");
        System.out.println("\t\t(b) Process Blocker");
        System.out.println("\t\t(c) System Files Monitor");
        System.out.print("\t\t: ");
        char ch = Sc.nextLine().charAt(0);

        switch (ch) {
            case 'a':
                System.out.println("\n\tinitiating process verification");
                break;
            case 'b':
                System.out.println("\n\tinitiating process blocker");
                break;
            case 'c':
                System.out.println("\n\tinitiating system files monitor");
                this.SystemFilesMonitorController();
                break;

            default:
                System.out.println("\n\nwrong input. try again");
        }
    }

    public void service_wall_CLI() throws IOException {

        while (true) {

            System.out.println("\nTypes :");
            System.out.println("\t(a) View Process Book");
            System.out.println("\t(b) View Verified Processes");
            System.out.println("\t(c) View Root Processes");
            System.out.println("\t(d) View UnTrusted Processes");
            System.out.println("\t(e) View Non-Root Processes");
            System.out.println("\t(f) View Malicious Processes");
            System.out.println("\t(g) View Blocked Processes");
            System.out.println("\t(h) Update all Buffers");
            System.out.println("\t(i) List functionality modules");
            System.out.println("\t(j) Exit CLI");
            System.out.print("\t: ");
            char choice = Sc.nextLine().charAt(0);
            if (choice == 'j') {
                break;
            }

            switch (choice) {
                case 'a': this.updateProcessesBook();this.showProcessBook();break;
                case 'b': this.viewVerifiedProcesses();break;
                case 'c': this.viewRootProcesses();break;
                case 'd': this.viewUnTrustedProcesses();break;
                case 'e': this.viewNonRootProcesses();break;
                case 'f': this.viewMaliciousProcesses();break;
                case 'g': this.viewBlockedProcesses();break;
                case 'h': this.updateBuffers();break;
                case 'i': this.listFunctionalities_CLI();break;
                default: System.out.println("wrong input.");
            }
            System.out.print("\nPress enter to continue");
            System.in.read();
            this.clearScreen();
        }

    }

    public static void main(String[] args) throws IOException {

        Engine obj = new Engine();
        obj.Initiator();
        obj.service_wall_CLI();

    }

}

