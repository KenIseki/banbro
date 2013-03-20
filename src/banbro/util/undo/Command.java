package banbro.util.undo;

public abstract class Command {
	private boolean isExecute;
	private boolean isUndo;

	public Command() {
		isExecute = false;
		isUndo = false;
	}

	public final void execute() {
		isExecute = true;
		isUndo = false;
		doExecute();
	}
	public final void undo() {
		isExecute = false;
		isUndo = true;
		doUndo();
	}
	public final void redo() {
		isExecute = true;
		isUndo = false;
		doRedo();
	}
	public boolean canExecute() {
		return isExecute==false;
	}
	public boolean canUndo() {
		return isUndo==false;
	}
	public boolean canRedo() {
		return canExecute();
	}
	public String getCommandName() {
		return null;
	}

	protected abstract void doExecute();
	protected abstract void doUndo();
	protected abstract void doRedo();

}
