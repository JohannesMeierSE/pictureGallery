package picturegallery.action;

import gallery.Picture;
import gallery.PictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

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

		// determine the maximum number of digits
		int digits = 0;
		int nothingTodo = 0;
		for (Picture pic : collectionToFix.getPictures()) {
			String number = Logic.getLastNumberSubstring(pic.getName());
			if (number == null || number.isEmpty()) {
				nothingTodo++;
			} else {
				digits = Math.max(digits, number.length());
			}
		}

		int size = collectionToFix.getPictures().size();
		if (size == nothingTodo) {
			return;
		}
		// determine the pictures to fix
		List<Picture> toFix = new ArrayList<>(size - nothingTodo);
		for (Picture pic : collectionToFix.getPictures()) {
			String number = Logic.getLastNumberSubstring(pic.getName());
			if (number == null || number.isEmpty()) {
				// do nothing
			} else if (number.length() < digits) {
				toFix.add(pic);
			}
		}

		if (!Logic.askForConfirmation("Fix picture names: running number",
				"From the " + size + " pictures, " + (size - nothingTodo) + " have a number at the end of its name, "
						+ toFix.size() + " of them needs a fix, "
						+ "and " + nothingTodo + " do not have any numbers.",
				"Do you really want to add missing leading zeros (up to " + digits + " digits)?")) {
			return;
		}

		final int finalDigits = digits;
		Logic.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (Picture pic : toFix) {
					String numberOld = Logic.getLastNumberSubstring(pic.getName());
					String prefix = pic.getName().substring(0, pic.getName().length() - numberOld.length());

					String numberNew = new String(numberOld);
					while (numberNew.length() < finalDigits) {
						numberNew = "0" + numberNew;
					}
					String newName = prefix + numberNew;

					MainApp.get().renamePicture(pic, newName);
				}
			}
		});
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.F;
	}

	@Override
	public String getDescription() {
		return "Fixes missing leading zeros in picture names.";
	}
}
