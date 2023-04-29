package picturegallery.action;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2023 Johannes Meier
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
import gallery.PictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.fix.FirstNumberInUnderscoreFixer;
import picturegallery.fix.NumberAtEndFixer;
import picturegallery.fix.NumberFixer;
import picturegallery.state.CollectionState;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;
import picturegallery.ui.TaskWithProgress;

public class FixPictureNumbersAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		PictureCollection collectionToFix = state.getSelection();
		if (collectionToFix == null) {
			return;
		}
		if (collectionToFix.getPictures().isEmpty()) {
			return;
		}

		// ask the user which method he wants to use
		List<NumberFixer> fixer = new ArrayList<>();
		fixer.add(0, new NumberAtEndFixer());
		fixer.add(1, new FirstNumberInUnderscoreFixer());

		List<String> fixerNames = new ArrayList<>(fixer.size());
		for (int i = 0; i < fixer.size(); i++) {
			fixerNames.add(i, fixer.get(i).getName());
		}

		int option = JavafxHelper.askForChoice(fixerNames, true, "Select fix mechanism",
				"There are different patterns for numbers with missing leading zeros.", "Select one pattern:");
		if (option < 0) {
			return;
		}
		NumberFixer fixerToUse = fixer.get(option);
		fixerToUse.initialize(); // initialize the fixer => e.g. ask for more information from the user (once)

		// determine the maximum number of digits
		int digits = 0;
		int noNumberAtTheEnd = 0; // counts the number of pictures which does not fit to the selected "pattern to fix"
		for (Picture pic : collectionToFix.getPictures()) {
			String number = fixerToUse.getNumberPart(pic.getName());
			if (number == null || number.isEmpty()) {
				noNumberAtTheEnd++;
			} else {
				digits = Math.max(digits, number.length());
			}
		}

		int size = collectionToFix.getPictures().size();
		if (noNumberAtTheEnd >= size) {
			// all pictures do not have a number at the end of their names
			return;
		}

		// determine the pictures to fix
		final List<Picture> toFix = new ArrayList<>(size - noNumberAtTheEnd);
		for (Picture pic : collectionToFix.getPictures()) {
			if (fixerToUse.shouldNumberBeFixed(pic.getName(), digits)) {
				toFix.add(pic);
				System.out.println(pic.getRelativePathWithoutBase());
			} else {
				// do nothing
			}
		}
		if (toFix.isEmpty()) {
			return;
		}

		if (!JavafxHelper.askForConfirmation("Fix picture names: running number",
				"From the " + size + " pictures, " + (size - noNumberAtTheEnd) + " have a number at the specified position, "
						+ toFix.size() + " of them need a fix (missing leading zeros up to " + digits + " digits OR other fixes), "
						+ "and " + noNumberAtTheEnd + " do not match the number pattern.",
				"Do you really want to fix them all?")) {
			return;
		}

		// the user has to wait and must not do other things (long running process)
		MainApp.get().switchToWaitingState(false);

		final int finalDigits = digits;
		JavafxHelper.runNotOnUiThread(new TaskWithProgress<Void>(MainApp.get().getWaitingState()) {
			@Override
			protected Void call() throws Exception {
				progress.updateProgressTitle(getDescription());
				progress.updateProgressMax(toFix.size());

				for (Picture pic : toFix) {
					progress.updateProgressDetails(pic.getRelativePath(), +1);
					String newName = fixerToUse.getNewComplete(pic.getName(), finalDigits);

					try {
						System.out.println(pic.getName() + " => " + newName);
						MainApp.get().renamePicture(pic, newName);
					} catch (Throwable e) {
						System.err.println("error while renaming " + pic.getFullPath() + " to " + newName);
						e.printStackTrace();
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
		return KeyCode.F;
	}

	@Override
	public String getDescription() {
		return "Fixes missing leading zeros in picture names (or other issues with file names).";
	}
}
