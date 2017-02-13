package picturegallery.action;

import gallery.Picture;

import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class DeleteSelectedPicturesAction extends Action {
	private final List<? extends Picture> picturesToDelete;
	private final String title;
	private final String description;

	public DeleteSelectedPicturesAction(List<? extends Picture> picturesToDelete, String title, String description) {
		super();
		this.picturesToDelete = picturesToDelete;
		this.title = title;
		this.description = description;
	}

	@Override
	public void run(State currentState) {
		// the key will not be deleted => one picture will be kept!!
		if (!Logic.askForConfirmation(title + "?", description,
				"Do you really want to delete all the selected pictures?")) {
			return;
		}

		// close the state => prevents loading removed pictures again!
		MainApp.get().switchState(((CollectionState) currentState.getNextAfterClosed()).getWaitingState());
		currentState.onClose();

		Logic.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
				// delete the pictures
				for (Picture picToDelete : picturesToDelete) {
					MainApp.get().deletePicture(picToDelete, false);
				}

				// close the waiting state!
				Logic.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						MainApp.get().switchToPreviousState();
					}
				});
			}
		});
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.D;
	}

	@Override
	public String getDescription() {
		return title;
	}
}
