package servicewall.engine;

import servicewall.modules.ProcessesSignal;

public class TerminateProcess {

    private final ProcessesSignal o = new ProcessesSignal();

    public Boolean Block_Terinate_KillProcess(int pid) {
        return o.SIGKILL(pid);
    }

    public Boolean Block_Terinate_KillProcess(String pName) {
        return o.SIGKILL(pName);
    }

}
