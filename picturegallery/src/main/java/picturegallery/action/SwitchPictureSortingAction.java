package picturegallery.action;

import gallery.Picture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.State;

public class SwitchPictureSortingAction extends Action {
	private final Comparator<Picture> byName;
	private final Comparator<Picture> byMonth;
	private final Comparator<Picture> bySize;

	public SwitchPictureSortingAction() {
		super();
		byName = MainApp.get().pictureComparator.get();
		byMonth = Logic.createComparatorPicturesMonth();
		bySize = Logic.createComparatorPicturesSize(false);
	}

	@Override
	public void run(State currentState) {
		SimpleObjectProperty<Comparator<Picture>> comp = MainApp.get().pictureComparator;
		List<String> options = new ArrayList<>();
		options.add(0, "Sort by name ascending");
		options.add(1, "Sort by 1. month, 2. day 3. year ascending");
		options.add(2, "Sort by size descending");
		int answer = Logic.askForChoice(options, true, "Order of shown pictures",
				"Select and change the order of the shown pictures", "Select one of the order options:");
		if (answer < 0) {
			return;
		}

		// set the matching comparator
		if (answer == 0) {
			comp.set(byName);
		} else if (answer == 1) {
			comp.set(byMonth);
		} else if (answer == 2) {
			comp.set(bySize);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.U;
	}

	@Override
	public String getDescription() {
		return "Select another order of the pictures to show";
	}
}