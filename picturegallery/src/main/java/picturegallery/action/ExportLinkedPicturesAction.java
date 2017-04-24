package picturegallery.action;

import gallery.LinkedPicture;
import gallery.PictureCollection;
import gallery.RealPicture;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class ExportLinkedPicturesAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		// get the real collection for export
		PictureCollection exportedCollection = state.getSelection();
		if (exportedCollection == null || exportedCollection.getPictures().isEmpty()) {
			return;
		}

		List<LinkedPicture> linked = Logic.getLinkedPicturesOf(exportedCollection);
		List<RealPicture> real = Logic.getRealPicturesOf(exportedCollection);

		// real and linked pictures together in the same collection => ask the user whether real and/or linked pictures should be exported
		if (!linked.isEmpty() && !real.isEmpty()) {
			List<String> options = new ArrayList<>();
			options.add(0, "real and linked pictures");
			options.add(1, "only linked pictures");
			options.add(2, "only real pictures");
			int answer = Logic.askForChoice(options, true, "Export pictures",
					"Which kind of pictures do you want to export?", "Select the kind of exported pictures:");
			answer = Math.max(0, answer);
			if (answer == 1) {
				// only linked
				real.clear();
			} else if (answer == 2) {
				// only real
				linked.clear();
			}
		}

		String path = Logic.askForDirectory("Select a directory to export the linked pictures", true);
		if (path == null || path.startsWith(MainApp.get().getBaseCollection().getFullPath())) {
			return;
		}

		// copy each linked picture
		for (LinkedPicture link : linked) {
			Logic.copyPicture(path, link);
		}

		// copy each real picture
		for (RealPicture realPicture : real) {
			Logic.copyPicture(path, realPicture);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.E;
	}

	@Override
	public String getDescription() {
		return "Exports all the linked pictures contained in the selected collection as real pictures to the file system.";
	}
}
