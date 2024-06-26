package picturegallery.action;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2024 Johannes Meier
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END-LICENSE
 */

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
import picturegallery.ui.JavafxHelper;
import picturegallery.ui.TaskWithProgress;

public class ExportLinkedPicturesAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof CollectionState == false) {
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
			int answer = JavafxHelper.askForChoice(options, true, "Export pictures",
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

		String path = JavafxHelper.askForDirectory("Select a directory to export the linked pictures", true);
		if (path == null || path.startsWith(MainApp.get().getBaseCollection().getFullPath())) {
			return;
		}

		// the user has to wait and must not do other things (long running process)
		MainApp.get().switchToWaitingState(false);

		JavafxHelper.runNotOnUiThread(new TaskWithProgress<Void>(MainApp.get().getWaitingState()) {
			@Override
			protected Void call() throws Exception {
				progress.updateProgressTitle(getDescription());
				progress.updateProgressMax(linked.size() + real.size());

				// copy each linked picture
				for (LinkedPicture link : linked) {
					Logic.copyPicture(path, link);
					progress.updateProgressDetails(link.getRelativePath(), +1);
				}

				// copy each real picture
				for (RealPicture realPicture : real) {
					Logic.copyPicture(path, realPicture);
					progress.updateProgressDetails(realPicture.getRelativePath(), +1);
				}

				// close the waiting state!
				MainApp.get().switchCloseWaitingState();
				return null;
			}
		});
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
