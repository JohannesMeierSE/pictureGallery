package picturegallery.action;

import gallery.Picture;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.State;

public class DeleteAndMoveMappedPicturesAction extends Action {
	private final Map<RealPicture, List<RealPicture>> map;

	public DeleteAndMoveMappedPicturesAction(Map<RealPicture, List<RealPicture>> map) {
		super();
		this.map = map;
	}

	@Override
	public void run(State currentState) {
		// the key will not be deleted => one picture will be kept!!
		if (!Logic.askForConfirmation("Delete and move?",
				"Now, all the mapped values will be removed and all the keys will be moved into one single collection.",
				"Do you really want to proceed?")) {
			return;
		}

		RealPictureCollection movetoCollection = (RealPictureCollection) Logic.selectCollection(
				currentState,
				true, true, false, Collections.emptyList());
		if (movetoCollection == null) {
			return;
		}

		// close the state => prevents loading removed pictures again!
		MainApp.get().switchToWaitingState();
		currentState.onClose();

		Logic.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
				// 1. delete all mapped values
				for (Entry<RealPicture, List<RealPicture>> e : map.entrySet()) {
					for (Picture picToDelete : e.getValue()) {
						MainApp.get().deletePicture(picToDelete, false);
					}
				}

				// 2. move all keys
				for (Entry<RealPicture, List<RealPicture>> e : map.entrySet()) {
					MainApp.get().movePicture(e.getKey(), movetoCollection);
				}

				// close the waiting state!
				MainApp.get().switchCloseWaitingState();
			}
		});
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.M;
	}

	@Override
	public String getDescription() {
		return "Delete all the mapped values and move all the keys into one single collection";
	}
}
