package picturegallery.action;

import gallery.LinkedPicture;
import gallery.PictureCollection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
		if (exportedCollection == null) {
			return;
		}
		// TODO: vorher auswählen: nur Linked oder nur Real oder beides!
		List<LinkedPicture> linked = Logic.getLinkedPicturesOf(exportedCollection);
		if (linked.isEmpty()) {
			return;
		}

		String path = Logic.askForDirectory("Select a directory to export the linked pictures", true);
		if (path == null || path.startsWith(MainApp.get().getBaseCollection().getFullPath())) {
			return;
		}

		// copy each picture
		for (LinkedPicture link : linked) {
			try {
				Files.copy(
						new File(link.getFullPath()).toPath(),
						new File(path + File.separator + link.getName() + "." + link.getFileExtension()).toPath(),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
