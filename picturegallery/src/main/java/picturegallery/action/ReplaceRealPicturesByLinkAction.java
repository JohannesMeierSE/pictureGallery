package picturegallery.action;

import gallery.RealPicture;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.State;

public class ReplaceRealPicturesByLinkAction extends Action {
	private final Map<RealPicture, List<RealPicture>> replaceMap;
	private final String title;
	private final String description;

	public ReplaceRealPicturesByLinkAction(Map<RealPicture, List<RealPicture>> replaceMap,
			String title, String description) {
		super();
		this.replaceMap = replaceMap;
		this.title = title;
		this.description = description;
	}

	@Override
	public void run(State currentState) {
		// the key will not be replaced => one real picture will be kept!!
		if (!Logic.askForConfirmation(title + "?", description,
				"Do you really want to replace all the selected pictures by links?")) {
			return;
		}

		// close the state => prevents loading removed pictures again!
		MainApp.get().switchToPreviousState();
		currentState.onClose();

		Logic.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (Entry<RealPicture, List<RealPicture>> e : replaceMap.entrySet()) {
					for (RealPicture picToDelete : e.getValue()) {
						Logic.replaceRealByLinkedPicture(picToDelete, e.getKey());
					}
				}
			}
		});
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.R;
	}

	@Override
	public String getDescription() {
		return title;
	}
}
