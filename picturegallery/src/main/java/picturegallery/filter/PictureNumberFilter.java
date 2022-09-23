package picturegallery.filter;

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

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import org.controlsfx.control.CheckComboBox;

import gallery.PictureCollection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import picturegallery.Logic;
import picturegallery.MainApp;

public class PictureNumberFilter extends CollectionFilter {
	public static class IntegerFilter implements UnaryOperator<TextFormatter.Change> {
		// https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
		private final static Pattern DIGIT_PATTERN = Pattern.compile("-?\\d*");

		@Override
		public Change apply(TextFormatter.Change change) {
			return DIGIT_PATTERN.matcher(change.getText()).matches() ? change : null;
		}
	}

	private final SimpleObjectProperty<Integer> min;
	private final SimpleObjectProperty<Integer> max;

	private final CheckComboBox<String> extensionCombo;

	public PictureNumberFilter(CompositeCollectionFilter parentFilter) {
		super(parentFilter);

		min = new SimpleObjectProperty<Integer>(0);
		min.addListener(listenerForNotification);
		max = new SimpleObjectProperty<Integer>(-1);
		max.addListener(listenerForNotification);

		// init the UI
		Label minLabel = new Label("min:");
		Label maxLabel = new Label("max:");

		// https://stackoverflow.com/questions/40472668/numeric-textfield-for-integers-in-javafx-8-with-textformatter-and-or-unaryoperat
		// modified version of standard converter that evaluates an empty string as zero instead of null
		StringConverter<Integer> converter = new IntegerStringConverter() {
			@Override
			public Integer fromString(String s) {
				if (s == null || s.isEmpty()) {
					return 0;
				} else {
					return super.fromString(s);
				}
			}
		};
		// https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
		TextField minField = new TextField();
		TextFormatter<Integer> minFormatter = new TextFormatter<>(converter, 0, new IntegerFilter());
		minFormatter.valueProperty().bindBidirectional(min);
		minField.setTextFormatter(minFormatter);
		minField.setMaxWidth(60.0);

		TextField maxField = new TextField();
		TextFormatter<Integer> maxFormatter = new TextFormatter<>(converter, 0, new IntegerFilter());
		maxFormatter.valueProperty().bindBidirectional(max);
		maxField.setTextFormatter(maxFormatter);
		maxField.setMaxWidth(60.0);

		extensionCombo = new CheckComboBox<>(MainApp.get().extensions);
		extensionCombo.getCheckModel().checkAll();
		extensionCombo.getCheckModel().getCheckedItems().addListener(listenerForNotification);

		hbox.getChildren().addAll(minLabel, minField, maxLabel, maxField, extensionCombo);
	}

	@Override
	protected boolean isUsableLogic(PictureCollection collection) {
		ObservableList<String> checkedItems = extensionCombo.getCheckModel().getCheckedItems();
		if (checkedItems.isEmpty()) {
			return false;
		}
		/* Das Ganze muss dynamisch sein und sich beim Löschen von Bildern sofort ändern!
		 * - In DIESEM Fall hier ist das unnötig, da die Zellen bereits beim Ändern der Anzahl Bilder aktualisiert werden ...
		 * - ... wegen der Spalte "Anzahl Bilder"!
		 */
		Map<String, AtomicInteger> extensionMap = Logic.getExtensionMap(Logic.getRealCollection(collection));

		// count the number of pictures with one of the selected file extensions
		int size = 0;
		for (String extension : checkedItems) {
			AtomicInteger count = extensionMap.get(extension);
			if (count != null) {
				size += count.get();
			}
		}

		if (size < min.get()) {
			return false;
		}

		int maximum = max.get();
		if (maximum < 0) {
			// allow an unlimited number of pictures
			return true;
		} else {
			return size <= maximum;
		}
	}

	@Override
	public Node getUI() {
		return hbox;
	}

	@Override
	public void close() {
		// TODO: unbind
		super.close();
	}
}
