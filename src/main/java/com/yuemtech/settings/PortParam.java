package com.yuemtech.settings;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortParam {
	private String portName;
	@Builder.Default private Integer bandRate = 6;
	@Builder.Default private Integer parity = 0;
	@Builder.Default private Integer dataBits = 3;
	@Builder.Default private Integer stopBits = 0;

	private List<String> commands;

	@Builder.Default private boolean hexMode = false;
	@Builder.Default private boolean withReturn = true;
	@Builder.Default private boolean withNewLine = true;
	
	@Builder.Default private boolean opened = false;
	
	public void addCommand(String command) {
		if (this.commands == null)
			this.commands = new ArrayList<>();
		this.commands.add(command);
		if (this.commands.size() > 64)
			this.commands = this.commands.subList(this.commands.size() - 64, 64);
	}
	
	public void removeCommand(int index) {
		if (this.commands == null)
			this.commands = new ArrayList<>();
		if (index > this.commands.size() - 1)
			return;
		this.commands.remove(index);
	}

	public void clearCommands() {
		this.commands = new ArrayList<>();
	}

	
}
