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
		int noNumberAtTheEnd = 0;
		for (Picture pic : collectionToFix.getPictures()) {
			String number = Logic.getLastNumberSubstring(pic.getName());
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
			String number = Logic.getLastNumberSubstring(pic.getName());
			if (number == null || number.isEmpty()) {
				// do nothing
			} else if (number.length() < digits) {
				toFix.add(pic);
				System.out.println(pic.getName() + "." + pic.getFileExtension());
			}
		}
		if (toFix.isEmpty()) {
			return;
		}

		if (!Logic.askForConfirmation("Fix picture names: running number",
				"From the " + size + " pictures, " + (size - noNumberAtTheEnd) + " have a number at the end of its name, "
						+ toFix.size() + " of them needs a fix, "
						+ "and " + noNumberAtTheEnd + " do not have any numbers.",
				"Do you really want to add missing leading zeros (up to " + digits + " digits)?")) {
			return;
		}

		// the user has to wait and must not do other things (long running process)
		MainApp.get().switchToWaitingState();

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

					try {
						MainApp.get().renamePicture(pic, newName);
					} catch (Throwable e) {
						System.err.println("error while renaming " + pic.getFullPath() + " to " + newName);
						e.printStackTrace();
					}
				}

				// close the waiting state!
				MainApp.get().switchCloseWaitingState();
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