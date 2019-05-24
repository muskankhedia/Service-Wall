package servicewall.services;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;


public class SystemConfig {

    public final SystemInfo SystemInfoObj = new SystemInfo();

    public OperatingSystem getOperatingSystemObject() {
        return SystemInfoObj.getOperatingSystem();
    }

    public HardwareAbstractionLayer getHardwareSystemObject() {
        return SystemInfoObj.getHardware();
    }

    public void PrintVariables() {

        System.out.println(SystemInfoObj.getOperatingSystem());
        System.out.println(SystemInfoObj.getHardware());

    }

}
