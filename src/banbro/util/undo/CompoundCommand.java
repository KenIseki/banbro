package banbro.util.undo;

import java.util.ArrayList;
import java.util.List;

public final class CompoundCommand extends Command {
	private List<Command> _commands;
	private String _commandName;

	public CompoundCommand() {
		_commands = new ArrayList<Command>();
	}

	public CompoundCommand(String name) {
		this();
		setCommandName(name);
	}

	@Override
	public String getCommandName() {
		return _commandName;
	}

	public void setCommandName(String name) {
		_commandName = name;
	}

	public List<Command> getSubCommands() {
		return _commands;
	}

	public void addSubCommand(Command command) {
		if (command instanceof CompoundCommand) {
			if (command==this) {
				return;
			}
			for (Command c : ((CompoundCommand) command).getSubCommands()) {
				addSubCommand(c);
			}
		} else {
			_commands.add(command);
		}
	}

	@Override
	public boolean canExecute() {
		if (_commands.isEmpty()) {
			return false;
		}
		for (Command command : _commands) {
			if (command.canExecute()==false) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canUndo() {
		if (_commands.isEmpty()) {
			return false;
		}
		for (int i=_commands.size()-1; i>=0; i--) {
			Command command = _commands.get(i);
			if (command.canUndo()==false) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canRedo() {
		if (_commands.isEmpty()) {
			return false;
		}
		for (Command command : _commands) {
			if (command.canRedo()==false) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void doExecute() {
		for (Command command : _commands) {
			if (command.canExecute()) {
				command.execute();
			}
		}
	}

	@Override
	protected void doUndo() {
		for (int i=_commands.size()-1; i>=0; i--) {
			Command command = _commands.get(i);
			if (command.canUndo()) {
				command.undo();
			}
		}
	}

	@Override
	protected void doRedo() {
		for (Command command : _commands) {
			if (command.canRedo()) {
				command.redo();
			}
		}
	}

}
