package com.yuemtech;

import com.yuemtech.settings.PortSetter;
import com.yuemtech.support.SerialController;

import java.util.List;

public class Main {
	public static void main(String[] args)throws Exception {
		List<String> ports = SerialController.findPorts();
		ports.forEach(System.out::println);
		
		System.out.println(PortSetter.BandRates[PortSetter.getInstance().getLeftPortSetting().getBandRate()]);
		System.out.println(PortSetter.DataBits[PortSetter.getInstance().getLeftPortSetting().getDataBits()]);
		System.out.println(PortSetter.Parities[PortSetter.getInstance().getLeftPortSetting().getParity()]);
		System.out.println(PortSetter.StopBits[PortSetter.getInstance().getLeftPortSetting().getStopBits()]);

//		SerialController sc = new SerialController(portSetter.getLeftPortSetting().getBandRate());
//		sc.openPort("tty.BLTH", new SerialController.SerialPortListener() {
//			@Override
//			public void dataRecieved(byte[] datas) {
//				System.out.println(new String(datas));
//			}
//		});
	}
}
