package banbro.util.undo;

import java.util.EventListener;

public interface CommandStackListener extends EventListener {
	void commandStackChanged(CommandStackEvent event);
}
