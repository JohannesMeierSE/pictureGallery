package picturegallery.action;

import gallery.Picture;
import gallery.PictureCollection;
import javafx.scene.input.KeyCode;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class DebuggerCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		PictureCollection currentCollection = state.getSelection();
		if (currentCollection == null) {
			return;
		}

		// debug action: print all shown picture names
		System.out.println();
		for (Picture pic : currentCollection.getPictures()) {
			System.out.println(pic.getName() + "." + pic.getFileExtension());
		}
		System.out.println();
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.Y;
	}

	@Override
	public String getDescription() {
		return "Executes some special debug action for the selected collection.";
	}
}
