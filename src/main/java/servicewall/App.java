package servicewall;

import servicewall.modules.*;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {

        System.out.println("Service-wall main process initiated!");
        Processes processesObj = new Processes();
        System.out.println(processesObj.getCurrentlyRunningProcesses());

    }
}