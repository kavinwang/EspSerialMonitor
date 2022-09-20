package com.yuemtech.settings;

import purejavacomm.SerialPort;

public class PortSetter {
	public static final int PORT_LEFT = 0;
	public static final int PORT_RIGHT = 1;
	private static PortSetter instance;
	private PortParam leftPortSetting;
	private PortParam rightPortSetting;

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

	public static int getPortParity(int portSide) {
		PortParam pp = portSide == 0 ?PortSetter.getInstance().getLeftPortSetting():PortSetter.getInstance().getRightPortSetting();
		return PortSetter.Parities[pp.getParity()];
	}

	public static int getPortBandRate(int portSide) {
		PortParam pp = portSide == 0 ? PortSetter.getInstance().getLeftPortSetting() : PortSetter.getInstance().getRightPortSetting();
		return PortSetter.BandRates[pp.getBandRate()];
	}
	
	public static int getPortDataBit(int portSide) {
		PortParam pp = portSide == 0 ? PortSetter.getInstance().getLeftPortSetting() : PortSetter.getInstance().getRightPortSetting();
		return PortSetter.DataBits[pp.getDataBits()];
	}

	public static int getPortStopBit(int portSide) {
		PortParam pp = portSide == 0 ? PortSetter.getInstance().getLeftPortSetting() : PortSetter.getInstance().getRightPortSetting();
		return PortSetter.StopBits[pp.getStopBits()];
	}
	
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
