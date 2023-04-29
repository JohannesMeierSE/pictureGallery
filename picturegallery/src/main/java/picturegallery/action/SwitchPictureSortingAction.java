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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;

public class SwitchPictureSortingAction extends Action {
	private final Comparator<Picture> byNameAscending;
	private final Comparator<Picture> byNameDescending;
	private final Comparator<Picture> byMonth;
	private final Comparator<Picture> bySize;

	public SwitchPictureSortingAction() {
		super();
		byNameAscending = MainApp.get().pictureComparator.get();
		byNameDescending = Logic.createComparatorPicturesName(false);
		byMonth = Logic.createComparatorPicturesMonth();
		bySize = Logic.createComparatorPicturesSize(false);
	}

	@Override
	public void run(State currentState) {
		SimpleObjectProperty<Comparator<Picture>> comp = MainApp.get().pictureComparator;
		List<String> options = new ArrayList<>();
		options.add(0, "Sort by name ascending");
		options.add(1, "Sort by name descending");
		options.add(2, "Sort by 1. month, 2. day 3. year ascending");
		options.add(3, "Sort by size descending");
		int answer = JavafxHelper.askForChoice(options, true, "Order of shown pictures",
				"Select and change the order of the shown pictures", "Select one of the order options:");
		if (answer < 0) {
			return;
		}

		// set the matching comparator
		if (answer == 0) {
			comp.set(byNameAscending);
		} else if (answer == 1) {
			comp.set(byNameDescending);
		} else if (answer == 2) {
			comp.set(byMonth);
		} else if (answer == 3) {
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
