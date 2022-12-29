package picturegallery.action;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2022 Johannes Meier
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

import gallery.RealPicture;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;
import picturegallery.ui.TaskWithProgress;

public class ReplaceRealPicturesByLinkAction extends Action {
	private final Map<RealPicture, List<RealPicture>> replaceMap;
	private final String title;
	private final String description;

	public ReplaceRealPicturesByLinkAction(Map<RealPicture, List<RealPicture>> replaceMap,
			String title, String description) {
		super();
		if (replaceMap.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.replaceMap = replaceMap;
		this.title = title;
		this.description = description;
	}

	@Override
	public void run(State currentState) {
		// the key will not be replaced => one real picture will be kept!!
		if (!JavafxHelper.askForConfirmation(title + "?", description,
				"Do you really want to replace all the selected pictures by links?")) {
			return;
		}

		// the user has to wait and must not do other things (long running process)
		MainApp.get().switchToWaitingState(true);

		JavafxHelper.runNotOnUiThread(new TaskWithProgress<Void>(MainApp.get().getWaitingState()) {
			@Override
			protected Void call() throws Exception {
				progress.updateProgressTitle(getDescription());
				progress.updateProgressMax(replaceMap.values().stream().mapToInt(list -> list.size()).sum());

				for (Entry<RealPicture, List<RealPicture>> e : replaceMap.entrySet()) {
					for (RealPicture picToDelete : e.getValue()) {
						progress.updateProgressDetails(e.getKey().getRelativePath() + " <-- " + picToDelete.getRelativePath(), +1);
						Logic.replaceRealByLinkedPicture(picToDelete, e.getKey());
					}
				}

				// close the waiting state!
				MainApp.get().switchCloseWaitingState();
				return null;
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
