package picturegallery.action;

import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class SearchIdenticalAndReplaceAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

        Task<Void> task = new Task<Void>() {
        	@Override
        	protected Void call() throws Exception {
    			Logic.replaceIdenticalPicturesInSubcollectionsByLink(state.getCurrentCollection());
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
	public boolean requiresShift() {
		return true;
	}

	@Override
	public String getDescription() {
		return "search for duplicated real pictures of the current collection in the (recursive) sub-collections and replace them by linked pictures";
	}
}
