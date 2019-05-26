package servicewalltest;


import org.junit.jupiter.api.Test;
import servicewall.modules.Processes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.fail;


public class GeneralTests {

    @Test
    public void TestProcess() throws Exception {
        Processes obj = new Processes();
        ArrayList<LinkedHashMap<String, String>> temp =  obj.getCurrentlyRunningProcesses();
        if (temp.size() == 0) {
            fail("failed to get currently running processes");
        } else {
            System.out.println("Object Counts TestProcess() => " + temp.size());
        }
    }

}
