package com.yuemtech;

import com.yuemtech.settings.PortSetter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SettingDialog extends JDialog {
	private JComboBox<String> leftPortSelect;
	private JComboBox<String> leftDataBitSelect;
	private JComboBox<String> leftParitySelect;
	private JComboBox<String> leftStopBitSelect;
	private JComboBox<String> leftBandRateSelect;
	private JCheckBox leftReturnCheck;
	private JCheckBox leftNewLineCheck;
	
	private JComboBox<String> rightPortSelect;
	private JComboBox<String> rightBandRateSelect;
	private JComboBox<String> rightDataBitSelect;
	private JComboBox<String> rightParitySelect;
	private JComboBox<String> rightStopBitSelect;
	private JCheckBox rightReturnCheck;
	private JCheckBox rightNewLineCheck;
	
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	
	public SettingDialog() {
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);
		
		buttonOK.addActionListener(e -> onOK());
		
		buttonCancel.addActionListener(e -> onCancel());
		
		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});
		
		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		this.leftPortSelect.setSelectedItem(PortSetter.getPortName(PortSetter.PORT_MONITOR)); //需要进行判断和检查
		this.leftDataBitSelect.setSelectedIndex(PortSetter.getPortDataBit(PortSetter.PORT_MONITOR));
		this.leftParitySelect.setSelectedIndex(PortSetter.getPortParity(PortSetter.PORT_MONITOR));
		this.leftStopBitSelect.setSelectedIndex(PortSetter.getPortStopBit(PortSetter.PORT_MONITOR));
		this.leftBandRateSelect.setSelectedIndex(PortSetter.getPortBandRate(PortSetter.PORT_MONITOR));
		this.leftReturnCheck.setSelected(PortSetter.isReturnCheck(PortSetter.PORT_MONITOR));
		this.leftNewLineCheck.setSelected(PortSetter.isNewLineCheck(PortSetter.PORT_MONITOR));
		
		this.rightPortSelect.setSelectedItem(PortSetter.getPortName(PortSetter.PORT_INTERACT)); //需要进行判断和检查
		this.rightDataBitSelect.setSelectedIndex(PortSetter.getPortDataBit(PortSetter.PORT_INTERACT));
		this.rightParitySelect.setSelectedIndex(PortSetter.getPortParity(PortSetter.PORT_INTERACT));
		this.rightStopBitSelect.setSelectedIndex(PortSetter.getPortStopBit(PortSetter.PORT_INTERACT));
		this.rightBandRateSelect.setSelectedIndex(PortSetter.getPortBandRate(PortSetter.PORT_INTERACT));
		this.rightReturnCheck.setSelected(PortSetter.isReturnCheck(PortSetter.PORT_INTERACT));
		this.rightNewLineCheck.setSelected(PortSetter.isNewLineCheck(PortSetter.PORT_INTERACT));
	}
	
	private void onOK() {
		// add your code here
		dispose();
	}
	
	private void onCancel() {
		// add your code here if necessary
		dispose();
	}
	
	public static void main(String[] args) {
		SettingDialog dialog = new SettingDialog();
		dialog.setMinimumSize(new Dimension(640,256));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension jfSize = dialog.getSize();
		dialog.setLocation(screenSize.width / 2 - jfSize.width / 2,screenSize.height / 2 - jfSize.height / 2);
		dialog.pack();

		dialog.setVisible(true);
		System.exit(0);
	}
	
}
