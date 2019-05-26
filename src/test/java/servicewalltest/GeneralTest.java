package servicewalltest;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import servicewall.modules.Processes;
import servicewall.modules.ProcessesActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
//import static org.junit.jupiter.api.Assertions.fail;


public class GeneralTest {

    @Test
    public void ProcessTest() throws IOException {
        Processes obj = new Processes();
        ArrayList<LinkedHashMap<String, String>> temp =  obj.getCurrentlyRunningProcesses();
        Assertions.assertTrue(temp.size() != 0);
    }

    @Test
    public void PATest() throws IOException {
        ProcessesActivity o = new ProcessesActivity();
        Assertions.assertTrue(o.getDataInUseByProcessID(1).size() != 0);
    }
}
