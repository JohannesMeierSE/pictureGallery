package picturegallery.action;

import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class SearchIdenticalAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

        Task<Void> task = new Task<Void>() {
        	@Override
        	protected Void call() throws Exception {
    			Logic.findIdenticalInOneCollection(state.getCurrentCollection());
				return null;
        	}
        };
        new Thread(task).start();
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.D;
	}

	@Override
	public String getDescription() {
		return "search for duplicates within this collection";
	}
}
