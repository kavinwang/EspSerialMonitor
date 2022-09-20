package com.yuemtech.settings;

public class PortParam {
	private String portName;
	private Integer bandRate = 5;
	private Integer parity = 0;
	private Integer dataBits = 3;
	private Integer stopBits = 0;
	
	public String getPortName() {
		return portName;
	}
	
	public void setPortName(String portName) {
		this.portName = portName;
	}
	
	public Integer getBandRate() {
		return bandRate;
	}
	
	public void setBandRate(Integer bandRate) {
		this.bandRate = bandRate;
	}
	
	public Integer getParity() {
		return parity;
	}
	
	public void setParity(Integer parity) {
		this.parity = parity;
	}
	
	public Integer getDataBits() {
		return dataBits;
	}
	
	public void setDataBits(Integer dataBits) {
		this.dataBits = dataBits;
	}
	
	public Integer getStopBits() {
		return stopBits;
	}
	
	public void setStopBits(Integer stopBits) {
		this.stopBits = stopBits;
	}

	
	public boolean isReturnCheck() {
		return returnCheck;
	}
	
	public void setReturnCheck(boolean returnCheck) {
		this.returnCheck = returnCheck;
	}
	
	public boolean isNewLineCheck() {
		return newLineCheck;
	}
	
	public void setNewLineCheck(boolean newLineCheck) {
		this.newLineCheck = newLineCheck;
	}
	
	boolean returnCheck = true;
	boolean newLineCheck = true;
}
