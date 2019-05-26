package servicewall.modules;

import java.io.IOException;

public class ProcessesSignal {

    private final String[] SIGKILL_cmd = {"kill", "-SIGKILL", ""};
    private final String[] SIGTERM_cmd = {"kill", "-SIGTERM", ""};
    private final String[] SIGINT_cmd = {"kill", "-SIGINT", ""};
    private final String[] SIGHUP_cmd = {"kill", "-SIGHUP", ""};
    Process runner;

    public boolean SIGKILL(int pid) {

        this.SIGKILL_cmd[2] = String.valueOf(pid);
        ProcessBuilder proc = new ProcessBuilder(this.SIGKILL_cmd);
        try {
            this.runner = proc.start();
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public boolean SIGKILL(String processName) {

        this.SIGKILL_cmd[2] = "$(pidof " + processName + ")";
        ProcessBuilder proc = new ProcessBuilder(this.SIGKILL_cmd);
        try {
            this.runner = proc.start();
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public boolean SIGTERM(int pid) {

        this.SIGTERM_cmd[2] = String.valueOf(pid);
        ProcessBuilder proc = new ProcessBuilder(this.SIGTERM_cmd);
        try {
            this.runner = proc.start();
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public boolean SIGTERM(String processName) {

        this.SIGTERM_cmd[2] = "$(pidof " + processName + ")";
        ProcessBuilder proc = new ProcessBuilder(this.SIGTERM_cmd);
        try {
            this.runner = proc.start();
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public boolean SIGINT(int pid) {

        this.SIGINT_cmd[2] = String.valueOf(pid);
        ProcessBuilder proc = new ProcessBuilder(this.SIGINT_cmd);
        try {
            this.runner = proc.start();
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public boolean SIGINT(String processName) {

        this.SIGINT_cmd[2] = "$(pidof " + processName + ")";
        ProcessBuilder proc = new ProcessBuilder(this.SIGINT_cmd);
        try {
            this.runner = proc.start();
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public boolean SIGHUP(int pid) {

        this.SIGHUP_cmd[2] = String.valueOf(pid);
        ProcessBuilder proc = new ProcessBuilder(this.SIGHUP_cmd);
        try {
            this.runner = proc.start();
        } catch (IOException e) {
            return false;
        }
        return true;

    }

    public boolean SIGHUP(String processName) {

        this.SIGHUP_cmd[2] = "$(pidof " + processName + ")";
        ProcessBuilder proc = new ProcessBuilder(this.SIGHUP_cmd);
        try {
            this.runner = proc.start();
        } catch (IOException e) {
            return false;
        }
        return true;

    }

}
