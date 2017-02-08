package picturegallery.action;

import gallery.Picture;

import java.util.Comparator;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.State;

public class SwitchPictureSortingAction extends Action {
	private final Comparator<Picture> byName;
	private final Comparator<Picture> byMonth;

	public SwitchPictureSortingAction() {
		super();
		byName = MainApp.get().pictureComparator.get();
		byMonth = Logic.createComparatorPicturesMonth();
	}

	@Override
	public void run(State currentState) {
		SimpleObjectProperty<Comparator<Picture>> comp = MainApp.get().pictureComparator;
		// TODO: sollte schöner mit einem Dialog und mehr Optionen realisiert werden!
		if (comp.get() == byName) {
			comp.set(byMonth);
		} else {
			comp.set(byName);
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
