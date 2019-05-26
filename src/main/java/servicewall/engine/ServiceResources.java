package servicewall.engine;

import java.io.*;
import java.util.*;

public class ServiceResources {

    private String processLine, separator, processDetailsAll;
    private String[] processDetailsArray, cpu_mem_process_cmd = {
            "ps",
            "-p",
            "",
            "-o",
            "%cpu,%mem,cmd"
    };
    private List processDetailsArrayAll, processDetailsArrayAllStringified;
    private List < Map < String, String >> psDetailMap;

    private Map < String, String > detailsToMaps(String[] d) {

        Map < String, String > processDetails = new LinkedHashMap < String, String > ();
        for (String x: d) {
            x.replaceAll(":", "%%%");
            try {
                processDetails.put(x.split(":")[0], x.split(":")[1].trim());
            } catch (ArrayIndexOutOfBoundsException e) {
                processDetails.put(x.split(":")[0], "");
            }
        }
        return processDetails;

    }

    /**
     * USE CASE EXAMPLE ( PID 13021 -> JAVA )
     * Name:   java
     * Umask:  0002
     * State:  S (sleeping)
     * Tgid:   13021
     * Ngid:   0
     * Pid:    13021
     * PPid:   3638
     * TracerPid:      0
     * Uid:    1000    1000    1000    1000
     * Gid:    1000    1000    1000    1000
     * FDSize: 512
     * Groups: 10 1000
     * NStgid: 13021
     * NSpid:  13021
     * NSpgid: 1636
     * NSsid:  1636
     * VmPeak:  4266236 kB
     * VmSize:  4266236 kB
     * VmLck:         0 kB
     * VmPin:         0 kB
     * VmHWM:    152952 kB
     * VmRSS:    151852 kB
     * RssAnon:          132448 kB
     * RssFile:           19364 kB
     * RssShmem:             40 kB
     * VmData:   354916 kB
     * VmStk:       140 kB
     * VmExe:         4 kB
     * VmLib:     22492 kB
     * VmPTE:       696 kB
     * VmSwap:        0 kB
     * HugetlbPages:          0 kB
     * CoreDumping:    0
     * Threads:        23
     * SigQ:   1/47404
     * SigPnd: 0000000000000000
     * ShdPnd: 0000000000000000
     * SigBlk: 0000000000000004
     * SigIgn: 0000000000000000
     * SigCgt: 2000000181005ccf
     * CapInh: 0000000000000000
     * CapPrm: 0000000000000000
     * CapEff: 0000000000000000
     * CapBnd: 0000003fffffffff
     * CapAmb: 0000000000000000
     * NoNewPrivs:     0
     * Seccomp:        0
     * Speculation_Store_Bypass:       thread vulnerable
     * Cpus_allowed:   ff
     * Cpus_allowed_list:      0-7
     * Mems_allowed:   00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000000,00000001
     * Mems_allowed_list:      0
     * voluntary_ctxt_switches:        1
     * nonvoluntary_ctxt_switches:     0
     * */

    public List < Map < String, String >> getCompleteProcessDetails(List < Integer > ps) throws IOException {

        BufferedReader Br;
        for (int pid: ps) {
            ProcessBuilder prr = new ProcessBuilder("cat", "/proc/" + String.valueOf(pid) + "/status");
            Process prrRun = prr.start();
            Br = new BufferedReader(new InputStreamReader(prrRun.getInputStream()));
            try {
                while (true) {
                    this.processLine = Br.readLine();
                    if (this.processLine.trim().equals("")) {
                        break;
                    }
                    this.processDetailsAll += this.processLine.replaceAll(",", "|") + this.separator;

                }
            } catch (Exception e) {
                ;
            }
            this.processDetailsArray = this.processDetailsAll.split(this.separator);
            this.processDetailsArrayAll.add(this.processDetailsArray);
            this.processDetailsArrayAllStringified.add(Arrays.toString(this.processDetailsArray));
            this.processDetailsAll = "";
            this.psDetailMap.add(this.detailsToMaps(this.processDetailsArray));
        }
        return this.psDetailMap;

    }

    public LinkedHashMap < String, String > getCPU_MEM_Process(int pid) throws IOException {

        this.cpu_mem_process_cmd[2] = String.valueOf(pid);
        ProcessBuilder process = new ProcessBuilder(this.cpu_mem_process_cmd);
        Process pr = process.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = br.readLine();
        line = br.readLine();
        LinkedHashMap < String, String > map = new LinkedHashMap < String, String > ();
        String[] inArr = line.trim().split(" ", 4);
        ArrayList arrL = new ArrayList();
        for (String x: inArr) {
            if (x.length() != 0)
                arrL.add(x);
        }
        System.out.println(arrL);
        map.put("cpu", arrL.get(0).toString());
        map.put("mem", arrL.get(1).toString());
        map.put("cmd", arrL.get(2).toString());
        System.out.println(map);
        return map;

    }

    public LinkedHashMap < String, String > getCPU_MEM_Process(String n) throws IOException {

        this.cpu_mem_process_cmd[2] = "$(pidof " + n + ")";
        ProcessBuilder process = new ProcessBuilder(this.cpu_mem_process_cmd);
        Process pr = process.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = br.readLine();
        line = br.readLine();
        LinkedHashMap < String, String > map = new LinkedHashMap < String, String > ();
        String[] inArr = line.trim().split(" ", 4);
        ArrayList arrL = new ArrayList();
        for (String x: inArr) {
            if (x.length() != 0)
                arrL.add(x);
        }
        System.out.println(arrL);
        map.put("cpu", arrL.get(0).toString());
        map.put("mem", arrL.get(1).toString());
        map.put("cmd", arrL.get(2).toString());
        System.out.println(map);
        return map;

    }

}