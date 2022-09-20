package com.yuemtech.support;

import purejavacomm.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

public class SerialController implements SerialPortEventListener{
	public static ArrayList<String> findPorts() {
		// 获得当前所有可用串口
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		ArrayList<String> portNameList = new ArrayList<>();
		// 将可用串口名添加到List并返回该List
		while(portList.hasMoreElements()) {
			String portName = portList.nextElement().getName();
			portNameList.add(portName);
		}
		return portNameList;
	}
	
	String portName;
	Integer baudRate = 9600;
	Integer dataBits = SerialPort.DATABITS_8;
	Integer parity = SerialPort.PARITY_NONE;
	Integer stopBits = SerialPort.STOPBITS_1;
	Integer flowControl = SerialPort.FLOWCONTROL_NONE;
	
	private SerialPortListener listener;
	private SerialPort serialPort = null;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public SerialController(){
	}
	public SerialController(Integer baudRate){
		this.baudRate = baudRate;
	}
	public SerialController(Integer baudRate, Integer dataBits, Integer parity, Integer stopBits){
		this.baudRate = baudRate;
		this.dataBits = dataBits;
		this.parity = parity;
		this.stopBits = stopBits;
	}
	public SerialController(Integer baudRate, Integer dataBits, Integer parity, Integer stopBits, Integer flowControl){
		this.baudRate = baudRate;
		this.dataBits = dataBits;
		this.parity = parity;
		this.stopBits = stopBits;
		this.flowControl = flowControl;
	}
	
	//重连
	public void openPort() throws Exception {
		openPort(null);
	}
	
	//关闭状态，允许换端口
	public void openPort(String portName) throws Exception {
		openPort(portName, this.listener);
	}

	public void openPort(String portName, SerialPortListener listener) throws Exception {
		if (this.serialPort != null) return;
		if(portName == null) portName = this.portName;
		if(portName == null || "".equals(portName.trim())) throw new Exception("need comm port.");
		if(listener != null) this.listener = listener;

		if(!portName.equals(this.portName)){
			String commStr = "";
			try {
				int comport = Integer.parseInt(portName);
				String osName = System.getProperty("os.name");
				if (osName.contains("Windows")) commStr = "COM" + comport;
				else if (osName.contains("Linux")) commStr = "/dev/ttyS" + (comport - 1);
				else commStr = portName;
			} catch (Exception e) {
				commStr = portName;
			}
			this.portName = commStr;
		}
		
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL && portId.getName().equals(this.portName )) {
				//CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
				CommPort commPort = portId.open(portName, 2000);
				if(commPort instanceof SerialPort) {
					this.serialPort = (SerialPort) commPort;
					this.serialPort.setSerialPortParams(this.baudRate, this.dataBits, this.stopBits, this.parity);
					this.serialPort.setFlowControlMode(this.flowControl);
					
//					serialPort.disableReceiveFraming();
//					serialPort.disableReceiveThreshold();
//					serialPort.disableReceiveTimeout();
					
					this.serialPort.notifyOnBreakInterrupt(true);// 设置当通信中断时唤醒中断线程
					this.serialPort.notifyOnDataAvailable(this.listener != null);// 设置当有数据到达时唤醒监听接收线程
					this.serialPort.addEventListener(this);
					outputStream = this.serialPort.getOutputStream();
					inputStream = this.serialPort.getInputStream();
				}else commPort.close();
			}
		}
		throw new Exception("NO SUCH PORT: " + this.portName);
	}
	
	public void closePort() throws Exception {
		if(this.serialPort != null) this.serialPort.close();
		this.serialPort = null;
	}
	
	public void directSend(byte[] datas) throws Exception {
		outputStream.write(datas);
		outputStream.flush();
	}

	public boolean hasDataInBuffer() throws Exception {
		return inputStream.available() > 0;
	}
	
	public byte[] directRecv(int length, int timeOut) throws Exception {
		if (timeOut < 0) timeOut = 0;
		long start = System.currentTimeMillis();
		byte[] buffer = new byte[length];
		int rc;
		int received = 0;
		int remain = length;
		while (remain > 0) {
			int avail = inputStream.available();
			if (avail > 0 && avail <= remain) rc = inputStream.read(buffer, received, avail);
			else rc = inputStream.read(buffer, received, remain);

			if (rc < 0) throw new Exception("Read Stream Closed!");
			remain -= rc;
			received += rc;

			if (remain == 0) break;
			if (timeOut > 0) {
				if (rc > 0) start = System.currentTimeMillis();
				else if ((System.currentTimeMillis() - start) > timeOut) throw new Exception("time out");
			}
		}
		return buffer;
	}
	
	public void clearReadBuffer() throws Exception {
		int av = inputStream.available();
		inputStream.skip(av);
	}
	
	@Override
	public void serialEvent(SerialPortEvent event) {
		switch(event.getEventType()) {
			case SerialPortEvent.BI: //BreakInterrupt
			case SerialPortEvent.OE: //OverrunError
			case SerialPortEvent.FE: //FramingError
			case SerialPortEvent.PE: //ParityError
			case SerialPortEvent.CD: //CarrierDetect
			case SerialPortEvent.RI: //RingIndicator
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;
			case SerialPortEvent.CTS:case SerialPortEvent.DSR:
				break;
			case SerialPortEvent.DATA_AVAILABLE:
				if(this.listener!=null) {
					ByteBuffer bb = new ByteBuffer();
					try {
						int av = inputStream.available();
						do {
							if(av != 0) {
								byte[] buffer = new byte[av];
								int read = inputStream.read(buffer);
								if(read > 0) bb.append(buffer,0, read);
							}
							av = inputStream.available();
							if(av == 0) {
								Thread.sleep(50);
								av = inputStream.available();
							}
						} while(av != 0);
						this.listener.dataRecieved(bb.getValue());
					} catch(Exception ignore) {
					}
				}
				break;
		}
	}
	
	public interface SerialPortListener {
		public void dataRecieved(byte[] datas);
	}
	
}
