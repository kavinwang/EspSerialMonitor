package com.yuemtech.settings;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import purejavacomm.SerialPort;

public class PortSetter {
	public static final int PORT_MONITOR = 0;
	public static final int PORT_INTERACT = 1;
	public static final Integer[] BandRates = new Integer[]{4800,9600,14400,19200,38400,57600,115200,128000,256000,512000};
	public static final Integer[] DataBits = new Integer[]{SerialPort.DATABITS_5,SerialPort.DATABITS_6,SerialPort.DATABITS_7,SerialPort.DATABITS_8};
	public static final Integer[] Parities = new Integer[]{SerialPort.PARITY_NONE,SerialPort.PARITY_ODD,SerialPort.PARITY_EVEN,SerialPort.PARITY_MARK,SerialPort.PARITY_SPACE};
	public static final Integer[] StopBits = new Integer[]{SerialPort.STOPBITS_1, SerialPort.STOPBITS_2, SerialPort.STOPBITS_1_5};
	private static YAMLMapper yamlMapper = new YAMLMapper();

	private static PortParam monitorPortSetting = new PortParam();
	private static PortParam interactPortSetting = new PortParam();
	static {
		try {
			PortSetter.loadSettings();
		} catch (Exception e) {
			try {
				PortSetter.saveSettings();
			} catch (Exception ee) {
			}
		}
	}
	//进行绑定时使用
	public static PortParam getMonitorPortSetting() {
		return monitorPortSetting;
	}
	//进行绑定时使用
	public static PortParam getInteractSetting() {
		return interactPortSetting;
	}

	public static String getPortName(int portSide) {
		PortParam pp = portSide == 0 ?monitorPortSetting:interactPortSetting;
		return pp.getPortName();
	}

	public static int getPortParity(int portSide) {
		PortParam pp = portSide == 0 ?monitorPortSetting:interactPortSetting;
		return PortSetter.Parities[pp.getParity()];
	}

	public static int getPortBandRate(int portSide) {
		PortParam pp = portSide == 0 ? monitorPortSetting : interactPortSetting;
		return PortSetter.BandRates[pp.getBandRate()];
	}
	
	public static int getPortDataBit(int portSide) {
		PortParam pp = portSide == 0 ? monitorPortSetting : interactPortSetting;
		return PortSetter.DataBits[pp.getDataBits()];
	}

	public static int getPortStopBit(int portSide) {
		PortParam pp = portSide == 0 ? monitorPortSetting : interactPortSetting;
		return PortSetter.StopBits[pp.getStopBits()];
	}

	public static boolean isReturnCheck(int portSide) {
		PortParam pp = portSide == 0 ? monitorPortSetting : interactPortSetting;
		return pp.isWithReturn();
	}
    public static boolean isNewLineCheck(int portSide) {
        PortParam pp = portSide == 0 ? monitorPortSetting : interactPortSetting;
		return pp.isWithNewLine();
    }

	private PortSetter() {
	}

	public static void loadSettings() throws Exception {
		try (FileReader reader = new FileReader(new File(".settings.yaml"))) {
			Setting setting = yamlMapper.readValue(reader, Setting.class);
			monitorPortSetting = setting.monitor;
			interactPortSetting = setting.interact;
			System.out.println(BandRates[monitorPortSetting.getBandRate()]);
			System.out.println(BandRates[interactPortSetting.getBandRate()]);
		}
	}
	
	public static void saveSettings() throws Exception {
		try (FileWriter writer = new FileWriter(new File(".settings.yaml"))) {
			String setting = yamlMapper.writeValueAsString(new Setting(monitorPortSetting, interactPortSetting));
			writer.write(setting);
			writer.flush();
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private static class Setting {
		public PortParam monitor;
		public PortParam interact;
	}
   
}
