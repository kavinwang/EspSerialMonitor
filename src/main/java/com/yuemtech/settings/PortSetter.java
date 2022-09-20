package com.yuemtech.settings;

import purejavacomm.SerialPort;

public class PortSetter {
	private static PortSetter instance;
	private PortParam leftPortSetting;
	
	public static final Integer[] BandRates = new Integer[]{9600,14400,19200,38400,57600,115200};
	public static final Integer[] DataBits = new Integer[]{SerialPort.DATABITS_5,SerialPort.DATABITS_6,SerialPort.DATABITS_7,SerialPort.DATABITS_8};
	public static final Integer[] Parities = new Integer[]{SerialPort.PARITY_NONE,SerialPort.PARITY_ODD,SerialPort.PARITY_EVEN,SerialPort.PARITY_MARK,SerialPort.PARITY_SPACE};
	public static final Integer[] StopBits = new Integer[]{SerialPort.STOPBITS_1, SerialPort.STOPBITS_2, SerialPort.STOPBITS_1_5};
	
	public PortParam getLeftPortSetting() {
		return leftPortSetting;
	}
	
	public PortParam getRightPortSetting() {
		return rightPortSetting;
	}
	
	private PortParam rightPortSetting;
	public static PortSetter getInstance(){
		if(instance == null)instance = new  PortSetter();
		return instance;
	}
	private PortSetter(){
		this.loadSettings();
	}
	public void loadSettings(){
		leftPortSetting = new PortParam();
		rightPortSetting = new PortParam();
	}
}
