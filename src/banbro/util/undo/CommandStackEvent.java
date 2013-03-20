package banbro.util.undo;

public class CommandStackEvent {
	public static final int EXECUTE = 1;
	public static final int UNDO = 2;
	public static final int REDO = 3;
	public static final int CLEAR = 4;

	protected int _state;
	protected Command _command;
	protected CommandStack _stack;

	public CommandStackEvent(CommandStack stack, int state, Command command) {
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
