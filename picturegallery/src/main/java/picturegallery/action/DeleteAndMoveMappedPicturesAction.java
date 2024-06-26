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

import gallery.Picture;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;
import picturegallery.ui.TaskWithProgress;

public class DeleteAndMoveMappedPicturesAction extends Action {
	private final Map<RealPicture, List<RealPicture>> map;

	public DeleteAndMoveMappedPicturesAction(Map<RealPicture, List<RealPicture>> map) {
		super();
		this.map = map;
	}

	@Override
	public void run(State currentState) {
		// the key will not be deleted => one picture will be kept!!
		if (!JavafxHelper.askForConfirmation("Delete and move?",
				"Now, all the mapped values will be removed and all the keys will be moved into one single collection.",
				"Do you really want to proceed?")) {
			return;
		}

		RealPictureCollection movetoCollection = (RealPictureCollection) JavafxHelper.selectCollection(
				currentState,
				true, true, false, Collections.emptyList());
		if (movetoCollection == null) {
			return;
		}

		// close the state => prevents loading removed pictures again!
		MainApp.get().switchToWaitingState(true);

		JavafxHelper.runNotOnUiThread(new TaskWithProgress<Void>(MainApp.get().getWaitingState()) {
			@Override
			protected Void call() throws Exception {
				progress.updateProgressTitle(getDescription());
				progress.updateProgressMax(map.values().stream().mapToInt(list -> list.size()).sum() + map.size());

				// 1. delete all mapped values
				for (Entry<RealPicture, List<RealPicture>> e : map.entrySet()) {
					for (Picture picToDelete : e.getValue()) {
						progress.updateProgressDetails("delete " + picToDelete.getRelativePath(), +1);
						MainApp.get().deletePicture(picToDelete, false);
					}
				}

				// 2. move all keys
				for (Entry<RealPicture, List<RealPicture>> e : map.entrySet()) {
					progress.updateProgressDetails("move " + e.getKey().getRelativePath(), +1);
					MainApp.get().movePicture(e.getKey(), movetoCollection);
				}

				// close the waiting state!
				MainApp.get().switchCloseWaitingState();
				return null;
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
