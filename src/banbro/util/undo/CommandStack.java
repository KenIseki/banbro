package banbro.util.undo;

import java.util.Stack;

import javax.swing.event.EventListenerList;

public class CommandStack {
	private final EventListenerList _listenerList = new EventListenerList();
	
	protected Stack<Command> _undoStack;
	protected Stack<Command> _redoStack;

	public CommandStack() {
		_undoStack = new Stack<Command>();
		_redoStack = new Stack<Command>();
	}

	public void addCommandStackListener(CommandStackListener l) {
		_listenerList.add(CommandStackListener.class, l);
	}
	public void removeCommandStackListener(CommandStackListener l) {
		_listenerList.remove(CommandStackListener.class, l);
	}
	public void fireCommandStackEvent(int state, Command command) {
		CommandStackEvent event = new CommandStackEvent(this, state, command);
		Object[] listeners = _listenerList.getListenerList();
		for(int i=listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==CommandStackListener.class) {
				((CommandStackListener)listeners[i+1]).commandStackChanged(event);
			}
		}
	}

	public void execute(Command command) {
		if (command!=null && command.canExecute()) {
			command.execute();
			_undoStack.push(command);
			fireCommandStackEvent(CommandStackEvent.EXECUTE, command);
		}
	}

	public void undo() {
		if (canUndo()) {
			Command command = _undoStack.pop();
			command.undo();
			_redoStack.push(command);
			fireCommandStackEvent(CommandStackEvent.UNDO, command);
		}
	}
	public void redo() {
		if (canRedo()) {
			Command command = _redoStack.pop();
			command.redo();
			_undoStack.push(command);
			fireCommandStackEvent(CommandStackEvent.REDO, command);
		}
	}

	public boolean canUndo() {
		return _undoStack.isEmpty()==false && _undoStack.peek().canUndo();
	}
	public boolean canRedo() {
		return _redoStack.isEmpty()==false && _redoStack.peek().canRedo();
	}
	public String getUndoName() {
		if (canUndo()) {
			return _undoStack.peek().getCommandName();
		}
		return null;
	}
	public String getRedoName() {
		if (canRedo()) {
			return _redoStack.peek().getCommandName();
		}
		return null;
	}

	public void clear() {
		_undoStack.clear();
		_redoStack.clear();
		fireCommandStackEvent(CommandStackEvent.CLEAR, null);
	}

}
