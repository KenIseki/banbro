package banbro.util.undo;

import java.util.EventObject;

public class CommandStackEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public static final int EXECUTE = 1;
	public static final int UNDO = 2;
	public static final int REDO = 3;
	public static final int CLEAR = 4;

	protected int _state;
	protected Command _command;
	protected CommandStack _stack;

	public CommandStackEvent(CommandStack stack, int state, Command command) {
		super(stack);
		_stack = stack;
		_state = state;
		_command = command;
	}

	public int getState() {
		return _state;
	}

	public Command getCommand() {
		return _command;
	}

	public CommandStack getCommandStack() {
		return _stack;
	}

}
