package com.yuemtech;

import com.yuemtech.settings.PortParam;
import com.yuemtech.settings.PortSetter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SettingDialog extends JDialog {
	private JComboBox leftPortSelect;
	private JComboBox leftDataBitSelect;
	private JComboBox leftParitySelect;
	private JComboBox leftStopBitSelect;
	private JComboBox leftBandRateSelect;
	private JCheckBox leftReturnCheck;
	private JCheckBox leftNewLineCheck;
	
	private JComboBox rightPortSelect;
	private JComboBox rightBandRateSelect;
	private JComboBox rightDataBitSelect;
	private JComboBox rightParitySelect;
	private JComboBox rightStopBitSelect;
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

		this.leftPortSelect.setSelectedItem(PortSetter.getInstance().getLeftPortSetting().getPortName()); //需要进行判断和检查
		this.leftDataBitSelect.setSelectedIndex(PortSetter.getInstance().getLeftPortSetting().getDataBits());
		this.leftParitySelect.setSelectedIndex(PortSetter.getInstance().getLeftPortSetting().getParity());
		this.leftStopBitSelect.setSelectedIndex(PortSetter.getInstance().getLeftPortSetting().getStopBits());
		this.leftBandRateSelect.setSelectedIndex(PortSetter.getInstance().getLeftPortSetting().getBandRate());
		this.leftReturnCheck.setSelected(PortSetter.getInstance().getLeftPortSetting().isReturnCheck());
		this.leftNewLineCheck.setSelected(PortSetter.getInstance().getLeftPortSetting().isNewLineCheck());
		
		this.rightPortSelect.setSelectedItem(PortSetter.getInstance().getRightPortSetting().getPortName()); //需要进行判断和检查
		this.rightDataBitSelect.setSelectedIndex(PortSetter.getInstance().getRightPortSetting().getDataBits());
		this.rightParitySelect.setSelectedIndex(PortSetter.getInstance().getRightPortSetting().getParity());
		this.rightStopBitSelect.setSelectedIndex(PortSetter.getInstance().getRightPortSetting().getStopBits());
		this.rightBandRateSelect.setSelectedIndex(PortSetter.getInstance().getRightPortSetting().getBandRate());
		this.rightReturnCheck.setSelected(PortSetter.getInstance().getRightPortSetting().isReturnCheck());
		this.rightNewLineCheck.setSelected(PortSetter.getInstance().getRightPortSetting().isNewLineCheck());
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
